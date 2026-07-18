package cires.bemodule.services;

import cires.bemodule.dtos.AuditFilterRequest;
import cires.bemodule.dtos.RevisionDetail;
import cires.bemodule.entities.*;
import cires.bemodule.entities.CustomRevisionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    // Every @Audited entity that should be searchable from the global feed.
    // NOTE: keep this list in sync with your actual entities, and double-check
    // the string keys below against whatever EntityTypeMapper.fromString()
    // expects on the /audit/{entityType}/{id} path — I don't have that file's
    // source, so these are my best guess at matching your frontend's
    // ENTITY_TYPES values (user/session/participant/attendance/trainer/
    // organization/notification/role); the rest are filled in for completeness.
    private static final Map<Class<?>, String> AUDITED_ENTITY_TYPES = Map.ofEntries(
            Map.entry(User.class, "user"),
            Map.entry(TrainingSession.class, "session"),
            Map.entry(Participant.class, "participant"),
            Map.entry(Attendance.class, "attendance"),
            Map.entry(Trainer.class, "trainer"),
            Map.entry(Organization.class, "organization"),
            Map.entry(Notification.class, "notification"),
            Map.entry(Role.class, "role"),
            Map.entry(Dashboard.class, "dashboard"),
            Map.entry(ExportHistory.class, "exporthistory"),
            Map.entry(Permission.class, "permission"),
            Map.entry(SessionParticipant.class, "sessionparticipant"),
            Map.entry(Subsidiary.class, "subsidiary")
    );

    private AuditReader getReader() {
        return AuditReaderFactory.get(entityManager);
    }

    // ---------- Basic queries (single entity) ----------

    public Page<RevisionDetail> getRevisionsForEntity(Class<?> clazz, Long entityId, Pageable pageable,
                                                      String username, String ipAddress) {
        List<Object[]> results = buildEntityQuery(clazz, entityId, username, ipAddress)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<RevisionDetail> details = mapToRevisionDetails(results, entityTypeKeyFor(clazz));
        long total = countEntityRevisions(clazz, entityId, username, ipAddress);
        return new PageImpl<>(details, pageable, total);
    }

    public Object getEntityAtRevision(Class<?> clazz, Long entityId, int revisionNumber) {
        AuditReader reader = getReader();
        return reader.find(clazz, entityId, revisionNumber);
    }

    // ---------- Global queries (across all audited entities) ----------
    //
    // Envers' AuditQuery doesn't support `forRevisionsOfEntity(null, ...)` as
    // "all entities" - each audited entity type has its own _AUD table, so
    // there is no single query across all of them. This runs one query per
    // audited entity (with the same filters/order applied to each), merges
    // the results in memory, sorts by timestamp, then trims/paginates.
    //
    // Trade-off: at real scale this does N queries instead of 1 and fetches
    // more rows than strictly needed before trimming. For this app's stated
    // usage profile (internal tool, moderate audit volume) that's an
    // acceptable cost. If audit volume grows significantly, look into
    // Envers' `track_entities_changed_in_revision` property, which adds a
    // side table you can query directly instead of doing this fan-out.

    public List<RevisionDetail> getRecentGlobalChanges(int limit) {
        List<RevisionDetail> all = new ArrayList<>();
        for (Class<?> clazz : AUDITED_ENTITY_TYPES.keySet()) {
            List<Object[]> results = getReader().createQuery()
                    .forRevisionsOfEntity(clazz, false, true)
                    .addOrder(AuditEntity.revisionNumber().desc())
                    .setMaxResults(limit)
                    .getResultList();
            all.addAll(mapToRevisionDetails(results, entityTypeKeyFor(clazz)));
        }
        all.sort(Comparator.comparing((RevisionDetail r) -> r.getRevisionInfo().getTimestamp()).reversed());
        return all.size() > limit ? all.subList(0, limit) : all;
    }

    public Page<RevisionDetail> getFilteredGlobalRevisions(String username, String ipAddress,
                                                           RevisionType action, LocalDateTime fromDate,
                                                           LocalDateTime toDate, Pageable pageable) {
        List<RevisionDetail> all = new ArrayList<>();
        for (Class<?> clazz : AUDITED_ENTITY_TYPES.keySet()) {
            AuditQuery query = buildGlobalQuery(clazz, username, ipAddress, action, fromDate, toDate);
            query.addOrder(AuditEntity.revisionNumber().desc());
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();
            all.addAll(mapToRevisionDetails(results, entityTypeKeyFor(clazz)));
        }
        all.sort(Comparator.comparing((RevisionDetail r) -> r.getRevisionInfo().getTimestamp()).reversed());

        long total = all.size();
        int from = Math.min((int) pageable.getOffset(), all.size());
        int to = Math.min(from + pageable.getPageSize(), all.size());
        List<RevisionDetail> pageContent = all.subList(from, to);
        return new PageImpl<>(pageContent, pageable, total);
    }

    // ---------- Advanced filtering (single entity, POST body) ----------

    public List<RevisionDetail> getRevisionsWithFilters(Class<?> clazz, Long entityId,
                                                        AuditFilterRequest filter, int maxResults) {
        AuditQuery query = getReader().createQuery()
                .forRevisionsOfEntity(clazz, false, true)
                .add(AuditEntity.id().eq(entityId))
                .addOrder(AuditEntity.revisionNumber().desc());

        applyCommonFilters(query, filter.getUsername(), filter.getIpAddress());
        if (filter.getAction() != null) {
            query.add(AuditEntity.revisionType().eq(filter.getAction()));
        }
        if (filter.getFromDate() != null) {
            query.add(AuditEntity.revisionProperty("timestamp").ge(toEpochMillis(filter.getFromDate())));
        }
        if (filter.getToDate() != null) {
            query.add(AuditEntity.revisionProperty("timestamp").le(toEpochMillis(filter.getToDate())));
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        return mapToRevisionDetails(results, entityTypeKeyFor(clazz));
    }

    // ---------- Query builders ----------

    private AuditQuery buildEntityQuery(Class<?> clazz, Long entityId, String username, String ipAddress) {
        AuditQuery query = getReader().createQuery()
                .forRevisionsOfEntity(clazz, false, true)
                .add(AuditEntity.id().eq(entityId))
                .addOrder(AuditEntity.revisionNumber().desc());
        applyCommonFilters(query, username, ipAddress);
        return query;
    }

    private AuditQuery buildGlobalQuery(Class<?> clazz, String username, String ipAddress,
                                        RevisionType action, LocalDateTime fromDate, LocalDateTime toDate) {
        AuditQuery query = getReader().createQuery()
                .forRevisionsOfEntity(clazz, false, true);
        applyCommonFilters(query, username, ipAddress);
        if (action != null) {
            query.add(AuditEntity.revisionType().eq(action));
        }
        if (fromDate != null) {
            query.add(AuditEntity.revisionProperty("timestamp").ge(toEpochMillis(fromDate)));
        }
        if (toDate != null) {
            query.add(AuditEntity.revisionProperty("timestamp").le(toEpochMillis(toDate)));
        }
        return query;
    }

    private void applyCommonFilters(AuditQuery query, String username, String ipAddress) {
        if (username != null && !username.isEmpty()) {
            query.add(AuditEntity.revisionProperty("username").eq(username));
        }
        if (ipAddress != null && !ipAddress.isEmpty()) {
            query.add(AuditEntity.revisionProperty("ipAddress").eq(ipAddress));
        }
    }

    private long toEpochMillis(LocalDateTime dt) {
        return dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private long countEntityRevisions(Class<?> clazz, Long entityId,
                                      String username, String ipAddress) {
        AuditQuery query = getReader().createQuery()
                .forRevisionsOfEntity(clazz, false, true)
                .add(AuditEntity.id().eq(entityId));
        applyCommonFilters(query, username, ipAddress);
        query.addProjection(AuditEntity.id().count());
        Object result = query.getSingleResult();
        return ((Number) result).longValue();
    }

    private long countGlobalRevisions(String username, String ipAddress, RevisionType action,
                                      LocalDateTime fromDate, LocalDateTime toDate) {
        long total = 0;
        for (Class<?> clazz : AUDITED_ENTITY_TYPES.keySet()) {
            AuditQuery query = buildGlobalQuery(clazz, username, ipAddress, action, fromDate, toDate);
            query.addProjection(AuditEntity.id().count());
            Object result = query.getSingleResult();
            total += ((Number) result).longValue();
        }
        return total;
    }

    // ---------- Helpers ----------

    private String entityTypeKeyFor(Class<?> clazz) {
        return AUDITED_ENTITY_TYPES.getOrDefault(clazz, clazz.getSimpleName().toLowerCase());
    }

    private Long extractEntityId(Object entity) {
        if (entity == null) return null;
        try {
            Method getId = entity.getClass().getMethod("getId");
            Object id = getId.invoke(entity);
            return (id instanceof Long) ? (Long) id : null;
        } catch (Exception e) {
            return null;
        }
    }

    private List<RevisionDetail> mapToRevisionDetails(List<Object[]> results, String entityType) {
        return results.stream()
                .map(row -> new RevisionDetail(
                        (CustomRevisionEntity) row[1],
                        row[0],
                        (RevisionType) row[2],
                        entityType,
                        extractEntityId(row[0])
                ))
                .collect(Collectors.toList());
    }
}
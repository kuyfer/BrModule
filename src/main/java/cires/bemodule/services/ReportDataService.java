package cires.bemodule.services;

import cires.bemodule.dtos.ReportData;
import cires.bemodule.dtos.ReportRequest;
import cires.bemodule.repositories.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportDataService {

    private final AttendanceRepository attendanceRepository;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReportData build(ReportRequest request) {
        return switch (request.getReportType()) {
            case ATTENDANCE_RATE -> buildAttendanceRate(request);
            case ABSENCE_SUMMARY -> buildAbsenceSummary(request);
            case SUBSIDIARY_BREAKDOWN -> buildSubsidiaryBreakdown(request);
        };
    }

    private String subtitle(ReportRequest r) {
        String period = "Période : " + r.getStartDate().format(DATE_FMT) + " – " + r.getEndDate().format(DATE_FMT);
        return period; // subsidiary/trainer name resolution intentionally left to the caller
        // if you want the actual filiale/formateur NAME (not just "filtré"),
        // inject SubsidiaryRepository/TrainerRepository here and look it up.
    }

    private ReportData buildAttendanceRate(ReportRequest r) {
        List<Object[]> rows = attendanceRepository.findAttendanceStatsBySession(
                r.getStartDate(), r.getEndDate(), r.getSubsidiaryId(), r.getTrainerId());

        List<String> columns = List.of("Session", "Formateur", "Filiale", "Participants", "Taux de présence");
        List<List<String>> data = new ArrayList<>();
        long totalPresent = 0, totalRecords = 0;

        for (Object[] row : rows) {
            String sessionTitle = (String) row[1];
            String trainer = row[2] + " " + row[3];
            String subsidiary = row[4] != null ? (String) row[4] : "—";
            long participants = (long) row[5];
            long total = (long) row[6];
            long present = (long) row[7];
            double rate = total == 0 ? 0 : (present * 100.0 / total);

            totalPresent += present;
            totalRecords += total;

            data.add(List.of(sessionTitle, trainer, subsidiary, String.valueOf(participants),
                    String.format("%.1f %%", rate)));
        }

        double globalRate = totalRecords == 0 ? 0 : (totalPresent * 100.0 / totalRecords);
        LinkedHashMap<String, String> summary = new LinkedHashMap<>();
        summary.put("Sessions incluses", String.valueOf(rows.size()));
        summary.put("Taux de présence global", String.format("%.1f %%", globalRate));

        return new ReportData("Rapport — Taux de présence", subtitle(r), columns, data, summary, LocalDateTime.now());
    }

    private ReportData buildAbsenceSummary(ReportRequest r) {
        List<Object[]> rows = attendanceRepository.findAbsences(
                r.getStartDate(), r.getEndDate(), r.getSubsidiaryId(), r.getTrainerId());

        List<String> columns = List.of("Participant", "Session", "Date", "Créneau", "Statut", "Commentaire");
        List<List<String>> data = new ArrayList<>();

        for (Object[] row : rows) {
            String participant = row[0] + " " + row[1];
            String session = (String) row[2];
            String date = row[3].toString();
            String slot = String.valueOf(row[4]);
            String status = String.valueOf(row[5]);
            String comment = row[6] != null ? (String) row[6] : "";
            data.add(List.of(participant, session, date, slot, status, comment));
        }

        LinkedHashMap<String, String> summary = new LinkedHashMap<>();
        summary.put("Total absences", String.valueOf(rows.size()));

        return new ReportData("Rapport — Absences", subtitle(r), columns, data, summary, LocalDateTime.now());
    }

    private ReportData buildSubsidiaryBreakdown(ReportRequest r) {
        // Reuses the per-session query and aggregates in memory, rather than
        // a separate multi-join JPQL query - see repository note above.
        List<Object[]> sessionRows = attendanceRepository.findAttendanceStatsBySession(
                r.getStartDate(), r.getEndDate(), r.getSubsidiaryId(), r.getTrainerId());

        record Agg(long sessions, long participants, long present, long total) {}
        Map<String, Agg> bySubsidiary = new LinkedHashMap<>();

        for (Object[] row : sessionRows) {
            String subsidiary = row[4] != null ? (String) row[4] : "Non assignée";
            long participants = (long) row[5];
            long total = (long) row[6];
            long present = (long) row[7];

            Agg prev = bySubsidiary.getOrDefault(subsidiary, new Agg(0, 0, 0, 0));
            bySubsidiary.put(subsidiary, new Agg(
                    prev.sessions() + 1,
                    prev.participants() + participants, // NOTE: sums per-session distinct counts,
                    // so a participant attending sessions in
                    // the same filiale twice is counted twice.
                    // Fine for "volume", not for "headcount".
                    prev.present() + present,
                    prev.total() + total
            ));
        }

        List<String> columns = List.of("Filiale", "Sessions", "Participants (cumul)", "Taux de présence moyen");
        List<List<String>> data = new ArrayList<>();
        for (var entry : bySubsidiary.entrySet()) {
            Agg a = entry.getValue();
            double rate = a.total() == 0 ? 0 : (a.present() * 100.0 / a.total());
            data.add(List.of(entry.getKey(), String.valueOf(a.sessions()),
                    String.valueOf(a.participants()), String.format("%.1f %%", rate)));
        }

        LinkedHashMap<String, String> summary = new LinkedHashMap<>();
        summary.put("Filiales incluses", String.valueOf(bySubsidiary.size()));

        return new ReportData("Rapport — Répartition par filiale", subtitle(r), columns, data, summary, LocalDateTime.now());
    }
}
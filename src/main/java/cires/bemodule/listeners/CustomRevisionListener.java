//package cires.bemodule.listeners;
//
//import cires.bemodule.entities.CustomRevisionEntity;
//import jakarta.persistence.PrePersist;
//import lombok.RequiredArgsConstructor;
//import org.apache.coyote.RequestInfo;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//import java.util.function.Supplier;
//
//@Component
//@RequiredArgsConstructor
//public class CustomRevisionListener {
//    private final Supplier<Optional<RequestInfo>> requestInfoSupplier;
//
//    @PrePersist
//    private void onPersist(CustomRevisionEntity entity) {
//        var info = requestInfoSupplier.get();
//        if (info.isEmpty()) {
//            return;
//        }
//
//        entity.setRemoteHost(info.get().remoteHost());
//        entity.setRemoteUser(info.get().remoteUser());
//    }
//}

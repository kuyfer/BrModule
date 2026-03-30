//package cires.bemodule.listeners;
//
//import cires.bemodule.entities.CustomRevisionEntity;
//import org.apache.coyote.RequestInfo;
//import org.springframework.stereotype.Component;
//import jakarta.persistence.PrePersist;
//import java.util.Optional;
//import java.util.function.Supplier;
//
//@Component
//public class CustomRevisionListener {
//    private final Supplier<Optional<RequestInfo>> requestInfoSupplier;
//
//    public CustomRevisionListener(Supplier<Optional<RequestInfo>> requestInfoSupplier) {
//        this.requestInfoSupplier = requestInfoSupplier;
//    }
//
////    @PrePersist
////    public void onPersist(CustomRevisionEntity entity) {
////        requestInfoSupplier.get().ifPresent(info -> {
////            entity.setRemoteHost(info.getRemoteHost());
////            entity.setRemoteUser(info.get);
////        });
////    }
//}
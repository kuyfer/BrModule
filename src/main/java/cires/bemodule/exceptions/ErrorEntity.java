package cires.bemodule.exceptions;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.beans.Transient;
import java.time.LocalDateTime;

@Builder
public record ErrorEntity(

        LocalDateTime timeStamp,
        String message,
        @Transient
        String errorAuthor,
        int httpStatus

) {
}

package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "reset-tokens")
public class ResetToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiresAt;

    @ManyToOne
    private User user;

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

}

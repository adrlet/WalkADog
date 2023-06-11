package billennium.faculties.walkadog.domain;

import billennium.faculties.walkadog.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "CONFIRMATION_TOKEN")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN",nullable = false)
    private String token;

    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "EXPIRES_AT",nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "CONFIRMED_AT")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private Users users;
}

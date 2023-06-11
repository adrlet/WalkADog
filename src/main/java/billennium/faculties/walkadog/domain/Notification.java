package billennium.faculties.walkadog.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NOTIFICATIONS")
public class Notification {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Users user;

    @OneToOne
    @JoinColumn(name = "TRAINER_ID")
    private Trainer trainer;

    @Column(name = "SEND_TIME", nullable = false)
    private LocalDateTime sendTime;

    @Column(name = "TEXT", nullable = false)
    private  String text;

    @Column(name = "VIEWED")
    private boolean viewed;

}

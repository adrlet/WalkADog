package billennium.faculties.walkadog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WALK_REVIEW")
public class WalkReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "RATE", nullable = false)
    private Integer rate;

    @Column(name = "COMMENT")
    private String comment;

    ///CHECK IF USER SHOULD BE
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Users user;
    ///CHECK IF USER SHOULD BE

    @OneToOne
    @JoinColumn(name = "WALK_ID")
    private Walk walk;

}

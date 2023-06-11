package billennium.faculties.walkadog.domain;

import billennium.faculties.walkadog.domain.enums.PetStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETS")
public class Pets {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String petName;

    @Column(name = "RACE", nullable = false)
    private String race;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PREFERENCES")
    private String preferences;

    @Column(name = "RESTRICTIONS")
    private String restrictions;

    @Column(name = "AVATAR", columnDefinition = "BLOB")
    private byte[] avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "PET_STATUS")
    private PetStatus petStatus = PetStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Users users;

}

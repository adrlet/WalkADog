package billennium.faculties.walkadog.application.dto;

import billennium.faculties.walkadog.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetsDto {

    private String petName;
    private String race;
    private String description;
    private String preferences;
    private String restrictions;
    private byte[] avatar;
    private String petStatus;
    private Long userId;
    private Long petId;
}

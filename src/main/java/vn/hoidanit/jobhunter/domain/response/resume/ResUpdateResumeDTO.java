package vn.hoidanit.jobhunter.domain.response.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUpdateResumeDTO {
    private Instant updatedAt;
    private String updatedBy;

}

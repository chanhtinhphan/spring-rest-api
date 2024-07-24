package vn.hoidanit.jobhunter.domain.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;
@Getter
@Setter
public class ReqUpdateResumeDTO {
    private Long id;
    private ResumeStateEnum status;
}

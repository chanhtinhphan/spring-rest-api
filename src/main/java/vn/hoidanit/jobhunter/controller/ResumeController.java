package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.request.ReqUpdateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    @ApiMessage("create a resume")
    public ResponseEntity<ResCreateResumeDTO> create(@RequestBody @Valid Resume resume) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(resume));
    }

    @PutMapping
    @ApiMessage("update a resume")
    public ResponseEntity<ResUpdateResumeDTO> update(
            @RequestBody ReqUpdateResumeDTO reqUpdateResumeDTO) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.handleUpdateResume(reqUpdateResumeDTO));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete a resume by id")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdNotFoundException {
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    @ApiMessage("fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> getById(@PathVariable("id")  Long id) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.handleGetResumeById(id));
    }

    @GetMapping
    @ApiMessage("fetch a resume page")
    public ResponseEntity<ResultPaginationDTO> getPage(
            @Filter Specification<Resume> specification, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.resumeService.handleGetPageResume(specification, pageable));
    }
    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}

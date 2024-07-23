package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.dto.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@Controller
@RequestMapping("api/v1/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @ApiMessage("create a new job")
    public ResponseEntity<ResCreateJobDTO> create(@RequestBody @Valid Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreateJob(job));
    }

    @PutMapping
    @ApiMessage("update job")
    ResponseEntity<ResUpdateJobDTO> update(@RequestBody @Valid Job job) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleUpdateJob(job));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete job")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    @ApiMessage("get job by id")
    public ResponseEntity<Job> fetchById(@PathVariable("id") Long id) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleGetJobById(id));
    }

    @GetMapping
    @ApiMessage("get page job")
    public ResponseEntity<ResultPaginationDTO> fetchJobPage(
            @Filter Specification<Job> specification, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleGetPageJob(specification, pageable));
    }
}

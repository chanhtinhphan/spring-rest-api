package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    @ApiMessage("create a new skill")
    public ResponseEntity<Skill> create(@RequestBody @Valid Skill skill) throws ExistedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping
    @ApiMessage("update skill")
    public ResponseEntity<Skill> update(@RequestBody @Valid Skill skill) throws ExistedException, IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleUpdateSkill(skill));
    }

    @GetMapping
    @ApiMessage("get a page skill")
    public ResponseEntity<ResultPaginationDTO> fetch(@Filter Specification<Skill> specification,
                                                     Pageable pageable) {
        ResultPaginationDTO result = this.skillService.handleGetAllSkill(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleGetAllSkill(specification, pageable));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdNotFoundException {
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

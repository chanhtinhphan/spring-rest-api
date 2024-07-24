package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @ApiMessage("fetch companies")
    public ResponseEntity<ResultPaginationDTO> get(
            @Filter Specification<Company> specification,
            //Note : không có cái Filter thì sort trên url vẫn tự map vào Pageable -> cuối khóa check lại
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.companyService.handleGetAllCompany(specification, pageable));
    }

    @PostMapping
    @ApiMessage("create companies")
    public ResponseEntity<Company> create(@RequestBody @Valid Company company) {
        Company newCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @PutMapping
    @ApiMessage("update companies")
    public ResponseEntity<Company> update(@RequestBody @Valid Company company) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleUpdateCompany(company));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("delete companies by id")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    @ApiMessage("fetch company by id")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") Long id) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleGetCompanyById(id));
    }


}

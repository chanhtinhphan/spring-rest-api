package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
                      CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResCreateJobDTO handleCreateJob(Job job) {
        List<Skill> skillDBs = this.skillRepository.findByIdIn(job.getSkills().stream()
                .map((skill) -> skill.getId())
                .collect(Collectors.toList()));
        job.setSkills(skillDBs);
        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            if (company != null) job.setCompany(company);
        }
        Job savedJob = this.jobRepository.save(job);
        ResCreateJobDTO res = convertToResCreateDTO(savedJob);
        return res;
    }

    public ResUpdateJobDTO handleUpdateJob(Job job) throws IdNotFoundException {
        Job jobDB = this.jobRepository.findById(job.getId()).orElse(null);
        if (jobDB == null) {
            throw new IdNotFoundException("Job id not found");
        }

        jobDB.setName(job.getName());
        jobDB.setLocation(job.getLocation());
        jobDB.setSalary(job.getSalary());
        jobDB.setQuantity(job.getQuantity());
        jobDB.setLevel(job.getLevel());
        jobDB.setStartDate(job.getStartDate());
        jobDB.setEndDate(job.getEndDate());
        jobDB.setDescription(job.getDescription());
        jobDB.setActive(job.isActive());
        // cuối khóa hỏi : chỗ này if else check null rồi mới set cho an toàn ?????
        // or front-end lo????/
        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            if (company != null) jobDB.setCompany(company);
        }
        List<Skill> skillDBs = this.skillRepository.findByIdIn(job.getSkills().stream()
                .map((skill) -> skill.getId())
                .collect(Collectors.toList()));
        jobDB.setSkills(skillDBs);
        return convertToResUpdateDTO(this.jobRepository.save(jobDB));
    }

    public void handleDeleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }

    public Job handleGetJobById(Long id) throws IdNotFoundException {
        Job job = this.jobRepository.findById(id).orElse(null);
        if (job == null) {
            throw new IdNotFoundException("Job not found");
        }
        return job;
    }

    public ResultPaginationDTO handleGetPageJob(Specification<Job> specification, Pageable pageable) {
        return this.convertToResultPaginationDTO(specification, pageable);
    }

    private ResultPaginationDTO convertToResultPaginationDTO(
            Specification<Job> specification, Pageable pageable) {
        Page<Job> skillPage = this.jobRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(skillPage.getTotalPages());
        meta.setTotal(skillPage.getTotalElements());

        result.setMeta(meta);
        result.setResult(skillPage.getContent());
        return result;
    }

    private static ResUpdateJobDTO convertToResUpdateDTO(Job job) {
        return new ResUpdateJobDTO(
                job.getId(),
                job.getName(),
                job.getLocation(),
                job.getSalary(),
                job.getQuantity(),
                job.getLevel(),
                job.getStartDate(),
                job.getEndDate(),
                job.isActive(),
                job.getUpdatedAt(),
                job.getUpdatedBy(),
                job.getSkills() != null ? job.getSkills().stream()
                        .map(skill -> skill.getName())
                        .collect(Collectors.toList()) : null
        );
    }

    private static ResCreateJobDTO convertToResCreateDTO(Job job) {
        return new ResCreateJobDTO(
                job.getId(),
                job.getName(),
                job.getLocation(),
                job.getSalary(),
                job.getQuantity(),
                job.getLevel(),
                job.getStartDate(),
                job.getEndDate(),
                job.isActive(),
                job.getCreatedAt(),
                job.getCreatedBy(),
                job.getSkills() != null ? job.getSkills().stream()
                        .map(skill -> skill.getName())
                        .collect(Collectors.toList()) : null
        );
    }
}

package vn.hoidanit.jobhunter.service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.request.ReqUpdateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository,
                         JobRepository jobRepository, FilterParser filterParser, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) throws IdNotFoundException {
        if (resume.getUser() == null || resume.getJob() == null)
            throw new IdNotFoundException("user/job is emty");
        if (!this.userRepository.existsById(resume.getUser().getId())
                || !this.jobRepository.existsById(resume.getJob().getId()))
            throw new IdNotFoundException("user/job not found");
        Resume savedResume = this.resumeRepository.save(resume);
        return new ResCreateResumeDTO(savedResume.getId(), savedResume.getCreatedAt(), savedResume.getCreatedBy());
    }

    public ResUpdateResumeDTO handleUpdateResume(ReqUpdateResumeDTO reqUpdateResumeDTO) throws IdNotFoundException {
        Resume resumeDB = this.resumeRepository.findById(reqUpdateResumeDTO.getId()).orElse(null);
        if (resumeDB == null) throw new IdNotFoundException("remume id not found");
        resumeDB.setStatus(reqUpdateResumeDTO.getStatus());
        Resume updatedResume = this.resumeRepository.save(resumeDB);
        return new ResUpdateResumeDTO(updatedResume.getUpdatedAt(), updatedResume.getUpdatedBy());
    }

    public void handleDeleteResume(Long id) throws IdNotFoundException {
        if (!this.resumeRepository.existsById(id)) throw new IdNotFoundException("resume id not found");
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO handleGetResumeById(Long id) throws IdNotFoundException {
        Resume resumeDB = this.resumeRepository.findById(id).orElse(null);
        if (resumeDB == null) throw new IdNotFoundException("remume id not found");
        return converToResFetchResumeDTO(resumeDB);
    }

    public ResultPaginationDTO handleGetPageResume(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> resumePage = this.resumeRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumePage.getTotalPages());
        meta.setTotal(resumePage.getTotalElements());

        result.setMeta(meta);
        List<ResFetchResumeDTO> listDTO = resumePage.getContent().stream()
                .map(resume -> converToResFetchResumeDTO(resume))
                .collect(Collectors.toList());
        result.setResult(listDTO);
        return result;

    }

    private static ResFetchResumeDTO converToResFetchResumeDTO(Resume resume) {
        ResFetchResumeDTO.User resUser = new ResFetchResumeDTO.User(resume.getUser().getId(), resume.getUser().getName());
        ResFetchResumeDTO.Job resJob = new ResFetchResumeDTO.Job(resume.getJob().getId(), resume.getJob().getName());
        return new ResFetchResumeDTO(
                resume.getId(), resume.getEmail(), resume.getUrl(), resume.getStatus(),
                resume.getCreatedAt(), resume.getUpdatedAt(), resume.getCreatedBy(), resume.getUpdatedBy(),
                resUser, resJob, resume.getJob() != null ? resume.getJob().getCompany().getName() : null);
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(item -> converToResFetchResumeDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }

}

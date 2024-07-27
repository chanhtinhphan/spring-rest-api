package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }


    public Skill handleCreateSkill(Skill skill) throws ExistedException {
        if (this.skillRepository.existsByName(skill.getName())) {
            throw new ExistedException("This skill is existed in db");
        }
        return this.skillRepository.save(skill);
    }

    public Skill handleUpdateSkill(Skill skill) throws ExistedException, IdNotFoundException {
        if (this.skillRepository.existsByName(skill.getName())) {
            throw new ExistedException("This skill is existed in db");
        }
        if (!this.skillRepository.existsById(skill.getId())) {
            throw new IdNotFoundException("Id not found");
        }
        Skill skillDB = this.skillRepository.findById(skill.getId()).get();
        skillDB.setName(skill.getName());
        return this.skillRepository.save(skillDB);
    }

    public ResultPaginationDTO handleGetAllSkill(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> skillPage = this.skillRepository.findAll(specification, pageable);
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

    public void handleDeleteSkill(Long id) throws IdNotFoundException {
        Skill currentSkill = this.skillRepository.findById(id).orElse(null);
        if (currentSkill == null) throw new IdNotFoundException("skill id not found");
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        currentSkill.getSubscribers().forEach(subs -> subs.getSkills().remove(currentSkill));

        this.skillRepository.delete(currentSkill);
    }

}

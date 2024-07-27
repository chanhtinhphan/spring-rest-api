package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public Subscriber create(Subscriber reqSubscriber) throws ExistedException {
        if (this.subscriberRepository.existsByEmail(reqSubscriber.getEmail()))
            throw new ExistedException("subscriber has been existed");
        if (reqSubscriber.getSkills() != null) {
            List<Long> skillIds = reqSubscriber.getSkills().stream()
                    .map(skill -> skill.getId()).collect(Collectors.toList());
            List<Skill> skillDB = this.skillRepository.findByIdIn(skillIds);
            reqSubscriber.setSkills(skillDB);
        }
        return this.subscriberRepository.save(reqSubscriber);
    }

    public Subscriber update(Subscriber reqSubscriber) throws IdNotFoundException {
        Subscriber subscriberDB = this.subscriberRepository.findById(reqSubscriber.getId()).orElse(null);
        if (subscriberDB == null) throw new IdNotFoundException("Subscriber not found");
        if (reqSubscriber.getSkills() != null) {
            List<Long> skillIds = reqSubscriber.getSkills().stream()
                    .map(skill -> skill.getId()).collect(Collectors.toList());
            List<Skill> skillDB = this.skillRepository.findByIdIn(skillIds);
            subscriberDB.setSkills(skillDB);
        }
        return this.subscriberRepository.save(subscriberDB);
    }
}

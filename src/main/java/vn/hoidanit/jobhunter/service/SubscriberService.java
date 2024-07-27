package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.JobRepository;
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
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(
            SubscriberRepository subscriberRepository,
            SkillRepository skillRepository,
            JobRepository jobRepository,
            EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
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

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
// List<ResEmailJob> arr = listJobs.stream().map(
// job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                listJobs);
                    }
                }
            }
        }
    }
}

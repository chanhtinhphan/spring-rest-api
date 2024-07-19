package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }
    public List<Company> hanldeGetAllCompany(){
        return this.companyRepository.findAll();
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> optCompany = companyRepository.findById(company.getId());
        if (optCompany.isPresent()) {
            Company updatedCompany = optCompany.get();
            updatedCompany.setName(company.getName());
            updatedCompany.setDescription(company.getDescription());
            updatedCompany.setAddress(company.getAddress());
            updatedCompany.setLogo(updatedCompany.getLogo());
            return this.companyRepository.save(updatedCompany);
        }
        return null;
    }
    public void handleDeleteCompany(Long id){
       this.companyRepository.deleteById(id);
    }
}

package cires.bemodule.services;


import cires.bemodule.entities.Subsidiary;
import cires.bemodule.repositories.OrganizationRepository;
import cires.bemodule.repositories.SubsidiaryRepository;
import org.springframework.stereotype.Service;

@Service
public class SubsidiaryService {

    private final SubsidiaryRepository subsidiaryRepository;
    private final OrganizationRepository organizationRepository;

    public SubsidiaryService(SubsidiaryRepository subsidiaryRepository, OrganizationRepository organizationRepository) {this.subsidiaryRepository = subsidiaryRepository;
        this.organizationRepository = organizationRepository;
    }


    public  Subsidiary createSubsidiary(String name, String address){
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setName(name);
        subsidiary.setAddress(address);
        return subsidiaryRepository.save(subsidiary);
    }

    public Subsidiary findSubsidiaryByName(String name){
        return subsidiaryRepository.findByName(name);
    }

    public Subsidiary findSubsidiaryById(Long id){return subsidiaryRepository.findById(id).orElseThrow();}
}

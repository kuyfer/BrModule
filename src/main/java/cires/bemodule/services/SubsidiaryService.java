package cires.bemodule.services;

import cires.bemodule.entities.Subsidiary;
import cires.bemodule.repositories.SubsidiaryRepository;
import org.springframework.stereotype.Service;

@Service
public class SubsidiaryService {

    private final SubsidiaryRepository subsidiaryRepository;

    public SubsidiaryService(SubsidiaryRepository subsidiaryRepository) {this.subsidiaryRepository = subsidiaryRepository;}

    public  Subsidiary createSubsidiary(String name){
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setName(name);
        return subsidiaryRepository.save(subsidiary);
    }

    public Subsidiary findSubsidiaryByName(String name){
        return subsidiaryRepository.findByName(name);
    }

    public Subsidiary findSubsidiaryById(Long id){return subsidiaryRepository.findById(id).orElseThrow();}


}

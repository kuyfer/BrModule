package cires.bemodule.dev;


import cires.bemodule.entities.Organization;
import cires.bemodule.entities.Subsidiary;
import cires.bemodule.repositories.OrganizationRepository;
import cires.bemodule.repositories.SubsidiaryRepository;
import cires.bemodule.services.OrganizationService;
import cires.bemodule.services.SubsidiaryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final OrganizationService organizationService;
    private final OrganizationRepository organizationRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final  SubsidiaryService subsidiaryService;

    public DataSeeder(OrganizationService organizationService, OrganizationRepository organizationRepository, SubsidiaryRepository subsidiaryRepository, SubsidiaryService subsidiaryService) {
        this.organizationService = organizationService;
        this.organizationRepository = organizationRepository;
        this.subsidiaryRepository = subsidiaryRepository;
        this.subsidiaryService = subsidiaryService;
    }

    @Override
    public void run(String... args) throws Exception {


            Organization test = new Organization();
            test.setName("test");
            organizationRepository.save(test);


        Subsidiary sub1 = new Subsidiary();
        sub1.setName("yawyaw alpha");
        sub1.setAddress("123 Main St");
        subsidiaryRepository.save(sub1);

        organizationService.addSubsidiary(sub1, test);

    }
}
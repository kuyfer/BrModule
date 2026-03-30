package cires.bemodule.dev;

import cires.bemodule.entities.Subsidiary;
import cires.bemodule.services.OrganizationService;
import cires.bemodule.services.SubsidiaryService;
import cires.bemodule.services.TrainerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final OrganizationService organizationService;
    private final TrainerService trainerService;
    private final SubsidiaryService subsidiaryService;

    public DataSeeder(OrganizationService organizationService, TrainerService trainerService, SubsidiaryService subsidiaryService) {
        this.organizationService = organizationService;
        this.trainerService = trainerService;
        this.subsidiaryService = subsidiaryService;
    }

    @Override
    public void run(String... args) throws Exception {
       // SubsidiaryService sub1 = SubsidiaryService.createSubsidiary("talyan");
    }
}

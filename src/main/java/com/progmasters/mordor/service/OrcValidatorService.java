package com.progmasters.mordor.service;

import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.dto.OrcDetails;
import com.progmasters.mordor.repository.OrcRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class OrcValidatorService {
    private static final long MAX_KILL_COUNT = 500;
    private final OrcRepository orcRepository;

    public OrcValidatorService(OrcRepository orcRepository) {
        this.orcRepository = orcRepository;
    }

    public void validateCommonAttributes(OrcDetails orc, Errors errors) {
        if (orc.getWeapons() != null && orc.getWeapons().size() > 3) {
            errors.rejectValue("weapons", "weapons.tooMany");
        }
        if (orc.getKillCount() != null) {
            if (orc.getKillCount() > MAX_KILL_COUNT) {
                errors.rejectValue("killCount", "killCount.tooMany");
            } else if (orc.getKillCount() < 0) {
                errors.rejectValue("killCount", "killCount.negative");
            }
        }
        if (orc.getRaceType() == null || orc.getRaceType().trim().equals("")) {
            errors.rejectValue("raceType", "raceType.mustChoose");
        }
        if (orc.getName() != null && orc.getName().isBlank()) {
            errors.rejectValue("name", "name.mustGive");
        }
    }

    public boolean nameIsTaken(String name) {
        return !orcRepository.findAllByName(name).isEmpty();
    }

    public void checkNameUniquenessOnUpdate(OrcDetails orc, Errors errors) {
        Orc orcToBeUpdated = orcRepository.findById(orc.getId()).orElse(null);
        if (orcToBeUpdated != null && !orcToBeUpdated.getName().equalsIgnoreCase(orc.getName()) && nameIsTaken(orc.getName())) {
            errors.rejectValue("name", "name.mustBeUnique");
        }
    }
}

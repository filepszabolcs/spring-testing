package com.progmasters.mordor.service;

import com.progmasters.mordor.repository.OrcRepository;
import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.OrcRaceType;
import com.progmasters.mordor.domain.WeaponType;
import com.progmasters.mordor.dto.OrcListItem;
import com.progmasters.mordor.dto.OrcDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrcService {

    private OrcRepository orcRepository;

    @Autowired
    public OrcService(OrcRepository orcRepository) {
        this.orcRepository = orcRepository;
    }

    public Orc saveOrc(OrcDetails orcDetails) {
        Orc orc = new Orc();
        updateValues(orcDetails, orc);

        return orcRepository.save(orc);
    }

    public Orc updateOrc(OrcDetails orcDetails, Long id) {
        Orc orc = orcRepository.findOne(id);
        if (orc != null) {
            updateValues(orcDetails, orc);
        }

        return orc;
    }

    private void updateValues(OrcDetails orcDetails, Orc orc) {
        orc.setName(orcDetails.getName());
        orc.setOrcRaceType(OrcRaceType.valueOf(orcDetails.getRaceType()));
        orc.setKillCount(orcDetails.getKillCount());
        orc.setWeapons(orcDetails.getWeapons().stream()
                .map(WeaponType::valueOf).collect(Collectors.toList()));
    }

    public OrcDetails getOrcDetails(Long id) {
        Orc orc = orcRepository.findOne(id);
        return new OrcDetails(orc);
    }

    public List<OrcListItem> listOrcs() {
        return orcRepository.findByOrderByKillCountDesc().stream()
                .map(OrcListItem::new).collect(Collectors.toList());
    }

    /**
     * Return true if deletion is successful
     */
    public boolean deleteOrc(Long id) {
        Orc orc = orcRepository.findOne(id);

        boolean result = false;
        if (orc != null) {
            orcRepository.delete(id);
            result = true;
        }

        return result;
    }

}

/*
 * Copyright © Progmasters (QTC Kft.), 2018.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package com.progmasters.mordor.service;

import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.OrcRaceType;
import com.progmasters.mordor.domain.WeaponType;
import com.progmasters.mordor.dto.OrcDetails;
import com.progmasters.mordor.dto.OrcListItem;
import com.progmasters.mordor.repository.OrcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrcService {

    private static final Logger logger = LoggerFactory.getLogger(OrcService.class);

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
        Optional<Orc> orcOptional = orcRepository.findById(id);
        if (orcOptional.isPresent()) {
            Orc orc = orcOptional.get();
            updateValues(orcDetails, orc);
            orcRepository.save(orc);
            return orc;
        } else {
            return null;
        }
    }

    private void updateValues(OrcDetails orcDetails, Orc orc) {
        orc.setName(orcDetails.getName());
        orc.setOrcRaceType(OrcRaceType.valueOf(orcDetails.getRaceType()));
        orc.setKillCount(orcDetails.getKillCount() == null ? 0 : orcDetails.getKillCount());
        orc.setWeapons(orcDetails.getWeapons().stream()
                .map(WeaponType::valueOf).collect(Collectors.toList()));
    }

    public OrcDetails getOrcDetails(Long id) {
        OrcDetails orcDetails = null;
        Optional<Orc> orcOptional = orcRepository.findById(id);
        if (orcOptional.isPresent()) {
            orcDetails = new OrcDetails(orcOptional.get());
        }
        return orcDetails;
    }

    public List<OrcListItem> listOrcs() {
        return orcRepository.findByOrderByKillCountDesc().stream()
                .map(OrcListItem::new).collect(Collectors.toList());
    }

    /**
     * Return true if deletion is successful
     */
    public boolean deleteOrc(Long id) {
        boolean result = false;
        Optional<Orc> orcOptional = orcRepository.findById(id);
        if (orcOptional.isPresent()) {
            Orc orc = orcOptional.get();
            orcRepository.delete(orc);
            result = true;
        }

        return result;
    }

}

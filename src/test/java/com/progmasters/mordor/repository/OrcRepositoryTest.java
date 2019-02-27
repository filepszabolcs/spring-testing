package com.progmasters.mordor.repository;

import com.progmasters.mordor.config.SpringWebConfig;
import com.progmasters.mordor.config.TestConfiguration;
import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.OrcRaceType;
import com.progmasters.mordor.domain.WeaponType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebConfig.class, TestConfiguration.class})
public class OrcRepositoryTest {

    @Autowired
    private OrcRepository orcRepository;

    @Test
    public void testSaveAndFindAll() {
        // given
        Orc orc = new Orc();
        orc.setName("Varag");
        orc.setOrcRaceType(OrcRaceType.MOUNTAIN);
        orc.setWeapons(Arrays.asList(WeaponType.KNIFE, WeaponType.SHIELD));
        orc.setKillCount(1000L);
        orcRepository.save(orc);

        // when
        List<Orc> orcs = orcRepository.findAll();

        // then
        assertEquals(1, orcs.size());
    }

}

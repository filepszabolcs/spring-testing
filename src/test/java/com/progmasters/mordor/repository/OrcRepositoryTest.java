package com.progmasters.mordor.repository;

import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.OrcRaceType;
import com.progmasters.mordor.domain.WeaponType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest

/*
Official Docs:
Annotation for a JPA test that focuses only on JPA components.
Using this annotation will disable full auto-configuration and instead apply only configuration relevant to JPA tests.
By default, tests annotated with @DataJpaTest are transactional and roll back at the end of each test.
They also use an embedded in-memory database (replacing any explicit or usually auto-configured DataSource).
The @AutoConfigureTestDatabase annotation can be used to override these settings.
If you are looking to load your full application configuration, but use an embedded database,
you should consider @SpringBootTest combined with @AutoConfigureTestDatabase rather than this annotation.
*/

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

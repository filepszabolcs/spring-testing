package com.progmasters.mordor.integration;

import com.progmasters.mordor.dto.OrcDetails;
import com.progmasters.mordor.dto.OrcListItem;
import com.progmasters.mordor.repository.OrcRepository;
import com.progmasters.mordor.service.OrcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrcServiceIT {

    @Autowired
    OrcRepository orcRepository;
    private OrcService orcService;

    @BeforeEach
    public void init() {
        this.orcService = new OrcService(orcRepository);
    }

    @Test
    public void testListOrcs() {
        //given
        OrcDetails firstOrcDetails = new OrcDetails();
        firstOrcDetails.setName("tibork");
        firstOrcDetails.setKillCount(50L);
        firstOrcDetails.setRaceType("MOUNTAIN");
        firstOrcDetails.setWeapons(List.of("KNIFE", "BOW"));

        OrcDetails secondOrcDetails = new OrcDetails();
        secondOrcDetails.setName("orkaresz");
        secondOrcDetails.setKillCount(10L);
        secondOrcDetails.setRaceType("URUK");
        secondOrcDetails.setWeapons(List.of("SWORD"));

        orcService.saveOrc(firstOrcDetails);
        orcService.saveOrc(secondOrcDetails);

        //when
        List<OrcListItem> orcList = orcService.listOrcs();

        //then
        assertEquals(2, orcList.size());

        assertEquals("tibork", orcList.get(0).getName());
        assertEquals(50L, orcList.get(0).getKillCount());
        assertEquals("Mountain", orcList.get(0).getOrcRaceType());
        assertEquals(2, orcList.get(0).getWeapons().size());
        assertTrue(orcList.get(0).getWeapons().contains("Knife"));

        assertEquals("orkaresz", orcList.get(1).getName());
        assertEquals(10L, orcList.get(1).getKillCount());
        assertEquals("Uruk", orcList.get(1).getOrcRaceType());
        assertEquals(1, orcList.get(1).getWeapons().size());
        assertTrue(orcList.get(1).getWeapons().contains("Sword"));
    }

}

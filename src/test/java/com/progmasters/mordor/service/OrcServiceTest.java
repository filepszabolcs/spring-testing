package com.progmasters.mordor.service;

import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.OrcRaceType;
import com.progmasters.mordor.domain.WeaponType;
import com.progmasters.mordor.dto.OrcDetails;
import com.progmasters.mordor.repository.OrcRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class OrcServiceTest {

    private OrcService orcService;

    private OrcRepository orcRepositoryMock;

    @Before
    public void setUp() {
        orcRepositoryMock = mock(OrcRepository.class);
        orcService = new OrcService(orcRepositoryMock);
    }

    @Test
    public void testSavingOrc() {
        Orc orc = new Orc();
        orc.setName("Varag");
        orc.setOrcRaceType(OrcRaceType.MOUNTAIN);
        orc.setWeapons(Arrays.asList(WeaponType.KNIFE, WeaponType.SHIELD));
        orc.setKillCount(100L);

        OrcDetails orcDetails = new OrcDetails(orc);

        when(orcRepositoryMock.save(any(Orc.class))).thenAnswer(returnsFirstArg());

        Orc savedOrc = orcService.saveOrc(orcDetails);

        assertEquals(orc.getName(), savedOrc.getName());
        assertEquals(orc.getOrcRaceType(), savedOrc.getOrcRaceType());
        assertEquals(orc.getWeapons(), savedOrc.getWeapons());
        assertEquals(orc.getKillCount(), savedOrc.getKillCount());

        verify(orcRepositoryMock, times(1)).save(any(Orc.class));
        verifyNoMoreInteractions(orcRepositoryMock);
    }
}

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
import com.progmasters.mordor.repository.OrcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class OrcServiceTest {

    private OrcService orcService;

    private OrcRepository orcRepositoryMock;

    @BeforeEach
    public void setUp() {
        orcRepositoryMock = mock(OrcRepository.class);
        orcService = new OrcService(orcRepositoryMock);
    }

    @Test
    public void testSavingOrc() {
        // given
        Orc orc = new Orc();
        orc.setName("Varag");
        orc.setOrcRaceType(OrcRaceType.MOUNTAIN);
        orc.setWeapons(Arrays.asList(WeaponType.KNIFE, WeaponType.SHIELD));
        orc.setKillCount(100L);

        OrcDetails orcDetails = new OrcDetails(orc);

        // when
        when(orcRepositoryMock.save(any(Orc.class))).thenAnswer(returnsFirstArg());
        //when(orcRepositoryMock.findOne(any(Long.class))).thenReturn(new Orc());

        Orc savedOrc = orcService.saveOrc(orcDetails);

        // then
        assertEquals(orc.getName(), savedOrc.getName());
        assertEquals(orc.getOrcRaceType(), savedOrc.getOrcRaceType());
        assertEquals(orc.getWeapons(), savedOrc.getWeapons());
        assertEquals(orc.getKillCount(), savedOrc.getKillCount());

        verify(orcRepositoryMock, times(1)).save(any(Orc.class));
        verifyNoMoreInteractions(orcRepositoryMock);
    }
}

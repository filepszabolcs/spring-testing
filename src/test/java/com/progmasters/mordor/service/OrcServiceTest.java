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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrcServiceTest {

    private OrcService orcService;

    @Mock
    private OrcRepository orcRepositoryMock;

    @BeforeEach
    public void setUp() {
        orcService = new OrcService(orcRepositoryMock);
    }

    @Test
    public void testUpdateOrc() {
        // given
        Orc originalOrc = new Orc("Varag", OrcRaceType.MOUNTAIN, 100L, List.of(WeaponType.KNIFE, WeaponType.BOW));

        OrcDetails orcDetails = new OrcDetails();
        orcDetails.setName("Updated varag");
        orcDetails.setRaceType("MOUNTAIN");
        orcDetails.setKillCount(null);
        orcDetails.setWeapons(List.of("SWORD"));

        when(orcRepositoryMock.findById(1L)).thenReturn(Optional.of(originalOrc));
        when(orcRepositoryMock.save(any(Orc.class))).thenAnswer(returnsFirstArg());

        //when
        Orc updatedOrc = orcService.updateOrc(orcDetails, 1L);

        // then
        assertEquals("Updated varag", updatedOrc.getName());
        assertEquals(OrcRaceType.MOUNTAIN, updatedOrc.getOrcRaceType());
        assertEquals(List.of(WeaponType.SWORD), updatedOrc.getWeapons());
        assertEquals(0, updatedOrc.getKillCount());

        verify(orcRepositoryMock, times(1)).findById(1L);
        verify(orcRepositoryMock, times(1)).save(any(Orc.class));
        verifyNoMoreInteractions(orcRepositoryMock);
    }

    @Test
    public void testUpdateOrc_orcDoesntExist() {
        //given
        OrcDetails orcDetails = new OrcDetails();
        orcDetails.setName("random ork");
        orcDetails.setRaceType("MOUNTAIN");
        orcDetails.setKillCount(20L);
        orcDetails.setWeapons(List.of("SWORD", "BOW"));

        when(orcRepositoryMock.findById(2L)).thenReturn(Optional.empty());

        //when
        Orc updatedOrc = orcService.updateOrc(orcDetails, 2L);

        //then
        assertNull(updatedOrc);

        verify(orcRepositoryMock, times(1)).findById(2L);
        verifyNoMoreInteractions(orcRepositoryMock);
    }
}

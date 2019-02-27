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

package com.progmasters.mordor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.OrcRaceType;
import com.progmasters.mordor.domain.WeaponType;
import com.progmasters.mordor.dto.OrcDetails;
import com.progmasters.mordor.dto.OrcListItem;
import com.progmasters.mordor.exception.GlobalExceptionHandler;
import com.progmasters.mordor.service.OrcService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StandaloneOrcControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrcService orcServiceMock;

    @InjectMocks
    private OrcController orcController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orcController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource()))
                .build();
    }

    @AfterEach
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void testGetOrcList() throws Exception {
        // given
        Orc orc1 = new Orc();
        orc1.setName("Varag");
        orc1.setOrcRaceType(OrcRaceType.MOUNTAIN);
        orc1.setKillCount(100L);

        Orc orc2 = new Orc();
        orc2.setName("Urgarok");
        orc2.setOrcRaceType(OrcRaceType.URUK);

        List<OrcListItem> orcs = Arrays.asList(orc1, orc2).stream()
                .map(OrcListItem::new).collect(Collectors.toList());

        // when
        when(orcServiceMock.listOrcs()).thenReturn(orcs);

        // then
        this.mockMvc.perform(get("/api/orcs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Varag")))
                .andExpect(jsonPath("$[0].orcRaceType", is("Mountain")))
                .andExpect(jsonPath("$[0].killCount", is(100)))
                .andExpect(jsonPath("$[1].name", is("Urgarok")))
                .andExpect(jsonPath("$[1].orcRaceType", is("Uruk")));

        verify(orcServiceMock, times(1)).listOrcs();
        verifyNoMoreInteractions(orcServiceMock);
    }

    @Test
    public void testPostOrcWithValidationError() throws Exception {
        Orc orc = new Orc();
        orc.setName("Varag");
        orc.setOrcRaceType(OrcRaceType.MOUNTAIN);
        orc.setWeapons(Arrays.asList(WeaponType.KNIFE, WeaponType.SHIELD));
        orc.setKillCount(10000000L);

        OrcDetails orcDetails = new OrcDetails(orc);

        this.mockMvc.perform(post("/api/orcs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(orcDetails)))
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is("killCount")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("Orcs must retire after killing half a million enemies")));

        verifyZeroInteractions(orcServiceMock);
    }

    private MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasename("messages");
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

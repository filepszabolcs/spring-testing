package com.progmasters.mordor.controller;


import com.progmasters.mordor.config.SpringWebConfig;
import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.OrcRaceType;
import com.progmasters.mordor.dto.OrcListItem;
import com.progmasters.mordor.service.OrcService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebConfig.class, OrcControllerTest.TestConfiguration.class})
public class OrcControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    OrcService orcServiceMock;

    @Before
    public void setup() {
//        MockitoAnnotations.initMocks(this);
        Mockito.reset(orcServiceMock);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @After
    public void validate() {
//        validateMockitoUsage();
    }

    @Test
    public void testGetOrcList() throws Exception {
        Orc orc1 = new Orc();
        orc1.setName("Varag");
        orc1.setOrcRaceType(OrcRaceType.MOUNTAIN);

        Orc orc2 = new Orc();
        orc2.setName("Urgarok");
        orc2.setOrcRaceType(OrcRaceType.URUK);

        List<OrcListItem> orcs = Arrays.asList(orc1, orc2).stream()
                .map(OrcListItem::new).collect(Collectors.toList());

        when(orcServiceMock.listOrcs()).thenReturn(orcs);

        this.mockMvc.perform(get("/api/orcs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Varag")))
                .andExpect(jsonPath("$[0].orcRaceType", is("Mountain")))
                .andExpect(jsonPath("$[1].name", is("Urgarok")))
                .andExpect(jsonPath("$[1].orcRaceType", is("Uruk")));

        verify(orcServiceMock).listOrcs();
//        verifyNoMoreInteractions(orcServiceMock);
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        public OrcService orcService() {
            return Mockito.mock(OrcService.class);
        }
    }

}

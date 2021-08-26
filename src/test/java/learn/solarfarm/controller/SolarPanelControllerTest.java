package learn.solarfarm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.data.SolarPanelJdbcTemplateRepository;
import learn.solarfarm.models.Material;
import learn.solarfarm.models.SolarPanel;
import learn.solarfarm.models.SolarPanelKey;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SolarPanelControllerTest {

    @MockBean
    SolarPanelJdbcTemplateRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    void shouldFindBySection() throws Exception {
        List<SolarPanel> expectedList = List.of(
                new SolarPanel(1, "Section One", 1, 1, 2020, Material.POLY_SI, true),
                new SolarPanel(2, "Section One", 1, 2, 2020, Material.POLY_SI, true)
        );

        Mockito
                .when(repository.findBySection("Section One"))
                .thenReturn(expectedList);

        String expectedResult = mapToJson(expectedList);

        mvc.perform(MockMvcRequestBuilders.get("/solar-panel/Section One"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));

    }

    @Test
    void shouldFindByKey() throws Exception {
        SolarPanelKey inputKey = new SolarPanelKey("test", 1, 1);
        SolarPanel panel = new SolarPanel(1, "test", 1, 1, 2000, Material.MONO_SI, true);

        Mockito
                .when(repository.findByKey(inputKey))
                .thenReturn(panel);

        String expectedString = mapToJson(panel);

        mvc.perform(MockMvcRequestBuilders.get("/solar-panel/test/1/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedString));
    }

    @Test
    public void shouldCreate() throws Exception {
        SolarPanel testInput = new SolarPanel(0, "test", 1, 1, 2000, Material.MONO_SI, true);
        SolarPanel testOutput = new SolarPanel(1, "test", 1, 1, 2000, Material.MONO_SI, true);

        String testJsonInput = mapToJson(testInput);
        String expectedJson = mapToJson(testOutput);

        Mockito
                .when(repository.create(Mockito.any()))
                .thenReturn(testOutput);

        var request = MockMvcRequestBuilders.post("/solar-panel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testJsonInput);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    public void shouldNotCreateWhenIdIsPresent() throws Exception {
        SolarPanel testInput = new SolarPanel(1, "test", 1, 1, 2000, Material.MONO_SI, true);

        String testJsonInput = mapToJson(testInput);

        var request = MockMvcRequestBuilders.post("/solar-panel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testJsonInput);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private String mapToJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }
}
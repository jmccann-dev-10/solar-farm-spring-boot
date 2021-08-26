package learn.solarfarm.domain;

import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.data.SolarPanelJdbcTemplateRepository;
import learn.solarfarm.models.Material;
import learn.solarfarm.models.SolarPanel;
import learn.solarfarm.models.SolarPanelKey;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SolarPanelServiceMockitoTest {

    @MockBean
    SolarPanelJdbcTemplateRepository repository;

    @Autowired
    SolarPanelService service;

    /*
       new SolarPanel(1, "Section One", 1, 1, 2020, Material.POLY_SI, true)
       new SolarPanel(2, "Section One", 1, 2, 2020, Material.POLY_SI, true)
       new SolarPanel(3, "Section Two", 10, 11, 2000, Material.A_SI, false)
       */

    @Test
    void shouldFindTwoSolarPanelsForSectionOne() throws DataAccessException {
        Mockito
                .when(repository.findBySection("Section One"))
                .thenReturn(List.of(
                        new SolarPanel(1, "Section One", 1, 1, 2020, Material.POLY_SI, true),
                        new SolarPanel(2, "Section One", 1, 2, 2020, Material.POLY_SI, true)
                ));

        List<SolarPanel> solarPanels = service.findBySection("Section One");
        assertEquals(2, solarPanels.size());
    }

    @Test
    void shouldFindSolarPanelInSectionTwoRow10Column11() throws DataAccessException {
        SolarPanelKey testKey = new SolarPanelKey("Section Two", 10, 11);
        SolarPanel testSolarPanel = new SolarPanel(3, "Section Two", 10, 11, 2000, Material.A_SI, false);
        Mockito
                .when(repository.findByKey(testKey))
                .thenReturn(testSolarPanel);

        SolarPanel solarPanel = service.findByKey(testKey);
        assertNotNull(solarPanel);
    }

    @Test
    void shouldNotCreateNull() throws DataAccessException {
        // Arrange
        SolarPanel solarPanel = null;

        // Act
        SolarPanelResult result = service.create(solarPanel);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrorMessages().size());
        assertTrue(result.getErrorMessages().get(0).contains("cannot be null"));
    }

    @Test
    void shouldCreate() throws DataAccessException {
        // Arrange
        SolarPanel testIn = new SolarPanel(0, "Test Section", 12, 13, 2000, Material.CIGS, true);
        SolarPanel testOut = new SolarPanel(1, "Test Section", 12, 13, 2000, Material.CIGS, true);
        Mockito
                .when(repository.create(testIn))
                .thenReturn(testOut);

        // Act
        SolarPanelResult result = service.create(testIn);

        // Assert
        assertTrue(result.isSuccess());
    }
}

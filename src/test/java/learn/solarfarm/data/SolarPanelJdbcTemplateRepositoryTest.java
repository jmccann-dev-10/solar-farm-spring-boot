package learn.solarfarm.data;

import learn.solarfarm.models.Material;
import learn.solarfarm.models.SolarPanel;
import learn.solarfarm.models.SolarPanelKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SolarPanelJdbcTemplateRepositoryTest {

    @Autowired
    SolarPanelJdbcTemplateRepository repository;

    @Autowired
    JdbcTemplate template;

    private boolean hasRunPreviously = false;

    private final String PANEL = "The Ridge";


    @BeforeEach
    public void init() {
        if (!hasRunPreviously) {
            template.update("call set_known_good_state()");
            hasRunPreviously = true;
        }
    }

    @Test
    public void shouldFindByKnownSection() throws DataAccessException {
        List<SolarPanel> panels = repository.findBySection(PANEL);
        assertNotNull(panels);

        // verify we found something
        assertNotEquals(0, panels.size());

        // filter the list based on the search
        List<SolarPanel> filteredList = panels.stream().filter(panel -> panel.getSection().equals(PANEL)).collect(Collectors.toList());

        // verify the filtered list contains the same items in the original list
        assertEquals(panels, filteredList);
    }

    @Test
    public void shouldFindByKey() throws DataAccessException {
        SolarPanel expected = new SolarPanel(1, "The Ridge", 1, 1,
                2020, Material.POLY_SI, true);

        SolarPanel actual = repository.findByKey(new SolarPanelKey("The Ridge", 1, 1));

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindByKeyMissing() throws DataAccessException {
        SolarPanel actual = repository.findByKey(new SolarPanelKey("Missing", 1, 1));
        assertNull(actual);
    }

    @Test
    void shouldCreate() throws DataAccessException {
        SolarPanel solarPanel = new SolarPanel(0, "The Ridge", 10, 10,
                2020, Material.POLY_SI, true);

        SolarPanel actual = repository.create(solarPanel);

        System.out.println(actual);
        assertNotNull(actual);
        assertTrue(actual.getId() > 0);
    }

    @Test
    void shouldUpdateExisting() throws DataAccessException {
        SolarPanel solarPanel = new SolarPanel(3, "New Flats", 20, 21,
                2000, Material.A_SI, false);

        assertTrue(repository.update(solarPanel));
        assertEquals(solarPanel, repository.findByKey(solarPanel.getKey()));
    }

    @Test
    void shouldNotUpdateMissing() throws DataAccessException {
        SolarPanel solarPanel = new SolarPanel(-1, "New Ridge", 20, 21,
                2000, Material.A_SI, false);

        assertFalse(repository.update(solarPanel));
    }

    @Test
    void shouldDeleteExisting() throws DataAccessException {
        assertTrue(repository.deleteByKey(new SolarPanelKey("Flats", 3, 7)));
    }

    @Test
    void shouldNotDeleteMissing() throws DataAccessException {
        assertFalse(repository.deleteByKey(new SolarPanelKey("Missing", 1, 1)));
    }

}
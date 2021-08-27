package learn.solarfarm.controller;

import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.domain.SolarPanelResult;
import learn.solarfarm.domain.SolarPanelService;
import learn.solarfarm.models.SolarPanel;
import learn.solarfarm.models.SolarPanelKey;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("rest")
@RestController
@RequestMapping("solar-panel")
public class SolarPanelController {

    private SolarPanelService service;

    public SolarPanelController(SolarPanelService service) {
        this.service = service;
    }

    @GetMapping("/{section}")
    public List<SolarPanel> getAllBySection(@PathVariable String section) throws DataAccessException {
        return service.findBySection(section);
    }

    @GetMapping("/{section}/{row}/{column}")
    public ResponseEntity<SolarPanel> getPanelByKey(@PathVariable String section,
                                    @PathVariable int row,
                                    @PathVariable int column) throws DataAccessException {
        SolarPanelKey key = new SolarPanelKey(section, row, column);
        SolarPanel panel = service.findByKey(key);
        if (panel == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(panel, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<SolarPanel> saveNewPanel(@RequestBody SolarPanel panel) throws DataAccessException {
        SolarPanelResult result = service.create(panel);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getSolarPanel(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

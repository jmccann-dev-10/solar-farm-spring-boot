package learn.solarfarm.ui;

import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.domain.SolarPanelResult;
import learn.solarfarm.domain.SolarPanelService;
import learn.solarfarm.models.SolarPanel;
import learn.solarfarm.models.SolarPanelKey;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Profile("console")
public class Controller {
    private final View view;
    private final SolarPanelService service;

    public Controller(View view, SolarPanelService service) {
        this.view = view;
        this.service = service;
    }

    // Add PostConstruct to this method so Spring knows to run this method once this bean is finished being created.
    @PostConstruct
    public void run() {
        view.displayHeader("Welcome to Solar Farm");
        try {
            runApp();
        } catch (DataAccessException ex) {
            view.displayErrors(List.of(ex.getMessage()));
        }
        view.displayMessage("Goodbye!");
    }

    private void runApp() throws DataAccessException {
        for (int option = view.chooseMenuOption();
             option > 0;
             option = view.chooseMenuOption()) {

            switch (option) {
                case 1:
                    findSolarPanelsBySection();
                    break;
                case 2:
                    addSolarPanel();
                    break;
                case 3:
                    updateSolarPanel();
                    break;
                case 4:
                    removeSolarPanel();
                    break;
            }
        }
    }

    private void findSolarPanelsBySection() throws DataAccessException {
        view.displayHeader("Find Panels by Section");
        String section = view.getSection();
        List<SolarPanel> solarPanels = service.findBySection(section);
        if (solarPanels.isEmpty()) {
            view.displayMessage("There are no panels in this section.");
        } else {
            view.displaySolarPanels(section, solarPanels);
        }
    }

    private void addSolarPanel() throws DataAccessException {
        SolarPanel solarPanel = view.addSolarPanel();
        SolarPanelResult result = service.create(solarPanel);
        if (result.isSuccess()) {
            view.displayMessage("[Success]%nPanel %s added.", result.getSolarPanel().getKey());
        } else {
            view.displayErrors(result.getErrorMessages());
        }
    }

    private void updateSolarPanel() throws DataAccessException {
        view.displayHeader("Update a Panel");
        SolarPanelKey key = view.getKey();
        SolarPanel solarPanel = service.findByKey(key);
        if (solarPanel != null) {
            SolarPanel updatedSolarPanel = view.updateSolarPanel(solarPanel);
            updatedSolarPanel.setId(solarPanel.getId());
            SolarPanelResult result = service.update(updatedSolarPanel);
            if (result.isSuccess()) {
                view.displayMessage("[Success]%nPanel %s updated.", result.getSolarPanel().getKey());
            } else {
                view.displayErrors(result.getErrorMessages());
            }
        } else {
            view.displayErrors(List.of(String.format("There is no panel %s.", key)));
        }
    }

    private void removeSolarPanel() throws DataAccessException {
        view.displayHeader("Remove a Panel");
        SolarPanelKey key = view.getKey();
        SolarPanel solarPanel = service.findByKey(key);
        if (solarPanel != null) {
            SolarPanelResult result = service.deleteByKey(key);
            if (result.isSuccess()) {
                view.displayMessage("[Success]%nPanel %s removed.", key);
            } else {
                view.displayErrors(result.getErrorMessages());
            }
        } else {
            view.displayErrors(List.of(String.format("There is no panel %s.", key)));
        }
    }
}

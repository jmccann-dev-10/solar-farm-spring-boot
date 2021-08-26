package learn.solarfarm.ui;

import learn.solarfarm.domain.SolarPanelService;
import learn.solarfarm.models.Material;
import learn.solarfarm.models.SolarPanel;
import learn.solarfarm.models.SolarPanelKey;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("file-repository")
public class View {
    private final TextIO io;

    public View(TextIO io) {
        this.io = io;
    }

    public int chooseMenuOption() {
        displayHeader("Main Menu");
        io.println("0. Exit");
        io.println("1. Find Panels by Section");
        io.println("2. Add a Panel");
        io.println("3. Update a Panel");
        io.println("4. Remove a Panel");
        return io.readInt("Choose [0-4]", 0, 4);
    }

    public String getSection() {
        io.println("");
        return io.readRequiredString("Section Name");
    }

    public void displaySolarPanels(String section, List<SolarPanel> solarPanels) {
        io.println("");
        io.printf("Panels in %s%n", section);
        io.println("Row Col Year Material Tracking");
        for (SolarPanel sp : solarPanels) {
            io.printf("%3s %3s %4s %8s %8s%n", sp.getRow(), sp.getColumn(), sp.getYearInstalled(),
                    sp.getMaterial().getAbbreviation(), sp.isTracking() ? "yes" : "no");
        }
    }

    public SolarPanelKey getKey() {
        io.println("");
        String section = io.readRequiredString("Section Name");
        int row = io.readInt("Row", 1, SolarPanelService.MAX_ROW_COLUMN);
        int column = io.readInt("Column", 1, SolarPanelService.MAX_ROW_COLUMN);
        return new SolarPanelKey(section, row, column);
    }

    public void displayHeader(String message) {
        int length = message.length();
        io.println("");
        io.println(message);
        io.println("=".repeat(length));
    }

    public void displayErrors(List<String> errors) {
        displayMessage("[Errors]");
        for (String error : errors) {
            io.println(error);
        }
    }

    public void displayMessage(String message) {
        io.println("");
        io.println(message);
    }

    public void displayMessage(String format, Object... args) {
        displayMessage(String.format(format, args));
    }

    public SolarPanel addSolarPanel() {
        displayHeader("Add a Panel");
        io.println("");

        SolarPanel result = new SolarPanel();
        result.setSection(io.readRequiredString("Section"));
        result.setRow(io.readInt("Row", 1, SolarPanelService.MAX_ROW_COLUMN));
        result.setColumn(io.readInt("Column", 1, SolarPanelService.MAX_ROW_COLUMN));
        result.setMaterial(io.selectEnum("Material", Material.class));
        result.setYearInstalled(io.readInt("Installation Year", SolarPanelService.getMaxInstallationYear()));
        result.setTracking(io.readBoolean("Tracked [y/n]"));

        return result;
    }

    public SolarPanel updateSolarPanel(SolarPanel solarPanel) {
        io.println("");
        io.printf("Editing %s%n", solarPanel.getKey());
        io.println("Press [Enter] to keep original value.");
        io.println("");

        SolarPanel result = new SolarPanel();
        result.setSection(io.readStringWithFallback("Section", solarPanel.getSection()));
        result.setRow(io.readIntWithFallback("Row", 1, SolarPanelService.MAX_ROW_COLUMN, solarPanel.getRow()));
        result.setColumn(io.readIntWithFallback("Column", 1, SolarPanelService.MAX_ROW_COLUMN, solarPanel.getColumn()));
        result.setMaterial(io.selectEnumWithFallback("Material", Material.class, solarPanel.getMaterial()));
        result.setYearInstalled(io.readIntWithFallback("Installation Year", SolarPanelService.getMaxInstallationYear(), solarPanel.getYearInstalled()));
        result.setTracking(io.readBooleanWithFallback("Tracked [y/n]", solarPanel.isTracking()));

        return result;
    }
}

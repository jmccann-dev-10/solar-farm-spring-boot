package learn.solarfarm.models;

import java.util.Objects;

public class SolarPanel {

    private int id;
    private String section;
    private int row;
    private int column;
    private int yearInstalled;
    private Material material;
    private boolean isTracking;
    private SolarPanelKey key;

    public SolarPanel() {
    }

    public SolarPanel(int id, String section, int row, int column, int yearInstalled, Material material, boolean isTracking) {
        this.id = id;
        this.section = section;
        this.row = row;
        this.column = column;
        this.yearInstalled = yearInstalled;
        this.material = material;
        this.isTracking = isTracking;
        this.key = new SolarPanelKey(section, row, column);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
        // Keep the key up-to-date.
        this.key = new SolarPanelKey(section, this.row, this.column);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
        // Keep the key up-to-date.
        this.key = new SolarPanelKey(this.section, row, this.column);
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
        // Keep the key up-to-date.
        this.key = new SolarPanelKey(this.section, this.row, column);
    }

    public int getYearInstalled() {
        return yearInstalled;
    }

    public void setYearInstalled(int yearInstalled) {
        this.yearInstalled = yearInstalled;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    public SolarPanelKey getKey() {
        return key;
    }

    public boolean isMatch(SolarPanelKey key) {
        return this.key.equals(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolarPanel that = (SolarPanel) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "SolarPanel{" +
                "id=" + id +
                ", section='" + section + '\'' +
                ", row=" + row +
                ", column=" + column +
                ", yearInstalled=" + yearInstalled +
                ", material=" + material +
                ", isTracking=" + isTracking +
                ", key=" + key +
                '}';
    }
}

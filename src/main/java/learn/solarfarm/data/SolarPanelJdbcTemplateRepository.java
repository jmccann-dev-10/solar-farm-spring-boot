package learn.solarfarm.data;

import learn.solarfarm.models.Material;
import learn.solarfarm.models.SolarPanel;
import learn.solarfarm.models.SolarPanelKey;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Profile("jdbc-template")
@Repository
public class SolarPanelJdbcTemplateRepository implements SolarPanelRepository {

    private static final String SOLAR_PANEL_NATURAL_COLUMNS = "section,`row`,`column`,year_installed,material_id,is_tracking";
    private static final String SOLAR_PANEL_ALL_COLUMNS = "solar_panel_id," + SOLAR_PANEL_NATURAL_COLUMNS;

    private final JdbcTemplate template;
    private final RowMapper<SolarPanel> mapper = (resultSet, rowNumber) -> {
        SolarPanel panel = new SolarPanel();
        panel.setId(resultSet.getInt("solar_panel_id"));
        panel.setSection(resultSet.getString("section"));
        panel.setRow(resultSet.getInt("row"));
        panel.setColumn(resultSet.getInt("column"));
        panel.setYearInstalled(resultSet.getInt("year_installed"));
        panel.setTracking(resultSet.getBoolean("is_tracking"));
        int materialId = resultSet.getInt("material_id");
        panel.setMaterial(Material.findByValue(materialId));
        return panel;
    };

    public SolarPanelJdbcTemplateRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<SolarPanel> findBySection(String section) throws DataAccessException {
        final String sql = String.format("select %s from solar_panel where section = ?;", SOLAR_PANEL_ALL_COLUMNS);

        return template.query(sql, mapper, section);
    }

    @Override
    public SolarPanel findByKey(SolarPanelKey key) throws DataAccessException {
        final String sql = String.format("select %s from solar_panel where " +
                "section = ? and " +
                "`row` = ? and " +
                "`column` = ?;", SOLAR_PANEL_ALL_COLUMNS);
        try {
            return template.queryForObject(sql, mapper, key.getSection(), key.getRow(), key.getColumn());
        } catch (EmptyResultDataAccessException ex) {
            System.out.printf("No object found for %s, %d, %d%n", key.getSection(), key.getRow(), key.getColumn());
        }
        return null;
    }

    @Override
    public SolarPanel create(SolarPanel solarPanel) throws DataAccessException {
        final String sql = String.format("insert into solar_panel " +
                "(%s) values" +
                "(?, ?, ?, ?, ?, ?);", SOLAR_PANEL_NATURAL_COLUMNS);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, solarPanel.getSection());
            statement.setInt(2, solarPanel.getRow());
            statement.setInt(3, solarPanel.getColumn());
            statement.setInt(4, solarPanel.getYearInstalled());
            statement.setInt(5, solarPanel.getMaterial().getValue());
            statement.setBoolean(6, solarPanel.isTracking());
            return statement;
        }, keyHolder);

        if (rowsAffected == 0) {
            return null;
        }

        solarPanel.setId(keyHolder.getKey().intValue());

        return solarPanel;
    }

    @Override
    public boolean update(SolarPanel solarPanel) throws DataAccessException {
        final String sql = "update solar_panel set " +
                "section = ?, " +
                "`row` = ?, " +
                "`column` = ?, " +
                "year_installed = ?, " +
                "material_id = ?, " +
                "is_tracking = ? " +
                "where solar_panel_id = ?;";

        int rowsUpdated = template.update(sql, solarPanel.getSection(), solarPanel.getRow(),
                solarPanel.getColumn(), solarPanel.getYearInstalled(),
                solarPanel.getMaterial().getValue(), solarPanel.isTracking(),
                solarPanel.getId());

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteByKey(SolarPanelKey key) throws DataAccessException {
        final String sql = "delete from solar_panel where section = ? and `row` = ? and `column` = ?;";
        return template.update(sql, key.getSection(), key.getRow(), key.getColumn()) > 0;
    }
}

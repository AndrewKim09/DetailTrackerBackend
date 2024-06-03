package detailsaver.detailsaver.repositories;

import detailsaver.detailsaver.exceptions.EtBadRequestException;
import detailsaver.detailsaver.exceptions.EtResourceNotFoundException;
import detailsaver.detailsaver.models.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DetailRepositoryImpl implements DetailRepository {
    private static final String SQL_CREATE = "INSERT INTO USER_SAVED_DETAILS (DETAILS_ID, USER_ID, USERNAME, PASSWORD, NOTE, CREATED_DATE) VALUES(NEXTVAL('USER_SAVED_DETAILS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM USER_SAVED_DETAILS WHERE DETAILS_ID = ? AND USER_ID = ?";
    private static final String SQL_UPDATE_BY_ID = "UPDATE USER_SAVED_DETAILS SET USERNAME = ?, PASSWORD = ?, CREATED_DATE = ? WHERE DETAILS_ID = ? AND USER_ID = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM USER_SAVED_DETAILS WHERE DETAILS_ID = ? AND USER_ID = ?";
    private static final String SQL_FETCH_ALL = "SELECT * FROM USER_SAVED_DETAILS WHERE USER_ID = ?";

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public DetailRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Detail> findAll(Integer userId) throws EtResourceNotFoundException {
        // Implementation needed
        List<Detail> userDetails = jdbcTemplate.query(SQL_FETCH_ALL, new Object[]{userId}, detailRowMapper);

       return userDetails;
    }

    @Override
    public Detail findById(Integer userId, Integer detailId) throws EtResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{detailId, userId}, detailRowMapper);
        } catch (Exception e) {
            throw new EtResourceNotFoundException("Could not find detail with id " + detailId);
        }
    }

    @Override
    public Integer create(Integer userId, String username, String password, String note) throws EtResourceNotFoundException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setString(4, note);
                ps.setLong(5, System.currentTimeMillis());
                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("DETAILS_ID");

        } catch (Exception e) {
            throw new EtBadRequestException(e.getMessage());
        }
    }

    @Override
    public void update(Integer userId, Integer detailId, Detail detail) {
        jdbcTemplate.update(SQL_UPDATE_BY_ID, new Object[]{detail.getUsername(), detail.getPassword(), System.currentTimeMillis(), detail.getUserId(), detail.getDetailId()});
    }

    @Override
    public void removeById(Integer userId, Integer detailId) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, new Object[]{detailId, userId});
    }

    private RowMapper<Detail> detailRowMapper = ((rs, rowNum) -> new Detail(
            rs.getInt("DETAILS_ID"),
            rs.getInt("USER_ID"),
            rs.getString("USERNAME"),
            rs.getString("PASSWORD"),
            rs.getString("NOTE"),
            rs.getLong("CREATED_DATE")
    ));
}

package detailsaver.detailsaver.repositories;

import detailsaver.detailsaver.exceptions.EtAuthException;
import detailsaver.detailsaver.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private static final String SQL_CREATE = "INSERT INTO USERS (USER_ID, EMAIL, PASSWORD) VALUES (NEXTVAL('USERS_SEQ'), ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, EMAIL, PASSWORD FROM USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, EMAIL, PASSWORD FROM USERS WHERE EMAIL = ?";


    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer create(String email, String password) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, email);
                ps.setString(2, hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");

        } catch( Exception e){
            throw new EtAuthException(e.getMessage());
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper);
            if(!BCrypt.checkpw(password, user.getPassword())){
                throw new EtAuthException("Wrong password");
            }
            return user;
        } catch (Exception e){
            throw new EtAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper);
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) ->
            new User(rs.getInt("USER_ID"),
            rs.getString("EMAIL"),
            rs.getString("PASSWORD")));
}

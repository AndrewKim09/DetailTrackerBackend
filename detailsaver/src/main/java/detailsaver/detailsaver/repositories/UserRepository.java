package detailsaver.detailsaver.repositories;

import detailsaver.detailsaver.exceptions.EtAuthException;
import detailsaver.detailsaver.models.User;

public interface UserRepository {

    Integer create(String email, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}

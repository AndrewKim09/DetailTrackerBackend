package detailsaver.detailsaver.services;

import detailsaver.detailsaver.exceptions.EtAuthException;
import detailsaver.detailsaver.models.User;

public interface UserService {

    User validateUser(String email, String password) throws EtAuthException;

    User registerUser(String email, String password) throws EtAuthException;
}

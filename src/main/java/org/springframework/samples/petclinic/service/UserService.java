package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.User;
import org.springframework.dao.DataAccessException;

public interface UserService {

    void saveUser(User user) throws Exception;
    User findUserByUsername(String username) throws DataAccessException;
    void deleteUser(User user) throws DataAccessException;
}

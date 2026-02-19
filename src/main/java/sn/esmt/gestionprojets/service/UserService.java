package sn.esmt.gestionprojets.service;

import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User findByEmail(String email);

    User register(User user);

    User update(Long id, User updatedUser);

    User changeRole(Long id, Role newRole);

    User toggleEnabled(Long id);

    void delete(Long id);

    boolean emailExists(String email);

    boolean usernameExists(String username);

}

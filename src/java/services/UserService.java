/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.RoleDB;
import dataaccess.UserDB;
import java.util.ArrayList;
import java.util.List;
import models.Role;
import models.User;

/**
 *
 * @author vitor
 */
public class UserService {
    public List<User> getAll() throws Exception {
        UserDB newUserDB = new UserDB();
        List<User> store_user = newUserDB.getAll();
        return store_user;
    }
    
    public void addUser(String email, String first_name, String last_name, String password, int role) throws Exception {
        User newUser = new User(email, first_name, last_name, password);
        RoleDB roleDB = new RoleDB();
        Role roleName = roleDB.getRoleName(role);
        newUser.setRole(roleName);
        UserDB newUserDB = new UserDB();
        newUserDB.addUser(newUser);
    }
    
    public void updateUser(String email, String first_name, String last_name, String password, int role) throws Exception {
        User updateUser = new User(email, first_name, last_name, password);
        RoleDB roleDB = new RoleDB();
        Role roleName = roleDB.getRoleName(role);
        updateUser.setRole(roleName);
        UserDB updateUserDB = new UserDB();
        updateUserDB.updateUser(updateUser);
    }
    
    public User getUser(String email) throws Exception {
        UserDB getUserDB = new UserDB();
        User getUserRow = getUserDB.getUser(email);
        return getUserRow;
    }
    
    public void deleteUser(String email) throws Exception {
        UserDB deleteUserDB = new UserDB();
        deleteUserDB.deleteUser(email);
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.RoleDB;
import java.util.ArrayList;
import models.Role;

/**
 *
 * @author vitor
 */
public class RoleService {
    public ArrayList<Role> getAll() throws Exception {
        RoleDB newRoleDB = new RoleDB();
        ArrayList<Role> store_role = newRoleDB.getAll();
        return store_role;
    }
    
    public String userRole (int role_number) throws Exception {
        RoleDB getUserRole = new RoleDB();
        String user_role = getUserRole.userRole(role_number);
        return user_role;
    }
}

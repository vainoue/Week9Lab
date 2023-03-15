/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Role;
import models.User;
import services.RoleService;
import services.UserService;

/**
 *
 * @author vitor
 */
public class userServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        UserService us = new UserService();
        RoleService role_service = new RoleService();

        String action = request.getParameter("action");

        if (action != null) {
            if (action.equals("edit")) {
                String email = request.getParameter("userEmail");
                session.setAttribute("option", action);
                session.setAttribute("printEmail", email);

                //caso tenha que armazenar os valores que constam na database
                try {
                    User getRow = us.getUser(email);
                    List<Role> typeRole = (List<Role>) session.getAttribute("roles1");
                    String role = getRow.getRole().getRoleName();
                    String admin = typeRole.get(0).getRoleName();
                    String regular = typeRole.get(1).getRoleName();

                    session.setAttribute("systemAdmin", admin);
                    session.setAttribute("regularUser", regular);
                    session.setAttribute("selectedRole", role);
                    session.setAttribute("column", getRow);
                } catch (Exception ex) {
                    Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                //até aqui é para armazenar
            } else if (action.equals("cancel")) {
                session.setAttribute("option", action);
            } else if (action.equals("delete")) {
                String email = request.getParameter("userEmail");
                try {
                    us.deleteUser(email);
                    session.setAttribute("option", action);
                } catch (Exception ex) {
                    Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            List<User> users = us.getAll();
            session.setAttribute("users1", users);
            List<Role> roles = role_service.getAll();
            session.setAttribute("roles1", roles);
            if (users.isEmpty()) {
                String empty = "No users found. Please add a user.";
                request.setAttribute("emptyTable", empty);
            }

        } catch (Exception ex) {
            Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        UserService us = new UserService();

        String action = request.getParameter("action");

        String email = request.getParameter("inemail");
        String first_name = request.getParameter("fname");
        String last_name = request.getParameter("lname");
        String password = request.getParameter("inpassword");
        String roleSelection = request.getParameter("serole");
        int role = Integer.parseInt(roleSelection);
        User getRow = new User();

        if (action.equals("add")) {
            if (!email.equals("") && !first_name.equals("") && !last_name.equals("") && !password.equals("")) {
                try {
                    getRow = us.getUser(email);
                } catch (Exception ex) {
                    Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (getRow != null) {
                        String userExist = "This email already exists.";
                        request.setAttribute("invalid", userExist);
                    } else {
                        us.addUser(email, first_name, last_name, password, role);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                String message = "All fields are required.";
                request.setAttribute("errorMessage", message);
            }
        } else if (action.equals("update")) {
            email = (String) session.getAttribute("printEmail");
            session.setAttribute("option", action);

            if (!first_name.equals("") && !last_name.equals("") && !password.equals("")) {
                try {
                    us.updateUser(email, first_name, last_name, password, role);
                    String roleName = us.getUser(email).getRole().getRoleName();
                    getRow = us.getUser(email);

                    session.setAttribute("selectedRole", roleName);
                    session.setAttribute("column", getRow);

                } catch (Exception ex) {
                    Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                String message = "All fields are required.";
                request.setAttribute("errorMessage", message);
                String updateFail = "updateFail";
                session.setAttribute("option", updateFail);
            }
        }
        try {
            List<User> users = us.getAll();
            session.setAttribute("users1", users);
            if (users.isEmpty()) {
                String empty = "No users found. Please add a user.";
                request.setAttribute("emptyTable", empty);
            }
        } catch (Exception ex) {
            Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp")
                .forward(request, response);
    }
}

package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.User;

/**
 *
 * @author vitor
 */
public class UserDB {

    public ArrayList<User> getAll() throws Exception {
        ArrayList<User> users_array = new ArrayList<>();
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM userdb.user";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                //get data from database
                String email = rs.getString(1);
                String first_name = rs.getString(2);
                String last_name = rs.getString(3);
                String password = rs.getString(4);
                int role = rs.getInt(5);

                //create new User and add to array
                User newUser = new User(email, first_name, last_name, password, role);
                users_array.add(newUser);
            }
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }

        return users_array;
    }

    public void addUser(User newUser) throws Exception {
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        String sql = "INSERT INTO userdb.user VALUES (?, ?, ?, ?, ?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, newUser.getEmail());
            ps.setString(2, newUser.getFirstName());
            ps.setString(3, newUser.getLastName());
            ps.setString(4, newUser.getPassword());
            ps.setInt(5, newUser.getRole());
            ps.executeUpdate();
        } finally {
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }
    }

    public void updateUser(User updateUser) throws Exception {
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        String sql = "UPDATE userdb.user SET first_name=?, last_name=?, password=?, role=? WHERE email=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, updateUser.getFirstName());
            ps.setString(2, updateUser.getLastName());
            ps.setString(3, updateUser.getPassword());
            ps.setInt(4, updateUser.getRole());
            ps.setString(5, updateUser.getEmail());
            ps.executeUpdate();
        } finally {
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }
    }

    public ArrayList<User> getUser(String email) throws Exception {
        ArrayList<User> getUserRow = new ArrayList<>();
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM userdb.user WHERE email=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            rs.next();
            //get data from database
            String first_name = rs.getString(2);
            String last_name = rs.getString(3);
            String password = rs.getString(4);
            int role = rs.getInt(5);
            //get User data and add to array
            User newUser = new User(email, first_name, last_name, password, role);
            getUserRow.add(newUser);
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }

        return getUserRow;
    }

    public void deleteUser(String email) throws Exception {
        ConnectionPool cp = ConnectionPool.getInstance();
        Connection con = cp.getConnection();
        PreparedStatement ps = null;
        String sql = "DELETE FROM userdb.user WHERE email=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.executeUpdate();
        } finally {
            DBUtil.closePreparedStatement(ps);
            cp.freeConnection(con);
        }
    }
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import metier.SingletonConnection;
import metier.User;

public class UserDaoImpl implements IUserDao {

    @Override
    public User save(User user) {
        Connection conn = SingletonConnection.getConnection();
        try {
            // Check if user already exists
            PreparedStatement checkPs = conn.prepareStatement("SELECT login FROM user WHERE login = ?");
            checkPs.setString(1, user.getLogin());
            ResultSet checkRs = checkPs.executeQuery();
            
            if (checkRs.next()) {
                System.out.println("User already exists!");
                checkRs.close();
                checkPs.close();
                return null;
            }
            checkRs.close();
            checkPs.close();
            
            // Insert new user
            PreparedStatement ps = conn.prepareStatement("INSERT INTO user (login, mdp, auth_level) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getMdp());
            ps.setString(3, user.getAuthLevel());
            ps.executeUpdate();
            
            // Get generated idUser
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setIdUser(rs.getLong(1));
            }
            rs.close();
            ps.close();
            
            System.out.println("User added successfully: " + user.getLogin());
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User authenticate(String login, String password) {
        Connection conn = SingletonConnection.getConnection();
        User user = null;
        
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE login = ? AND mdp = ?");
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setIdUser(rs.getLong("idUser"));
                user.setLogin(rs.getString("login"));
                user.setMdp(rs.getString("mdp"));
                user.setAuthLevel(rs.getString("auth_level"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = SingletonConnection.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getLong("idUser"));
                user.setLogin(rs.getString("login"));
                user.setMdp(rs.getString("mdp"));
                user.setAuthLevel(rs.getString("auth_level"));
                users.add(user);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    @Override
    public User getUser(String login) {
        Connection conn = SingletonConnection.getConnection();
        User user = null;
        
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE login = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setIdUser(rs.getLong("idUser"));
                user.setLogin(rs.getString("login"));
                user.setMdp(rs.getString("mdp"));
                user.setAuthLevel(rs.getString("auth_level"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }

    @Override
    public User updateUser(User user) {
        Connection conn = SingletonConnection.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE user SET mdp = ?, auth_level = ? WHERE login = ?");
            ps.setString(1, user.getMdp());
            ps.setString(2, user.getAuthLevel());
            ps.setString(3, user.getLogin());
            int rows = ps.executeUpdate();
            ps.close();
            
            if (rows > 0) {
                System.out.println("User updated successfully: " + user.getLogin());
                return user;
            } else {
                System.out.println("User not found: " + user.getLogin());
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteUser(String login) {
        Connection conn = SingletonConnection.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM user WHERE login = ?");
            ps.setString(1, login);
            ps.executeUpdate();
            ps.close();
            
            System.out.println("User deleted: " + login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

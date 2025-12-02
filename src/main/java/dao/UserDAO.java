package dao;

import model.UserTO;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "userDAO")
@ApplicationScoped
public class UserDAO implements Serializable {

    
    private Connection getConnection() throws SQLException {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/SecurityDB");
            return ds.getConnection();
        } catch (NamingException e) {
            throw new SQLException("Problem z JNDI: " + e.getMessage());
        }
    }

    public List<UserTO> findAll() throws SQLException {
        List<UserTO> list = new ArrayList<>();
        
        String sql = "SELECT u.username, u.password, r.role_name " +
                     "FROM users u " +
                     "LEFT JOIN user_roles r ON u.username = r.username";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new UserTO(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role_name")
                ));
            }
        }
        return list;
    }

    
    public void save(UserTO user) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                if (user.isNewUser()) {
                    // INSERT
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                        ps.setString(1, user.getUsername());
                        ps.setString(2, user.getPassword());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO user_roles (username, role_name) VALUES (?, ?)")) {
                        ps.setString(1, user.getUsername());
                        ps.setString(2, user.getRole());
                        ps.executeUpdate();
                    }
                } else {
                    // UPDATE (Loginu zazwyczaj się nie zmienia, bo to klucz główny)
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE users SET password=? WHERE username=?")) {
                        ps.setString(1, user.getPassword());
                        ps.setString(2, user.getUsername());
                        ps.executeUpdate();
                    }
                    // Aktualizacja roli
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE user_roles SET role_name=? WHERE username=?")) {
                        ps.setString(1, user.getRole());
                        ps.setString(2, user.getUsername());
                        ps.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void delete(String username) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Najpierw rola (klucz obcy)
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM user_roles WHERE username=?")) {
                    ps.setString(1, username);
                    ps.executeUpdate();
                }
                // Potem użytkownik
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE username=?")) {
                    ps.setString(1, username);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
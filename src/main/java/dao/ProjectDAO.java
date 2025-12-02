package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import model.ProjectTO;

@ManagedBean(name = "projectDAO")
@ApplicationScoped
public class ProjectDAO implements Serializable { 

    private Connection getConnection() throws SQLException {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/BusinessDB");
            return ds.getConnection();
        } catch (NamingException e) {
            throw new SQLException("Problem z JNDI: " + e.getMessage());
        }
    }

    public List<ProjectTO> findAll() throws SQLException {
        List<ProjectTO> list = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM projects")) {
            while (rs.next()) {
                list.add(new ProjectTO(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("status")
                ));
            }
        }
        return list;
    }

    public void save(ProjectTO p) throws SQLException {
        try (Connection conn = getConnection()) {
            if (p.getId() == null) {
                String sql = "INSERT INTO projects (name, description, status) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getDescription());
                    ps.setString(3, p.getStatus());
                    ps.executeUpdate();
                }
            } else {
                String sql = "UPDATE projects SET name=?, description=?, status=? WHERE id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getDescription());
                    ps.setString(3, p.getStatus());
                    ps.setLong(4, p.getId());
                    ps.executeUpdate();
                }
            }
        }
    }

    public void delete(Long id) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM projects WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
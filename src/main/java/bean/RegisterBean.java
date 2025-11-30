package bean;


import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ManagedBean
@RequestScoped
public class RegisterBean {

    private String username;
    private String password;

    @Resource(name = "jdbc/SecurityDB")
    private DataSource dataSource;

    public String register() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            
            String sqlUser = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, username);
            psUser.setString(2, password);
            psUser.executeUpdate();

            String sqlRole = "INSERT INTO user_roles (username, role_name) VALUES (?, 'USER')";
            PreparedStatement psRole = conn.prepareStatement(sqlRole);
            psRole.setString(1, username);
            psRole.executeUpdate();

            return "/login.xhtml?faces-redirect=true";

        } catch (SQLException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd: " + e.getMessage(), null));
            return null;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

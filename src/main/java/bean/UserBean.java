package bean;

import dao.UserDAO;
import model.UserTO;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@ManagedBean
@ViewScoped
public class UserBean implements Serializable {

    @ManagedProperty("#{userDAO}")
    private UserDAO userDAO;

    private List<UserTO> users;
    private UserTO currentUser = new UserTO();

    @PostConstruct
    public void init() {
        // ZABEZPIECZENIE: Jeśli nie Admin, przekieruj na dashboard
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (!ctx.getExternalContext().isUserInRole("ADMIN")) {
            try {
                ctx.getExternalContext().redirect("index.xhtml");
            } catch (Exception e) { e.printStackTrace(); }
            return;
        }

        refreshList();
    }

    public void refreshList() {
        try {
            users = userDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void edit(UserTO u) {
        // Kopia do edycji
        this.currentUser = new UserTO(u.getUsername(), u.getPassword(), u.getRole());
    }

    public void cancelEdit() {
        this.currentUser = new UserTO();
    }

    public void save() {
        try {
            userDAO.save(currentUser);
            currentUser = new UserTO();
            refreshList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String username) {
        try {
            // Zabezpieczenie przed usunięciem samego siebie
            String myName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
            if (myName.equals(username)) {
                return;
            }
            
            userDAO.delete(username);
            refreshList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUserDAO(UserDAO userDAO) { this.userDAO = userDAO; }
    public List<UserTO> getUsers() { return users; }
    public UserTO getCurrentUser() { return currentUser; }
    public void setCurrentUser(UserTO currentUser) { this.currentUser = currentUser; }
}
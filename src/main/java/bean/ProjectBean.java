package bean;

import dao.ProjectDAO;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import model.ProjectTO;

@ManagedBean
@ViewScoped
public class ProjectBean implements Serializable {

    @ManagedProperty("#{projectDAO}")
    private ProjectDAO projectDAO;

    private List<ProjectTO> projects;
    private ProjectTO currentProject = new ProjectTO(); 

    @PostConstruct
    public void init() {
        refreshList();
    }

    public void refreshList() {
        try {
            projects = projectDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAjax() {
        try {
            projectDAO.save(currentProject);
            currentProject = new ProjectTO();
            refreshList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void delete(Long id) {
        try {
            projectDAO.delete(id);
            refreshList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String saveStandard() {
        try {
            projectDAO.save(currentProject);
            currentProject = new ProjectTO();
            return "projects?faces-redirect=true"; // Nawigacja
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setProjectDAO(ProjectDAO projectDAO) { this.projectDAO = projectDAO; }
    public List<ProjectTO> getProjects() { return projects; }
    public ProjectTO getCurrentProject() { return currentProject; }
    public void setCurrentProject(ProjectTO currentProject) { this.currentProject = currentProject; }
}
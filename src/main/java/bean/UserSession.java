/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package bean;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gabri
 */
@ManagedBean(name = "userSession")
@SessionScoped
public class UserSession implements Serializable {

    private AppBean appBean;

    public UserSession() {
    }

    public void setAppBean(AppBean appBean) {
        this.appBean = appBean;
    }

    public AppBean getAppBean() {
        return appBean;
    }

    public String getUsername() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "/login.xhtml?faces-redirect=true";
    }
}
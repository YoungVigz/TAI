package bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;

@ManagedBean
@SessionScoped
public class LocaleBean implements Serializable {

    private Locale locale;

    @PostConstruct
    public void init() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx.getExternalContext().getRequestLocale() != null) {
            locale = ctx.getExternalContext().getRequestLocale();
        } else {
            locale = ctx.getApplication().getDefaultLocale();
        }
    }

    public Locale getLocale() {
        if (locale == null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            return ctx.getApplication().getDefaultLocale();
        }
        return locale;
    }

    public String getLanguage() {
        return getLocale().getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
}
package bean;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.Arrays;
import java.util.List;

@ManagedBean(name = "appBean")
@ApplicationScoped
public class AppBean {
    public List<String> getStatuses() {
        return Arrays.asList("NOWY", "W TOKU", "ZAKONCZONY", "ARCHIWUM");
    }
    
    public String getAppVersion() {
        return "v1.0-Gabriel";
    }
}
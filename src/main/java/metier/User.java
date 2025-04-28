package metier;

import java.io.Serializable;

public class User implements Serializable {
    private Long idUser;
    private String login;
    private String mdp;
    private String authLevel;

    public User() {
        super();
    }

    public User(String login, String mdp, String authLevel) {
        this.login = login;
        this.mdp = mdp;
        this.authLevel = authLevel;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(String authLevel) {
        this.authLevel = authLevel;
    }

    @Override
    public String toString() {
        return "User [idUser=" + idUser + ", login=" + login + ", authLevel=" + authLevel + "]";
    }
}

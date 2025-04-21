package metier;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String mdp;
    
    public User() {
        super();
    }
    
    public User(String login, String mdp) {
        this.login = login;
        this.mdp = mdp;
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
    
    @Override
    public String toString() {
        return "User [login=" + login + "]";
    }
    
    // This method is kept for backward compatibility but uses database authentication
    public boolean verif() {
        // Will be replaced by DAO authentication
        if (login == null || mdp == null) {
            return false;
        }
        return login.equals("user") && mdp.equals("123");
    }
}
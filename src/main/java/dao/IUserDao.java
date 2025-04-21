package dao;
import java.util.List;
import metier.User;

public interface IUserDao {
    public User save(User user);
    public User authenticate(String login, String password);
    public List<User> getAllUsers();
    public User getUser(String login);
    public User updateUser(User user);
    public void deleteUser(String login);
}
package attendancemanagementsystem.controller;

import attendancemanagementsystem.dao.UserDAO;
import attendancemanagementsystem.model.User;

public class AuthController {
    private UserDAO userDao;
    
    public AuthController() {
        userDao = new UserDAO();
    }
    
    public boolean signup(String username, String password, String email) {
        if (userDao.isUsernameTaken(username)) {
            return false;
        }
        
        User user = new User(username, password, email);
        return userDao.createUser(user);
    }
    
    public User login(String username, String password) {
        return userDao.authenticateUser(username, password);
    }
}
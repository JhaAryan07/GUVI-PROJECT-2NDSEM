package attendancemanagementsystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import attendancemanagementsystem.controller.AuthController;
import attendancemanagementsystem.model.User;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthController authController;
    
    public LoginFrame() {
        authController = new AuthController();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Attendance Management - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                User user = authController.login(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                            "Login successful! Welcome " + user.getUsername());
                    new DashboardFrame(user).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                            "Invalid username or password", 
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });
        
        panel.add(loginButton);
        panel.add(signupButton);
        
        add(panel);
    }
}
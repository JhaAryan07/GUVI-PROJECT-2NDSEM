package attendancemanagementsystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import attendancemanagementsystem.controller.AuthController;

public class SignupFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private AuthController authController;
    
    public SignupFrame() {
        authController = new AuthController();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Attendance Management - Sign Up");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        
        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(SignupFrame.this, 
                            "All fields are required", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (authController.signup(username, password, email)) {
                    JOptionPane.showMessageDialog(SignupFrame.this, 
                            "Signup successful! Please login.");
                    new LoginFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(SignupFrame.this, 
                            "Signup failed. Username may already exist.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton loginButton = new JButton("Already have an account? Login");
        loginButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        
        panel.add(signupButton);
        panel.add(loginButton);
        
        add(panel);
    }
}
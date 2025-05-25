package attendancemanagementsystem.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import attendancemanagementsystem.model.Subject;
import attendancemanagementsystem.dao.SubjectDao;

public class AttendanceFrame extends JFrame {
    private final int userId;
    private final int subjectId;
    private final SubjectDao subjectDao;
    private Subject currentSubject;
    
    private JLabel infoLabel;
    private JLabel percentageLabel;
    private JLabel statusLabel;
    private JTextField targetField;
    
    public AttendanceFrame(int userId, int subjectId) {
        this.userId = userId;
        this.subjectId = subjectId;
        this.subjectDao = new SubjectDao();
        
        // Load subject before UI initialization
        if (!loadSubject()) {
            showErrorAndClose("Subject not found or unavailable");
            return;
        }
        
        initializeUI();
    }
    
    private boolean loadSubject() {
        try {
            List<Subject> subjects = subjectDao.getSubjectsByUser(userId);
            if (subjects == null || subjects.isEmpty()) {
                return false;
            }
            
            for (Subject subject : subjects) {
                if (subject != null && subject.getSubjectId() == subjectId) {
                    currentSubject = subject;
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void showErrorAndClose(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        dispose();
    }
    
    private void initializeUI() {
        setTitle("Attendance Management - " + currentSubject.getSubjectName());
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoLabel = new JLabel("Subject: " + currentSubject.getSubjectName());
        percentageLabel = new JLabel(String.format(
            "Current Attendance: %d/%d (%.1f%%)",
            currentSubject.getAttendedClasses(),
            currentSubject.getTotalClasses(),
            currentSubject.getAttendancePercentage()
        ));
        statusLabel = new JLabel();
        updateStatusLabel(75); // Default target percentage
        
        infoPanel.add(infoLabel);
        infoPanel.add(percentageLabel);
        infoPanel.add(statusLabel);
        
        // Target panel
        JPanel targetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        targetPanel.add(new JLabel("Target Percentage:"));
        targetField = new JTextField("75", 5);
        targetPanel.add(targetField);
        targetPanel.add(new JLabel("%"));
        
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> handleCalculate());
        targetPanel.add(calculateButton);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JButton presentButton = new JButton("Mark Present");
        presentButton.addActionListener(e -> handleAttendance(true));
        
        JButton absentButton = new JButton("Mark Absent");
        absentButton.addActionListener(e -> handleAttendance(false));
        
        buttonPanel.add(presentButton);
        buttonPanel.add(absentButton);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(targetPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void handleCalculate() {
        try {
            double target = Double.parseDouble(targetField.getText());
            if (target < 0 || target > 100) {
                throw new NumberFormatException();
            }
            updateStatusLabel(target);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid percentage (0-100)",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void handleAttendance(boolean isPresent) {
        try {
            if (subjectDao.updateAttendance(subjectId, isPresent)) {
                loadSubject(); // Refresh data
                updateDisplay();
                JOptionPane.showMessageDialog(
                    this,
                    "Attendance marked as " + (isPresent ? "present" : "absent"),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                throw new Exception("Update failed");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to update attendance",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void updateStatusLabel(double targetPercentage) {
        int needed = currentSubject.getClassesNeeded(targetPercentage);
        int safeSkips = currentSubject.getSafeSkips(targetPercentage);
        
        if (needed == 0) {
            statusLabel.setText(String.format(
                "You can skip up to %d classes and still maintain %.1f%%",
                safeSkips,
                targetPercentage
            ));
        } else {
            statusLabel.setText(String.format(
                "You need to attend %d more classes to reach %.1f%%",
                needed,
                targetPercentage
            ));
        }
    }
    
    private void updateDisplay() {
        infoLabel.setText("Subject: " + currentSubject.getSubjectName());
        percentageLabel.setText(String.format(
            "Current Attendance: %d/%d (%.1f%%)",
            currentSubject.getAttendedClasses(),
            currentSubject.getTotalClasses(),
            currentSubject.getAttendancePercentage()
        ));
        
        try {
            double target = Double.parseDouble(targetField.getText());
            updateStatusLabel(target);
        } catch (NumberFormatException ex) {
            updateStatusLabel(75); // Default if invalid
        }
    }
}
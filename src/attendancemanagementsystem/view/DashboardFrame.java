package attendancemanagementsystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import attendancemanagementsystem.model.Subject;
import attendancemanagementsystem.model.User;
import attendancemanagementsystem.dao.SubjectDao;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private SubjectDao subjectDao;
    private DefaultListModel<String> subjectListModel;
    private JList<String> subjectList;
    
    public DashboardFrame(User user) {
        this.currentUser = user;
        this.subjectDao = new SubjectDao();
        initializeUI();
        loadSubjects();
    }
    
    private void initializeUI() {
        setTitle("Attendance Management - Dashboard (" + currentUser.getUsername() + ")");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Subject list
        subjectListModel = new DefaultListModel<>();
        subjectList = new JList<>(subjectListModel);
        subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(subjectList);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        
        JButton addButton = new JButton("Add Subject");
        addButton.addActionListener(e -> {
            String subjectName = JOptionPane.showInputDialog(this, "Enter subject name:");
            if (subjectName != null && !subjectName.trim().isEmpty()) {
                Subject subject = new Subject(currentUser.getUserId(), subjectName.trim());
                if (subjectDao.createSubject(subject)) {
                    loadSubjects();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add subject", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton viewButton = new JButton("View/Update");
        viewButton.addActionListener(e -> {
            int selectedIndex = subjectList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selected = subjectListModel.getElementAt(selectedIndex);
                int subjectId = Integer.parseInt(selected.split(" - ")[0]);
                new AttendanceFrame(currentUser.getUserId(), subjectId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a subject first", 
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedIndex = subjectList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selected = subjectListModel.getElementAt(selectedIndex);
                int subjectId = Integer.parseInt(selected.split(" - ")[0]);
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to delete this subject?", 
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (subjectDao.deleteSubject(subjectId)) {
                        loadSubjects();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete subject", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a subject first", 
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        
        mainPanel.add(new JLabel("Your Subjects:"), BorderLayout.NORTH);
        mainPanel.add(listScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadSubjects() {
        subjectListModel.clear();
        List<Subject> subjects = subjectDao.getSubjectsByUser(currentUser.getUserId());
        for (Subject subject : subjects) {
            String display = String.format("%d - %s (Attended: %d/%d, %.1f%%)",
                    subject.getSubjectId(),
                    subject.getSubjectName(),
                    subject.getAttendedClasses(),
                    subject.getTotalClasses(),
                    subject.getAttendancePercentage());
            subjectListModel.addElement(display);
        }
    }
}
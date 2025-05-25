package attendancemanagementsystem.dao;

import attendancemanagementsystem.model.Subject;
import attendancemanagementsystem.model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDao {
    public boolean createSubject(Subject subject) {
        String sql = "INSERT INTO subjects(user_id, subject_name) VALUES(?,?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, subject.getUserId());
            pstmt.setString(2, subject.getSubjectName());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        subject.setSubjectId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Subject> getSubjectsByUser(int userId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subject_id"));
                subject.setUserId(rs.getInt("user_id"));
                subject.setSubjectName(rs.getString("subject_name"));
                subject.setTotalClasses(rs.getInt("total_classes"));
                subject.setAttendedClasses(rs.getInt("attended_classes"));
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }
    
    public boolean updateAttendance(int subjectId, boolean attended) {
        String sqlUpdateTotal = "UPDATE subjects SET total_classes = total_classes + 1 WHERE subject_id = ?";
        String sqlUpdateAttended = "UPDATE subjects SET attended_classes = attended_classes + 1 WHERE subject_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // First update total classes
            try (PreparedStatement pstmtTotal = conn.prepareStatement(sqlUpdateTotal)) {
                pstmtTotal.setInt(1, subjectId);
                pstmtTotal.executeUpdate();
            }
            
            // Then update attended if needed
            if (attended) {
                try (PreparedStatement pstmtAttended = conn.prepareStatement(sqlUpdateAttended)) {
                    pstmtAttended.setInt(1, subjectId);
                    pstmtAttended.executeUpdate();
                }
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
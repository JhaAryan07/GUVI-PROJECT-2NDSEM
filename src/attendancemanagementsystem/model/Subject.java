package attendancemanagementsystem.model;

public class Subject {
    private int subjectId;
    private int userId;
    private String subjectName;
    private int totalClasses;
    private int attendedClasses;
    
    public Subject() {}
    
    public Subject(int userId, String subjectName) {
        this.userId = userId;
        this.subjectName = subjectName;
    }
    
    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public int getTotalClasses() {
        return totalClasses;
    }
    
    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }
    
    public int getAttendedClasses() {
        return attendedClasses;
    }
    
    public void setAttendedClasses(int attendedClasses) {
        this.attendedClasses = attendedClasses;
    }
    
    // Business logic methods
    public double getAttendancePercentage() {
        if (totalClasses == 0) return 0;
        return ((double) attendedClasses / totalClasses) * 100;
    }
    
    public int getClassesNeeded(double targetPercentage) {
        if (targetPercentage <= 0) return 0;
        if (getAttendancePercentage() >= targetPercentage) return 0;
        
        double needed = (targetPercentage * totalClasses - 100 * attendedClasses) / (100 - targetPercentage);
        return (int) Math.ceil(needed);
    }
    
    public int getSafeSkips(double targetPercentage) {
        if (targetPercentage <= 0) return totalClasses - attendedClasses;
        
        double maxAbsent = ((100 - targetPercentage) * totalClasses) / targetPercentage;
        int safeSkips = (int) Math.floor(maxAbsent) - (totalClasses - attendedClasses);
        return Math.max(safeSkips, 0);
    }
}
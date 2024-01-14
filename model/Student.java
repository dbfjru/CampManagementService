package camp.model;

import java.util.ArrayList;

public class Student {
    private String studentId;
    private String studentName;

//    ArrayList<Subject> subjectList = new ArrayList<>();

    public Student(String seq, String studentName) {
        this.studentId = seq;
        this.studentName = studentName;
    }

    // Getter
    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

//    public ArrayList<Subject> getSubjectList() {
//        return subjectList;
//    }
}
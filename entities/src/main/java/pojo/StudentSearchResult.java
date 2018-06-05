package pojo;

import entities.Enrolment;
import entities.EnrolmentToken;
import entities.Student;

public class StudentSearchResult {

    private Student student;

    private Enrolment enrolment;

    private EnrolmentToken token;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public EnrolmentToken getToken() {
        return token;
    }

    public void setToken(EnrolmentToken token) {
        this.token = token;
    }
}

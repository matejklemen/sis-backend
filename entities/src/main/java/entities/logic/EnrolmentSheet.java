package entities.logic;

import entities.EnrolmentToken;
import entities.Student;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

public class EnrolmentSheet implements Serializable {
    @XmlElement
    private Student student;

    @XmlElement
    private EnrolmentToken enrolmentToken;

    @XmlElement
    private List<Integer> courses;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public EnrolmentToken getEnrolmentToken() {
        return enrolmentToken;
    }

    public void setEnrolmentToken(EnrolmentToken enrolmentToken) {
        this.enrolmentToken = enrolmentToken;
    }

    public List<Integer> getCourses() {
        return courses;
    }

    public void setCourses(List<Integer> courses) {
        this.courses = courses;
    }
}

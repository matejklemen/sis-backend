package entities.logic;

import entities.*;
import entities.curriculum.Course;
import entities.curriculum.StudentCourses;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;
import java.util.List;

public class EnrolmentSheet implements Serializable {

    @XmlElement
    private Enrolment enrolment;

    @XmlElement
    private List<Integer> courses;

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public List<Integer> getCourses() {
        return courses;
    }

    public void setCourses(List<Integer> courses) {
        this.courses = courses;
    }
}

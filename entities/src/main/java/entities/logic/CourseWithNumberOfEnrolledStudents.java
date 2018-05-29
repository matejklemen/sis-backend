package entities.logic;

import entities.curriculum.Course;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class CourseWithNumberOfEnrolledStudents implements Serializable {
    @XmlElement
    private Course course;

    @XmlElement
    private Integer numberOfEnrolledStudents;

    public entities.curriculum.Course getCourse() {
        return this.course;
    }

    public void setCourse(entities.curriculum.Course course) {
        this.course = course;
    }

    public Integer getNumberOfEnrolledStudents() {
        return numberOfEnrolledStudents;
    }

    public void setNumberOfEnrolledStudents(Integer numberOfEnrolledStudents) {
        this.numberOfEnrolledStudents = numberOfEnrolledStudents;
    }
}

package entities.curriculum;

import javax.persistence.*;
import java.io.Serializable;

/* "izpitni rok" */
@Entity(name = "course_exam_term")
@NamedQueries(value = {
    @NamedQuery(name = "CourseExamTerm.getTermsBetweenDates", query = "SELECT cet FROM course_exam_term cet WHERE cet.date >= :datetime1 AND cet.date <= :datetime2"),
    @NamedQuery(name = "CourseExamTerm.getTermsByCourseId", query = "SELECT cet FROM course_exam_term cet WHERE cet.course.course.id = :course_id AND cet.date >= :datetime")
})
public class CourseExamTerm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course_exam_term")
    private int id;

    @Column(nullable = false)
    private long date; /* UNIX timestamp of start date */

    private int duration;

    private int grade;

    @ManyToOne
    @JoinColumn(name = "id_course_organization")
    CourseOrganization course;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public CourseOrganization getCourse() {
        return course;
    }

    public void setCourse(CourseOrganization course) {
        this.course = course;
    }
}

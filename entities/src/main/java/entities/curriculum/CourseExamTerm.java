package entities.curriculum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    // ! note: String formatted as: YYYY-MM-DD HH:MM:SS
    private Date date;

    private int duration; /* in minutes */

    @ManyToOne
    @JoinColumn(name = "id_course_organization")
    CourseOrganization course;

    private boolean deleted = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public CourseOrganization getCourse() {
        return course;
    }

    public void setCourse(CourseOrganization course) {
        this.course = course;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

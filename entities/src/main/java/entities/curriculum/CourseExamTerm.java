package entities.curriculum;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/* "izpitni rok" */
@Entity(name = "course_exam_term")
@NamedQueries(value = {
    @NamedQuery(name = "CourseExamTerm.getTermsBetweenDates", query = "SELECT cet FROM course_exam_term cet WHERE cet.datetime >= :datetime1 AND cet.datetime <= :datetime2"),
    @NamedQuery(name = "CourseExamTerm.getTermsByCourseId", query = "SELECT cet FROM course_exam_term cet WHERE cet.course.course.id = :course_id AND cet.datetime >= :datetime")
})
public class CourseExamTerm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course_exam_term")
    private int id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    // ! note: String formatted as: YYYY-MM-DDTHH:MM:SS
    private Timestamp datetime;

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

    public String getDatetime() {
        // format for JSON output
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(datetime);
    }

    @XmlTransient // we are sending formatted datetime instead of this one
    public Timestamp getDatetimeObject() {
        return datetime;
    }

    public void setDatetime(Timestamp date) {
        this.datetime = date;
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

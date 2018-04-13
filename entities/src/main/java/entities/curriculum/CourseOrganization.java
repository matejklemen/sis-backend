package entities.curriculum;

import entities.Professor;
import entities.StudyYear;

import javax.persistence.*;

/* Represents the metadata about who are the organizers of a course in a particular study year */
@Entity(name = "course_organization")
@NamedQueries(value = {
        @NamedQuery(name = "CourseOrganization.getAll", query = "SELECT co FROM course_organization co"),
        @NamedQuery(name = "CourseOrganization.getByCourseId", query = "SELECT co FROM course_organization co WHERE co.course.id = :idCourse"),
        @NamedQuery(name = "CourseOrganization.getByProfessorId", query = "SELECT co FROM course_organization co WHERE (co.organizer1.id = :profId OR co.organizer2.id = :profId OR co.organizer3.id = :profId)"),
        @NamedQuery(name = "CourseOrganization.getByStudyYear", query = "SELECT co FROM course_organization co WHERE co.studyYear.name = :studyYear")
})
public class CourseOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course_organization")
    private int idCourseOrganization;

    @ManyToOne
    @JoinColumn(name = "id_study_year", nullable = false)
    private StudyYear studyYear;

    @ManyToOne
    @JoinColumn(name = "id_professor1", nullable = false)
    private Professor organizer1;

    @ManyToOne
    @JoinColumn(name = "id_professor2")
    private Professor organizer2;

    @ManyToOne
    @JoinColumn(name = "id_professor3")
    private Professor organizer3;

    @ManyToOne
    @JoinColumn(name = "id_course", nullable = false)
    private Course course;

    public int getIdCourseOrganization() {
        return idCourseOrganization;
    }

    public void setIdCourseOrganization(int idCourseOrganization) {
        this.idCourseOrganization = idCourseOrganization;
    }

    public StudyYear getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(StudyYear studyYear) {
        this.studyYear = studyYear;
    }

    public Professor getOrganizer1() {
        return organizer1;
    }

    public void setOrganizer1(Professor organizer1) {
        this.organizer1 = organizer1;
    }

    public Professor getOrganizer2() {
        return organizer2;
    }

    public void setOrganizer2(Professor organizer2) {
        this.organizer2 = organizer2;
    }

    public Professor getOrganizer3() {
        return organizer3;
    }

    public void setOrganizer3(Professor organizer3) {
        this.organizer3 = organizer3;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String toString() {
        return String.format("CourseOrganization(studyYear = %s, course = %s, organizer1 = %s)", studyYear.getName(), course.getName(), organizer1.toString());
    }
}

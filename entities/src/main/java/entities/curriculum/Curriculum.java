package entities.curriculum;

import entities.StudyProgram;
import entities.StudyYear;

import javax.persistence.*;
import java.io.Serializable;

/*
    Information about a course that has at some point been or is still being run.
*/
@Entity(name = "curriculum")
@NamedQueries(
        value = {
                @NamedQuery(name = "Curriculum.getAll", query = "SELECT cur FROM curriculum cur"),
                @NamedQuery(name = "Curriculum.getByStudyProgramId", query = "SELECT cur FROM curriculum cur WHERE cur.idStudyProgram.id = :id"),
                /* Example use-case: get courses for BUN study program */
                @NamedQuery(name = "Curriculum.getByStudyProgramName", query = "SELECT cur FROM curriculum cur WHERE cur.idStudyProgram.name = :name"),
                /* Example use-case: get courses for doctorate study degree */
                @NamedQuery(name = "Curriculum.getByStudyProgramDegreeName", query = "SELECT cur FROM curriculum cur WHERE cur.idStudyProgram.studyDegree.name = :name"),
                @NamedQuery(name = "Curriculum.getFirstYear",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.studyYear.name = :nameStudyYear " +
                                "AND cur.idStudyProgram.id = :idStudyProgram " +
                                "AND cur.yearOfProgram = 1"),
                /* Example use-case: get module courses for specific year for undergraduate UNI program */
                @NamedQuery(name = "Curriculum.getModuleCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.POC.type = \"mod\" " +
                                "AND cur.studyYear.name = :nameStudyYear " +
                                "AND cur.idStudyProgram.id = :idStudyProgram " +
                                "AND cur.yearOfProgram = :yearOfProgram"),
                @NamedQuery(name = "Curriculum.getMandatoryCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.POC.type = \"obv\" " +
                                "AND cur.studyYear.name = :nameStudyYear " +
                                "AND cur.idStudyProgram.id = :idStudyProgram " +
                                "AND cur.yearOfProgram = :yearOfProgram"),
                /* Note: specialist elective courses = strokovni izbirni predmeti */
                @NamedQuery(name = "Curriculum.getSpecialistElectiveCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.POC.type = \"siz\" " +
                                "AND cur.studyYear.name = :nameStudyYear " +
                                "AND cur.idStudyProgram.id = :idStudyProgram " +
                                "AND cur.yearOfProgram = :yearOfProgram"),
                /* Note: general elective courses = splošni izbirni predmeti */
                @NamedQuery(name = "Curriculum.getGeneralElectiveCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.POC.type = \"piz\" " +
                                "AND cur.studyYear.name = :nameStudyYear " +
                                "AND cur.idStudyProgram.id = :idStudyProgram " +
                                "AND cur.yearOfProgram = :yearOfProgram")
        }
)
public class Curriculum implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curriculum")
    private int idCurriculum;
    @ManyToOne
    @JoinColumn(name = "part_of_curriculum")
    private PartOfCurriculum POC;

    @ManyToOne
    @JoinColumn(name = "id_course")
    private Course idCourse;

    @ManyToOne
    @JoinColumn(name = "id_study_program")
    private StudyProgram idStudyProgram;

    @ManyToOne
    @JoinColumn(name = "id_study_year")
    private StudyYear studyYear;

    @Column(name = "year_of_program")
    private int yearOfProgram; // = letnik

    public int getIdCurriculum() {
        return idCurriculum;
    }

    public void setIdCurriculum(int idCurriculum) {
        this.idCurriculum = idCurriculum;
    }

    public PartOfCurriculum getPOC() {
        return POC;
    }

    public void setPOC(PartOfCurriculum POC) {
        this.POC = POC;
    }

    public Course getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(Course idCourse) {
        this.idCourse = idCourse;
    }

    public StudyProgram getIdStudyProgram() {
        return idStudyProgram;
    }

    public void setIdStudyProgram(StudyProgram idStudyProgram) {
        this.idStudyProgram = idStudyProgram;
    }

    public StudyYear getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(StudyYear studyYear) {
        this.studyYear = studyYear;
    }
}

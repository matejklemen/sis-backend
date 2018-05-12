package entities.curriculum;

import entities.StudyProgram;
import entities.StudyYear;
import interfaces.Codelistable;

import javax.persistence.*;
import java.io.Serializable;

/*
    Information about a course that has at some point been or is still being run.
*/
@Entity(name = "curriculum")
@NamedQueries(
        value = {
                @NamedQuery(name = "Curriculum.getByIdCurriculum", query = "SELECT cur FROM curriculum cur WHERE cur.id = :id_curriculum"),
                @NamedQuery(name = "Curriculum.getByStudyProgramId", query = "SELECT cur FROM curriculum cur WHERE cur.idStudyProgram.id = :id"),
                /* Example use-case: get courses for BUN study program */
                @NamedQuery(name = "Curriculum.getByStudyProgramName", query = "SELECT cur FROM curriculum cur WHERE cur.idStudyProgram.name = :name"),
                /* Example use-case: get courses for doctorate study degree */
                @NamedQuery(name = "Curriculum.getByStudyProgramDegreeName", query = "SELECT cur FROM curriculum cur WHERE cur.idStudyProgram.studyDegree.name = :name"),
                @NamedQuery(name = "Curriculum.getFirstYear",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.studyYear.name = :name_study_year " +
                                "AND cur.idStudyProgram.id = :id_study_program " +
                                "AND cur.yearOfProgram = 1 "),
                /* Example use-case: get module courses for specific year for undergraduate UNI program */
                @NamedQuery(name = "Curriculum.getAllModuleCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.poc.type = \"mod\" " +
                                "AND cur.studyYear.name = :name_study_year " +
                                "AND cur.idStudyProgram.id = :id_study_program " +
                                "AND cur.yearOfProgram = :year_of_program " +
                                "ORDER BY cur.poc.id"),
                /* Example use-case: get module courses for module "Informacijski sistemi" in year 2017/2018 */
                @NamedQuery(name = "Curriculum.getCurriculumByPOC",
                            query = "SELECT cur FROM curriculum cur WHERE " +
                                    "cur.poc.id = :id_poc " +
                                    "AND cur.studyYear.name = :name_study_year " +
                                    "AND cur.idStudyProgram.id = :id_study_program " +
                                    "AND cur.yearOfProgram = :year_of_program "),
                @NamedQuery(name = "Curriculum.getMandatoryCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.poc.type = \"obv\" " +
                                "AND cur.studyYear.name = :name_study_year " +
                                "AND cur.idStudyProgram.id = :id_study_program " +
                                "AND cur.yearOfProgram = :year_of_program "),
                /* Note: specialist elective courses = strokovni izbirni predmeti */
                @NamedQuery(name = "Curriculum.getSpecialistElectiveCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.poc.type = \"siz\" " +
                                "AND cur.studyYear.name = :name_study_year " +
                                "AND cur.idStudyProgram.id = :id_study_program " +
                                "AND cur.yearOfProgram = :year_of_program "),
                /* Note: general elective courses = splošni izbirni predmeti */
                @NamedQuery(name = "Curriculum.getGeneralElectiveCourses",
                        query = "SELECT cur FROM curriculum cur WHERE " +
                                "cur.poc.type = \"piz\" " +
                                "AND cur.studyYear.name = :name_study_year " +
                                "AND cur.idStudyProgram.id = :id_study_program " +
                                "AND cur.yearOfProgram = :year_of_program ")
        }
)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames =
                { "part_of_curriculum", "id_course", "id_study_program", "id_study_year", "year_of_program" }) })
public class Curriculum implements Serializable, Codelistable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curriculum")
    private int id;
    @ManyToOne
    @JoinColumn(name = "part_of_curriculum")
    private PartOfCurriculum poc;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PartOfCurriculum getPoc() {
        return poc;
    }

    public void setPoc(PartOfCurriculum POC) {
        this.poc = POC;
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

    public int getYearOfProgram() {
        return yearOfProgram;
    }

    public void setYearOfProgram(int yearOfProgram) {
        this.yearOfProgram = yearOfProgram;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"poc", "idCourse", "idStudyProgram", "studyYear", "yearOfProgram"};
    }

    @Override
    public String[] getColumnTypes() {
        return new String[]{"poc", "courses", "studyprograms", "studyyears", TYPE_NUMBER};
    }

    @Column(columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean deleted = false;

    @Override
    public boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

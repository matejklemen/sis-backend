package pojo;

import entities.Student;
import entities.StudyYear;
import entities.curriculum.Course;

import java.sql.Timestamp;

public class SignUpInfoResponse {
    private int idExamSignUp;
    private Student studentInfo;
    private StudyYear yearOfTakingCourse; // study year in which student selected course
    private Integer currentGrade;
    private Integer finalGrade;
    // number of times that the student took an exam on this course prior to (and including) this sign up
    private Integer currentNumberOfTakings;

    public int getIdExamSignUp() {
        return idExamSignUp;
    }

    public void setIdExamSignUp(int idExamSignUp) {
        this.idExamSignUp = idExamSignUp;
    }

    public Student getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(Student studentInfo) {
        this.studentInfo = studentInfo;
    }

    public StudyYear getYearOfTakingCourse() {
        return yearOfTakingCourse;
    }

    public void setYearOfTakingCourse(StudyYear yearOfTakingCourse) {
        this.yearOfTakingCourse = yearOfTakingCourse;
    }

    public Integer getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Integer finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Integer getCurrentNumberOfTakings() {
        return currentNumberOfTakings;
    }

    public void setCurrentNumberOfTakings(Integer currentNumberOfTakings) {
        this.currentNumberOfTakings = currentNumberOfTakings;
    }

    public Integer getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(Integer currentGrade) {
        this.currentGrade = currentGrade;
    }
}

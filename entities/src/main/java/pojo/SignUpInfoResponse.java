package pojo;

import entities.Student;
import entities.StudyYear;

public class SignUpInfoResponse {
    private int idExamSignUp;
    private Student studentInfo;
    private StudyYear yearOfTakingCourse; // study year in which student selected course
    private Integer writtenScore;
    private Integer suggestedGrade;
    // number of times that the student took an exam on this course prior to (and including) this sign up
    private Integer currentNumberOfTakings;
    private Boolean isReturned;

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

    public Integer getSuggestedGrade() {
        return suggestedGrade;
    }

    public void setSuggestedGrade(Integer suggestedGrade) {
        this.suggestedGrade = suggestedGrade;
    }

    public Integer getCurrentNumberOfTakings() {
        return currentNumberOfTakings;
    }

    public void setCurrentNumberOfTakings(Integer currentNumberOfTakings) {
        this.currentNumberOfTakings = currentNumberOfTakings;
    }

    public Integer getWrittenScore() {
        return writtenScore;
    }

    public void setWrittenScore(Integer writtenScore) {
        this.writtenScore = writtenScore;
    }

    public Boolean getReturned() {
        return isReturned;
    }

    public void setReturned(Boolean returned) {
        isReturned = returned;
    }
}

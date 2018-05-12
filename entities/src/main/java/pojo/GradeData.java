package pojo;

import entities.curriculum.CourseOrganization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GradeData {

    private String Year;

    private CourseOrganization courseOrganization;

    private String ExamGrade;

    private String FinalGrade;


    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public CourseOrganization getCourseOrganization() {
        return courseOrganization;
    }

    public void setCourseOrganization(CourseOrganization courseOrganization) {
        this.courseOrganization = courseOrganization;
    }

    public String getExamGrade() {
        return ExamGrade;
    }

    public void setExamGrade(String examGrade) {
        ExamGrade = examGrade;
    }

    public String getFinalGrade() {
        return FinalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        FinalGrade = finalGrade;
    }
}

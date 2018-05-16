package pojo;

import entities.curriculum.CourseOrganization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GradeData {

    private String Year;

    private CourseOrganization courseOrganization;

    private Integer examGrade;

    private Integer finalGrade;


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

    public Integer getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(Integer examGrade) {
        this.examGrade = examGrade;
    }

    public Integer getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Integer finalGrade) {
        this.finalGrade = finalGrade;
    }
}

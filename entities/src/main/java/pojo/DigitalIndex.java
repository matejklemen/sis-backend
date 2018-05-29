package pojo;

import entities.curriculum.StudentCourses;

import java.util.List;

public class DigitalIndex {

    private List<StudentCourses> passedCourses;

    private List<Statistics> statistics;

    public List<StudentCourses> getPassedCourses() {
        return passedCourses;
    }

    public void setPassedCourses(List<StudentCourses> passedCourses) {
        this.passedCourses = passedCourses;
    }

    public List<Statistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistics> statistics) {
        this.statistics = statistics;
    }
}

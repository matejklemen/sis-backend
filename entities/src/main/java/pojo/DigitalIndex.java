package pojo;

import entities.curriculum.StudentCourses;

import java.util.List;

public class DigitalIndex {

    public List<DigitalIndexByProgram> indexList;

    public List<DigitalIndexByProgram> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<DigitalIndexByProgram> indexList) {
        this.indexList = indexList;
    }
}

package beans.logic;

import beans.crud.CurriculumBean;
import beans.crud.GradeBean;
import entities.Enrolment;
import entities.Grade;
import entities.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
public class EnrolmentPolicyBean {

    @Inject
    private GradeBean gb;

    @Inject
    private CurriculumBean cb;

    public boolean hasStudentFreeChoiceOfCurriculum(Student s){
        if(s.getEnrolments().isEmpty())
            return false;

        Iterator<Enrolment> iters = s.getEnrolments().iterator();
        int maxYear = 0;
        while(iters.hasNext()){
            int tmp = iters.next().getYear();
            if(tmp > maxYear)
                maxYear = tmp;
        }

        if(maxYear != 2)
            return false;

        List<Grade> grades =  gb.getGradesByStudentId(s.getId());
        double sum = 0.0;
        for(Grade grade: grades){
            sum += grade.getGrade();
        }
        if (sum/grades.size() > 8.5)
            return true;

        return false;
    }
}

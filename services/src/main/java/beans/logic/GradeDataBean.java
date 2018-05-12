package beans.logic;

import beans.crud.CourseOrganizationBean;
import beans.crud.EnrolmentBean;
import beans.crud.ExamSignUpBean;
import beans.crud.StudentCoursesBean;
import entities.Enrolment;
import entities.curriculum.CourseOrganization;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import pojo.GradeData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class GradeDataBean {

    @Inject private EnrolmentBean eb;
    @Inject private StudentCoursesBean scb;
    @Inject private ExamSignUpBean esub;
    @Inject private CourseOrganizationBean cob;

    public List<GradeData> getStudentGradeData(int studentId){
        List<GradeData> gradeDataList = new ArrayList<>();

        Enrolment e = eb.getLastEnrolmentByStudentId(studentId);
        List<StudentCourses> scl = scb.getStudentCoursesByEnrolmentId(e.getId());

        for(StudentCourses sc : scl){
            List<ExamSignUp> esu = esub.getExamSignUpsOnCourseForStudent(sc.getCourse().getId(), studentId);
            CourseOrganization so = cob.getCourseOrganizationsByCourseIdAndYear(sc.getCourse().getId(), e.getStudyYear().getId());

            String examGrade = null;
            if(!esu.isEmpty()){
                Collections.sort(esu, Comparator.comparingInt(ExamSignUp ::getId));
                if(esu.get(0).getGrade() != null){
                    examGrade = esu.get(0).getGrade().toString();
                }else if(esu.size() > 1){
                    examGrade = esu.get(1).getGrade().toString();
                }
            }

            GradeData gd = new GradeData();
            gd.setExamGrade(examGrade);
            gd.setFinalGrade(sc.getGrade()== null?"":sc.getGrade().toString());
            gd.setCourseOrganization(so);
            gd.setYear(Integer.toString(e.getYear()));

            gradeDataList.add(gd);
        }
        return gradeDataList;
    }
}

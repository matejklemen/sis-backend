package beans.logic;

import beans.crud.EnrolmentBean;
import beans.crud.ExamSignUpBean;
import beans.crud.StudentCoursesBean;
import entities.Enrolment;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class StudentCoursesLogicBean {

    @Inject private StudentCoursesBean scB;
    @Inject private EnrolmentBean eB;
    @Inject private ExamSignUpBean esuB;

    public List<StudentCourses> getPassedCourses(int studentId){
        List<StudentCourses> passedCourses = new ArrayList<>();

        // We get all enrolments for student
        List<Enrolment> allEnrolments = eB.getEnrolmentsByStudentId(studentId);

        for(Enrolment enrolment : allEnrolments){
            // We get courses for each enrolment
            List<StudentCourses> allStudentCourses = scB.getStudentCoursesByEnrolmentId(enrolment.getId());

            for(StudentCourses studentCourse : allStudentCourses){
                // We get last exam sign up with grade for each course
                List<ExamSignUp>  allExamSignUps = esuB.getExamSignUpsForStudentCourse(studentCourse.getIdStudentCourses());

                if(allExamSignUps.isEmpty())
                    continue;

                // We sort them by date
                allExamSignUps.sort(Comparator.comparing(o -> o.getCourseExamTerm().getDatetimeObject()));

                // We get the grade and date from last
                ExamSignUp lastSignUp = allExamSignUps.get(0);

                if(lastSignUp.getSuggestedGrade() != null && lastSignUp.getSuggestedGrade() > 5){
                    studentCourse.setGrade(lastSignUp.getSuggestedGrade());
                    studentCourse.setDateOfGrade(lastSignUp.getCourseExamTerm().getDatetimeObject());

                    passedCourses.add(studentCourse);
                }
            }
        }

        return passedCourses;
    }
}

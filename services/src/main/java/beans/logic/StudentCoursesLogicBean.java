package beans.logic;

import beans.crud.EnrolmentBean;
import beans.crud.ExamSignUpBean;
import beans.crud.StudentCoursesBean;
import entities.Enrolment;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import pojo.DigitalIndex;
import pojo.Statistics;

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

    public DigitalIndex getPassedCourses(int studentId){
        List<StudentCourses> passedCourses = new ArrayList<>();
        List<Statistics> statistics = new ArrayList<>();

        // We get all enrolments for student
        List<Enrolment> allEnrolments = eB.getEnrolmentsByStudentId(studentId);
        allEnrolments.sort(Comparator.comparing(o -> o.getYear()));

        for(Enrolment enrolment : allEnrolments){
            Statistics stat = new Statistics();
            stat.setYear(enrolment.getYear());

            // We get courses for each enrolment
            List<StudentCourses> allStudentCourses = scB.getStudentCoursesByEnrolmentId(enrolment.getId());
            stat.setTotalCourses(allStudentCourses.size());

            int passedCoursesCount = 0;
            int sumOfGrades = 0;
            for(StudentCourses studentCourse : allStudentCourses){
                // We get last exam sign up with grade for each course
                List<ExamSignUp>  allExamSignUps = esuB.getExamSignUpsForStudentCourseWithPositiveGrade(studentCourse.getIdStudentCourses());

                if(allExamSignUps.isEmpty())
                    continue;

                // We sort them by date
                allExamSignUps.sort(Comparator.comparing(o -> o.getCourseExamTerm().getDatetimeObject()));

                // We get the grade and date from last
                ExamSignUp lastSignUp = allExamSignUps.get(allExamSignUps.size() - 1);

                if(lastSignUp.getSuggestedGrade() != null && lastSignUp.getSuggestedGrade() > 5){
                    studentCourse.setGrade(lastSignUp.getSuggestedGrade());
                    studentCourse.setDateOfGrade(lastSignUp.getCourseExamTerm().getDatetimeObject());
                    studentCourse.setYear(enrolment.getYear());

                    passedCoursesCount++;
                    sumOfGrades += lastSignUp.getSuggestedGrade();

                    passedCourses.add(studentCourse);
                }
            }
            stat.setPassedCourses(passedCoursesCount);
            stat.setAvg((float)sumOfGrades/(float)passedCoursesCount);

            statistics.add(stat);
        }
        passedCourses.sort(Comparator.comparing(o -> o.getDateOfGrade()));

        DigitalIndex di = new DigitalIndex();
        di.setPassedCourses(passedCourses);
        di.setStatistics(statistics);
        return di;
    }
}

package beans.logic;

import beans.crud.EnrolmentBean;
import beans.crud.ExamSignUpBean;
import beans.crud.StudentCoursesBean;
import entities.Enrolment;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import pojo.DigitalIndex;
import pojo.DigitalIndexByProgram;
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
        List<DigitalIndexByProgram> indexList = new ArrayList<>();

        // We get all enrolments for student
        List<Enrolment> allEnrolments = eB.getEnrolmentsByStudentId(studentId);
        allEnrolments.sort(Comparator.comparing(o -> o.getYear()));
        allEnrolments.sort(Comparator.comparing(o -> Integer.parseInt(o.getStudyProgram().getId())));

        Integer currProgramId = null;
        DigitalIndexByProgram digitalIndexByProgram = null;
        for(Enrolment enrolment : allEnrolments){

            // For each new program
            if(currProgramId == null || currProgramId != Integer.parseInt(enrolment.getStudyProgram().getId())){

                // Add record for prev. if any
                if(digitalIndexByProgram != null) {
                    passedCourses.sort(Comparator.comparing(o -> o.getDateOfGrade()));
                    digitalIndexByProgram.setPassedCourses(passedCourses);
                    digitalIndexByProgram.setStatistics(statistics);
                    indexList.add(digitalIndexByProgram);
                }
                // Reset all states
                digitalIndexByProgram = new DigitalIndexByProgram();
                passedCourses = new ArrayList<>();
                statistics = new ArrayList<>();
                digitalIndexByProgram.setStudyProgram(enrolment.getStudyProgram().getName().toUpperCase() +" - "+enrolment.getStudyProgram().getStudyDegree().getName());
                currProgramId = Integer.parseInt(enrolment.getStudyProgram().getId());
            }
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
            stat.setAvg(passedCoursesCount == 0? 0: (float)sumOfGrades/(float)passedCoursesCount);
            stat.setSchoolYear(enrolment.getStudyYear().getName());

            statistics.add(stat);
        }

        passedCourses.sort(Comparator.comparing(o -> o.getDateOfGrade()));
        digitalIndexByProgram.setPassedCourses(passedCourses);
        digitalIndexByProgram.setStatistics(statistics);
        indexList.add(digitalIndexByProgram);

        DigitalIndex di = new DigitalIndex();
        di.setIndexList(indexList);
        return di;
    }
}

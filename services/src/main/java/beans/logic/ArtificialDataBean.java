package beans.logic;

import beans.crud.*;
import entities.*;
import entities.curriculum.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ArtificialDataBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject private CurriculumBean cb;
    @Inject private StudentBean sb;
    @Inject private StudyTypeBean stb;
    @Inject private StudyKindBean stk;
    @Inject private StudyFormBean stf;
    @Inject private StudyYearBean syb;
    @Inject private StudyProgramBean spb;
    @Inject private StudentCoursesBean scb;
    @Inject private KlasiusSrvBean ksb;
    @Inject private ECTSDistributionBean edb;
    @Inject private EnrolmentTokenBean etb;
    @Inject private EnrolmentBean eb;
    @Inject private ExamSignUpBean esub;
    @Inject private CourseOrganizationBean cob;
    @Inject private CourseExamTermBean cetb;

    // isFreeChoiceThirdYear will be false on every (yearOfProgram != 3)
    // ... will only be true sometimes when (yearOfProgram == 3)
    public EnrolmentToken generateEnrolmentToken(int idStudent, StudyProgram studyProgram,
                                                 StudyYear studyYear, int yearOfProgram,
                                                 boolean isFreeChoiceThirdYear) {
        Student student = sb.getStudent(idStudent);

        EnrolmentToken enrToken = new EnrolmentToken();
        enrToken.setStudent(student);
        enrToken.setStudyYear(studyYear);
        enrToken.setStudyProgram(studyProgram);
        enrToken.setYear(yearOfProgram);

        // prvi vpis v letnik
        StudyType studyType = stb.getStudyType(1);
        enrToken.setType(studyType);

        // redni
        StudyKind studyKind = stk.getStudyKind(1);
        enrToken.setKind(studyKind);

        // na lokaciji
        StudyForm studyForm = stf.getStudyForm(1);
        enrToken.setForm(studyForm);

        // assign klasius depending on study program (1000470 = BVS RI -> assign "Visokošolska strokovna izobrazba")
        Integer idKlasiusSrv = studyProgram.getId().equals("1000470")? 16203: 16204;
        KlasiusSrv klasiusSrv = ksb.getKlasiusSrv(idKlasiusSrv);
        enrToken.setKlasiusSrv(klasiusSrv);

        enrToken.setUsed(false);

        enrToken.setFreeChoice(isFreeChoiceThirdYear);
        EnrolmentToken newEnrToken = etb.putEnrolmentToken(enrToken);

        return newEnrToken;
    }

    // currently only works for BUN RI study program
    public List<Integer> selectRandomCourses(EnrolmentToken et) {
        List<Integer> chosenCourses = new ArrayList<Integer>();

        int yearOfProgram = et.getYear();
        StudyYear studyYear = et.getStudyYear();
        StudyProgram studyProgram = et.getStudyProgram();

        ECTSDistribution ectsDistribution = edb.getECTSDistribution(studyYear.getId(),
                yearOfProgram, studyProgram.getId());

        List<Curriculum> mandatoryCourses = cb.getMandatoryCourses(studyYear.getName(), studyProgram.getId(), yearOfProgram);
        for(Curriculum curr: mandatoryCourses)
            chosenCourses.add(curr.getIdCourse().getId());

        int sizECTSRequired = ectsDistribution.getEctsSiz();
        int pizECTSRequired = ectsDistribution.getEctsPiz();
        int modECTSRequired = ectsDistribution.getEctsMod();

        // choose specialist elective courses
        if(sizECTSRequired > 0 && pizECTSRequired > 0) {
            List<Curriculum> sizCourses = cb.getSpecialistElectiveCourses(studyYear.getName(), studyProgram.getId(), yearOfProgram);

            int idxChosenCourse1 = (int)(Math.random() * sizCourses.size());
            int idxChosenCourse2 = (idxChosenCourse1 + 1) % sizCourses.size();

            // always choose 2 specialist elective courses
            chosenCourses.add(sizCourses.get(idxChosenCourse1).getIdCourse().getId());
            chosenCourses.add(sizCourses.get(idxChosenCourse2).getIdCourse().getId());


            log.info(String.format("Chosen specialist elective course: %d %s",
                    sizCourses.get(idxChosenCourse1).getIdCourse().getId(),
                    sizCourses.get(idxChosenCourse1).getIdCourse().getName()));
            log.info(String.format("Chosen specialist elective course: %d %s",
                    sizCourses.get(idxChosenCourse2).getIdCourse().getId(),
                    sizCourses.get(idxChosenCourse2).getIdCourse().getName()));
        }

        // choose module courses
        if(modECTSRequired > 0) {
            List<Curriculum> modCourses = cb.getModuleCourses(studyYear.getName(), studyProgram.getId(), yearOfProgram);
            boolean hasFreeChoice = et.isFreeChoice();
            log.info(String.format("I %s free module courses choice",
                    hasFreeChoice? "have": "don't have"));

            if(hasFreeChoice) {
                Collections.shuffle(modCourses);

                for(int i = 0; i < modECTSRequired / 6; i++) {
                     log.info(String.format("Chosen module course: %d %s",
                             modCourses.get(i).getIdCourse().getId(),
                             modCourses.get(i).getIdCourse().getName()));
                    chosenCourses.add(modCourses.get(i).getIdCourse().getId());
                }

                // choose an additional module course instead of a general elective course
                chosenCourses.add(modCourses.get(modECTSRequired / 6).getIdCourse().getId());
                log.info(String.format("Chosen general elective course: %d %s",
                        modCourses.get(modECTSRequired / 6).getIdCourse().getId(),
                        modCourses.get(modECTSRequired / 6).getIdCourse().getName()));
            }
            else {
                // module consists of 3 courses
                int numberOfAllModules = modCourses.size() / 3;
                List<Integer> possibleIdxModule = new ArrayList<Integer>();
                for(int i = 0; i < numberOfAllModules; i++)
                    possibleIdxModule.add(i);

                Collections.shuffle(possibleIdxModule);
                // choose whole module together
                for(int i = 0; i < modECTSRequired / (6 * 3); i++) {
                    for(int j = 0; j < 3; j++) {
                        log.info(String.format("Chosen module course: %d %s",
                                modCourses.get(possibleIdxModule.get(i) * 3 + j).getIdCourse().getId(),
                                modCourses.get(possibleIdxModule.get(i) * 3 + j).getIdCourse().getName()));
                        chosenCourses.add(modCourses.get(possibleIdxModule.get(i) * 3 + j).getIdCourse().getId());
                    }
                }

                // choose an additional module course instead of a general elective course
                chosenCourses.add(modCourses.get(possibleIdxModule.get(modECTSRequired / (6 * 3)) * 3 +
                        (int)(Math.random() * 3)).getIdCourse().getId());
                log.info(String.format("Chosen general elective course: %d %s",
                        modCourses.get(possibleIdxModule.get(modECTSRequired / (6 * 3)) * 3 +
                                (int)(Math.random() * 3)).getIdCourse().getId(),
                        modCourses.get(possibleIdxModule.get(modECTSRequired / (6 * 3)) * 3 +
                                (int)(Math.random() * 3)).getIdCourse().getName()));
            }
        }

        return chosenCourses;
    }

    public void generateExamSignUpsAndGrades(EnrolmentToken enrToken,
                                             Enrolment enr, boolean isFreeChoiceThirdYear) {
        List<StudentCourses> chosenCourses = scb.getStudentCoursesByEnrolmentId(enr.getId());
        int minSuccessfulGrade = isFreeChoiceThirdYear? 9: 6;
        int maxSuccessfulGrade = 10;

        for(StudentCourses course: chosenCourses) {
            log.info(String.format("Doing stuff for course %s", course.getCourse().getName()));
            CourseOrganization courseOrganization = cob.getCourseOrganizationsByCourseIdAndYear(course.getCourse().getId(),
                    enrToken.getStudyYear().getId());

            List<CourseExamTerm> availableTerms = cetb.getExamTermsByCourse(courseOrganization.getId());

            if(availableTerms.size() == 0)
                log.severe(String.format("No exam terms for course %d", course.getCourse().getId()));
            else {
                int maxNumberOfTerms = availableTerms.size() > 3 ? 3 : availableTerms.size();
                int numberOfTerms = (int) (Math.random() * maxNumberOfTerms) + 1;

                // unsuccessful attempts
                for (int i = 0; i < numberOfTerms - 1; i++) {
                    ExamSignUp esu = new ExamSignUp();
                    esu.setCourseExamTerm(availableTerms.get(i));
                    esu.setStudentCourses(course);
                    esu.setSuggestedGrade(5);
                    esu.setReturned(false);
                    esub.addExamSignUp(esu);
                    log.info(String.format("Added an exam sign up for course %d %s with grade %d",
                            course.getCourse().getId(), course.getCourse().getName(), 5));
                }

                int finalGrade = (int)(Math.random() * (maxSuccessfulGrade - minSuccessfulGrade) + minSuccessfulGrade);

                // successful attempt
                ExamSignUp esu = new ExamSignUp();
                esu.setCourseExamTerm(availableTerms.get(numberOfTerms - 1));
                esu.setStudentCourses(course);
                esu.setSuggestedGrade(finalGrade);
                esu.setReturned(false);
                esub.addExamSignUp(esu);

                // set final grade of the course as the last grade obtained from sign-ups
                course.setGrade(finalGrade);
                scb.updateCourse(course);
                log.info(String.format("Added an exam sign up for course %d %s with grade %d [ = FINAL GRADE]",
                        course.getCourse().getId(), course.getCourse().getName(), esu.getSuggestedGrade())); // TODO: check if that's what you want
            }
        }
    }

    // copies latest grade on exam sign ups for each course in 1st and 2nd year of student with ID 'idStudent'.
    public void transferCourseGrades(int idStudent) {
        Student student = sb.getStudent(idStudent);
        log.info(String.format("Updating stuff for student %s %s...", student.getName(), student.getSurname()));

        List<Enrolment> enrolmentsForStudent = eb.getEnrolmentsByStudentId(idStudent);

        for(Enrolment enr: enrolmentsForStudent) {
            // don't set final grades for third year yet
            if(enr.getYear() == 3)
                break;

            List<StudentCourses> studentCourses = scb.getStudentCoursesByEnrolmentId(enr.getId());

            // for each student course, write the last obtained grade as the final course grade
            for(StudentCourses sc: studentCourses) {
                log.info(String.format("Checking stuff for course %d %s...", sc.getCourse().getId(),
                        sc.getCourse().getName()));
                ExamSignUp lastSignUp = esub.getLastSignUpForStudentCourse(sc.getIdStudentCourses());

                if(lastSignUp != null) {
                    log.info(String.format("Setting final grade to %d for course %d %s...", lastSignUp.getWrittenScore(),
                            sc.getCourse().getId(), sc.getCourse().getName()));
                    sc.setGrade(lastSignUp.getSuggestedGrade());

                    scb.updateCourse(sc);
                }
            }
        }
    }

    public void caller(int idStudent, String idStudyProgram, int idStudyYear, int toYearOfProgram) {
        boolean isFreeChoiceThirdYear = (int)(Math.random() * 10) == 1;
        log.info(String.format("isFreeChoice outside of main logic: %b", isFreeChoiceThirdYear));

        for(int yr = 1; yr <= toYearOfProgram; yr++) {
            // log.info(String.format("Doing stuff for year#%d", yr));
            StudyProgram sp = spb.getStudyProgram(idStudyProgram);
            // relies on later study years having a bigger ID than previous study year
            // (e.g. 2017/2018 = id 5, 2016/2017 = id 4)
            StudyYear sy = syb.getStudyYear(idStudyYear - (toYearOfProgram - yr));
            EnrolmentToken newEnrToken = generateEnrolmentToken(idStudent, sp, sy, yr,
                    yr == 3? isFreeChoiceThirdYear: false);
            List<Integer> chosenCourses = selectRandomCourses(newEnrToken);
            Enrolment enr = eb.putEnrolment(newEnrToken, chosenCourses);
            enr.setConfirmed(true);
            eb.updateEnrolment(enr);

            newEnrToken.setUsed(true);
            etb.updateEnrolmentToken(newEnrToken);

            // set grades for all but the last year
            if(yr != toYearOfProgram)
                generateExamSignUpsAndGrades(newEnrToken, enr, isFreeChoiceThirdYear);
        }
    }
}

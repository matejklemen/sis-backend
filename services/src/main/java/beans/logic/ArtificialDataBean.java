package beans.logic;

import beans.crud.*;
import entities.*;
import entities.curriculum.Curriculum;
import entities.curriculum.ECTSDistribution;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
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
    @Inject private KlasiusSrvBean ksb;
    @Inject private ECTSDistributionBean edb;
    @Inject private EnrolmentTokenBean etb;
    @Inject private EnrolmentBean eb;

    public EnrolmentToken generateEnrolmentToken(int idStudent, StudyProgram studyProgram,
                                                 StudyYear studyYear, int yearOfProgram) {
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

        boolean freeChoice = false;
        // add a bit of randomness into the process
        if(yearOfProgram == 3)
            freeChoice = (int)(Math.random() * 10) == 1;

        enrToken.setFreeChoice(freeChoice);
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

        // choose specialist elective courses - TODO: write this in a more general manner
        if(sizECTSRequired > 0 && pizECTSRequired > 0) {
            List<Curriculum> sizCourses = cb.getSpecialistElectiveCourses(studyYear.getName(), studyProgram.getId(), yearOfProgram);

            int idxChosenCourse1 = (int)(Math.random() * sizCourses.size());
            int idxChosenCourse2 = (idxChosenCourse1 + 1) % sizCourses.size();

            // always choose 2 specialist elective courses
            chosenCourses.add(sizCourses.get(idxChosenCourse1).getIdCourse().getId());
            chosenCourses.add(sizCourses.get(idxChosenCourse2).getIdCourse().getId());

            /*
            log.info(String.format("Chosen specialist elective course: %d %s",
                    sizCourses.get(idxChosenCourse1).getIdCourse().getId(),
                    sizCourses.get(idxChosenCourse1).getIdCourse().getName()));
            log.info(String.format("Chosen specialist elective course: %d %s",
                    sizCourses.get(idxChosenCourse2).getIdCourse().getId(),
                    sizCourses.get(idxChosenCourse2).getIdCourse().getName()));
            */
        }

        // choose module courses
        if(modECTSRequired > 0) {
            List<Curriculum> modCourses = cb.getModuleCourses(studyYear.getName(), studyProgram.getId(), yearOfProgram);
            boolean hasFreeChoice = et.isFreeChoice();
            // log.info(String.format("I %s free module courses choice",
            //         hasFreeChoice? "have": "don't have"));

            if(hasFreeChoice) {
                Collections.shuffle(modCourses);

                for(int i = 0; i < modECTSRequired / 6; i++) {
                    // log.info(String.format("Chosen module course: %d %s",
                    //         modCourses.get(i).getIdCourse().getId(),
                    //         modCourses.get(i).getIdCourse().getName()));
                    chosenCourses.add(modCourses.get(i).getIdCourse().getId());
                }
            }
            else {
                // module consists of 3 courses
                int numberOfAllModules = modCourses.size() / 3;
                List<Integer> possibleIdxModule = new ArrayList<Integer>();
                for(int i = 0; i < numberOfAllModules; i++)
                    possibleIdxModule.add(i);

                Collections.shuffle(possibleIdxModule);
                log.info(possibleIdxModule.toString());
                // choose whole module together
                for(int i = 0; i < modECTSRequired / (6 * 3); i++) {
                    for(int j = 0; j < 3; j++) {
                        // log.info(String.format("Chosen module course: %d %s",
                        //         modCourses.get(possibleIdxModule.get(i) * 3 + j).getIdCourse().getId(),
                        //         modCourses.get(possibleIdxModule.get(i) * 3 + j).getIdCourse().getName()));
                        chosenCourses.add(modCourses.get(possibleIdxModule.get(i) * 3 + j).getIdCourse().getId());
                    }
                }
            }
        }

        return chosenCourses;
    }

    public void caller(int idStudent, String idStudyProgram, int idStudyYear, int toYearOfProgram) {
        for(int yr = 1; yr <= toYearOfProgram; yr++) {
            // log.info(String.format("Doing stuff for year#%d", yr));
            StudyProgram sp = spb.getStudyProgram(idStudyProgram);
            // relies on later study years having a bigger ID than previous study year
            // (e.g. 2017/2018 = id 5, 2016/2017 = id 4)
            StudyYear sy = syb.getStudyYear(idStudyYear - (toYearOfProgram - yr));
            EnrolmentToken newEnrToken = generateEnrolmentToken(idStudent, sp, sy, yr);
            List<Integer> chosenCourses = selectRandomCourses(newEnrToken);
            Enrolment enr = eb.putEnrolment(newEnrToken, chosenCourses);
            enr.setConfirmed(true);
            eb.updateEnrolment(enr);
        }
    }
}

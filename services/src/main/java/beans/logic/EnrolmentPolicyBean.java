package beans.logic;

import beans.crud.*;
import com.arjuna.ats.jta.exceptions.NotImplementedException;
import entities.*;
import entities.address.Country;
import entities.address.Municipality;
import entities.curriculum.Curriculum;
import entities.curriculum.ECTSDistribution;
import entities.logic.EnrolmentSheet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

import static org.eclipse.persistence.config.TargetDatabase.Database;

@ApplicationScoped
public class EnrolmentPolicyBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private static final String BUN_RI = "1000468";
    private static final String BVS_RI = "1000470";
    private static final String BM_RI = "1000471";
    private static final int SLO_COUNTRY_ID = 705;

    @Inject
    private GradeBean GradeBean;

    @Inject
    private EnrolmentBean enrolmentBean;

    @Inject
    private PostAddressBean postAddressBean;

    @Inject
    private CountryBean countryBean;

    @Inject
    private MunicipalityBean municipalityBean;

    @Inject
    private StudyProgramBean studyProgramBean;

    @Inject
    private StudyTypeBean studyTypeBean;

    @Inject
    private KlasiusSrvBean klasiusSrvBean;

    @Inject
    private StudyFormBean studyFormBean;

    @Inject
    private StudyKindBean studyKindBean;

    @Inject
    private ECTSDistributionBean ectsDistributionBean;

    @Inject
    private CurriculumBean curriculumBean;

    public boolean hasStudentFreeChoiceOfCurriculum(Student s){
        if(enrolmentBean.getEnrolmentsForStudent(s.getId()).isEmpty())
            return false;

        Iterator<Enrolment> iters = enrolmentBean.getEnrolmentsForStudent(s.getId()).iterator();
        int maxYear = 0;
        while(iters.hasNext()){
            int tmp = iters.next().getYear();
            if(tmp > maxYear)
                maxYear = tmp;
        }

        if(maxYear != 2)
            return false;

        List<Grade> grades =  GradeBean.getGradesByStudentId(s.getId());
        double sum = 0.0;
        for(Grade grade: grades){
            sum += grade.getGrade();
        }
        if (sum/grades.size() > 8.5)
            return true;

        return false;
    }

    private List<String> checkCourses(EnrolmentSheet es, List<String> errList) {
        String chosenStudyProgram = es.getEnrolmentToken().getStudyProgram().getId();
        int chosenIdStudyYear = es.getEnrolmentToken().getStudyYear().getId();
        int chosenYearOfProgram = es.getEnrolmentToken().getYear();
        boolean hasStudentFreeModuleChoice = es.getEnrolmentToken().isFreeChoice();

        List<Integer> takenCourses = es.getCourses();
        /* Object containing number of required mandatory, general
           elective, specialist elective and module courses (in number of ECTS). */
        ECTSDistribution requiredCourseDistribution = ectsDistributionBean.getECTSDistribution(
                chosenIdStudyYear, chosenYearOfProgram, chosenStudyProgram);

        int requiredObv = requiredCourseDistribution.getEctsObv(),
                requiredPiz = requiredCourseDistribution.getEctsPiz(),
                requiredSiz = requiredCourseDistribution.getEctsSiz(),
                requiredMod = requiredCourseDistribution.getEctsMod();

        log.info(String.format("Required course distribution: [obv=%d, piz=%d, siz=%d, mod=%d]",
                requiredObv, requiredPiz, requiredSiz, requiredMod));

        /* Check if the enrolment sheet contains the right amount of ECTS for mandatory, general
           elective, specialist elective and module courses. */
        int sumObv = 0, sumPiz = 0, sumSiz = 0, sumMod = 0;

        HashMap<Integer, Integer> numOfCoursesPerModuleChosen = new HashMap<>();

        for(Integer idCourse: takenCourses) {
            if(idCourse == null) {
                // TODO: throw 500?
                errList.add("A course is unspecified.");
                log.severe("ID of a course is not specified (= null). How??");
                continue;
            }

            Curriculum currCourse = curriculumBean.getCourseMetadata(idCourse, chosenYearOfProgram, chosenStudyProgram, chosenIdStudyYear);

            // will only happen if database is unpopulated with data for a course in specific year
            if(currCourse == null) {
                errList.add(String.format("A course you selected (%d) is not planned to be carried out in selected year.",
                        idCourse));
                continue;
            }

            String coursePocType = currCourse.getPoc().getType();
            int courseECTS = currCourse.getIdCourse().getCreditPoints();
            int moduleId = currCourse.getPoc().getId();

            switch (coursePocType) {
                case "obv": sumObv += courseECTS; break;
                case "piz": sumPiz += courseECTS; break;
                case "siz": sumSiz += courseECTS; break;
                case "mod": {
                    sumMod += courseECTS;

                    // count how many distinct modules the enrolment sheet contains (if any)
                    Integer numberOfExistingCoursesInModule = numOfCoursesPerModuleChosen.get(moduleId);
                    if(numberOfExistingCoursesInModule == null)
                        numOfCoursesPerModuleChosen.put(moduleId, 1);
                    else
                        numOfCoursesPerModuleChosen.put(moduleId, numberOfExistingCoursesInModule + 1);
                    break;
                }
            }
        }

        log.info(String.format("Course distribution on the EnrolmentSheet: [obv=%d, piz=%d, siz=%d, mod=%d]...\n" +
                        "That is without taking into account that sometimes part of points for siz can be moved to piz.",
                sumObv, sumPiz, sumSiz, sumMod));

        /* Check if the student chose to take an additional specialist elective course instead of taking general
            elective courses. */
        // Note: the check if study program is "BUN RI" might be redundant (not sure)
        if(sumSiz > 0 && sumPiz != requiredPiz && chosenStudyProgram.equals(BUN_RI)) {
            int diff = sumSiz - requiredSiz;

            sumSiz -= diff;
            sumPiz += diff;

            if(sumPiz != requiredPiz) {
                errList.add("You did not take enough courses from the general elective courses area.");
            }
            else
                log.info("The student took an additional specialist elective course, the absolute madman/madwoman!");
        }

        int numberOfDistinctModules = numOfCoursesPerModuleChosen.size();
        if(numberOfDistinctModules == 3) {
            /* the only way a student with no free module choice is allowed to choose 3 distinct modules is by choosing
                1 module course instead of a general elective course. */
            if(!hasStudentFreeModuleChoice) {
                int diff = requiredMod - sumMod;

                sumPiz += diff;
                sumMod -= diff;

                if(sumPiz != requiredPiz) {
                    errList.add("You do not have permission to choose more than two distinct modules.");
                }
            }
        }

        if(sumMod != requiredMod) {
            errList.add(String.format("Number of chosen ECTS points for module courses (%d) does not equal the required " +
                    "number of ECTS points for module courses (%d).", sumMod, requiredMod));
        }
        if(sumObv != requiredObv) {
            errList.add(String.format("Number of chosen ECTS points for mandatory courses (%d) does not equal the required " +
                    "number of ECTS points for mandatory courses (%d).", sumObv, requiredObv));
        }
        if(sumPiz != requiredPiz) {
            errList.add(String.format("Number of chosen ECTS points for general elective courses (%d) does not equal the required " +
                    "number of ECTS points for general elective courses (%d).", sumPiz, requiredPiz));
        }
        if(sumSiz != requiredSiz) {
            errList.add(String.format("Number of chosen ECTS points for specialist elective courses (%d) does not equal the required " +
                    "number of ECTS points for general elective courses (%d).", sumObv, requiredObv));
        }

        return errList;
    }

    public List<String> checkEnrolment(EnrolmentSheet es, EnrolmentToken enToken){
        List<String> list = new ArrayList<>();
        if(enToken == null) {
            list.add("napaka v zahtevku: ni vpisnega žetona");
            return list;
        }

        // Preveri, da po končanem vnosu ni moč popravljati podatkov (izkoriščen žeton)
        if(enToken.isUsed())
            list.add("vpisni žeton je že porabljen");

        if(!enToken.validEnrolmentToken(es.getEnrolmentToken())) {
            list.add("ni veljavnega vpisnega žetona za izbran vpis");
            return list;
        }

        if(es.getStudent().getAddress1() != null && !postAddressBean.existsPostAddress(es.getStudent().getAddress1().getPost().getId())) {
            list.add("neveljavna poštna številka za stalno bivališče");
        }
        if(es.getStudent().getAddress2() != null && !postAddressBean.existsPostAddress(es.getStudent().getAddress2().getPost().getId())) {
            list.add("neveljavna poštna številka za začasno bivališče");
        }

        Municipality munOfBirth = es.getStudent().getMunicipalityOfBirth();
        Country countryOfBirth = es.getStudent().getCountryOfBirth();
        /* "Kraj rojstva" either empty because the student forgot to fill it or the student is from a foreign country */
        if(munOfBirth == null)
            list.add("občina rojstva ni izbrana");
        else {
            if(munOfBirth.getId() == 999 && countryOfBirth.getId() == SLO_COUNTRY_ID)
                list.add("izbrana država rojstva je Slovenija, a izbrana občina rojstva ni v Sloveniji");

            if(!municipalityBean.existsMunicipality(munOfBirth.getId()))
                list.add("neveljavna koda občine rojstva");
        }

        if(!countryBean.existsCountry(countryOfBirth.getId()))
            list.add("neveljavna koda države rojstva");

        if(!studyProgramBean.existsStudyProgram(es.getEnrolmentToken().getStudyProgram().getId())) {
            list.add("neveljavna koda študijskega programa");
        }
        if(!studyTypeBean.existsStudyType(es.getEnrolmentToken().getType().getId())) {
            list.add("neveljavna koda vrste vpisa");
        }
        if(es.getEnrolmentToken().getKlasiusSrv() != null && !klasiusSrvBean.existsKlasiusSrv(es.getEnrolmentToken().getKlasiusSrv().getId())) {
            list.add("neveljavna koda vrste študija (klasius)");
        }
        if(!studyFormBean.existsStudyForm(es.getEnrolmentToken().getForm().getId())) {
            list.add("neveljavna koda oblike študija");
        }
        if(!studyKindBean.existsStudyKind(es.getEnrolmentToken().getKind().getId())) {
            list.add("neveljavna koda načina študija");
        }
        if(es.getEnrolmentToken().getYear() < 1 || es.getEnrolmentToken().getYear() > 3) {
            list.add("neveljaven izbran letnik študija");
        }

        String inputEMSO = es.getStudent().getEmso();

        if (inputEMSO == null)
            list.add("EMŠO ni vnesen");
        else if(inputEMSO.length() != 13)
            list.add("EMŠO mora vsebovati točno 13 števk");
        else {
            int weightedSum = 0;
            // multiplying numbers: 7 6 5 4 3 2 7 6 5 4 3 2
            for(int i = 0; i < 12; i++) {
                int currMultiplier = (11 - i) % 6 + 2;
                weightedSum += (int)(inputEMSO.charAt(i) - '0') * currMultiplier;
            }

            int controlNumber = 11 - (weightedSum % 11);

            String[] dateSplit = es.getStudent().getDateOfBirth().split("-");
            String dd = dateSplit[2].substring(0, 2);
            String mm = dateSplit[1];
            String yyy = dateSplit[0].substring(1, 4);

            String reconstructedEMSO = String.format("%s%s%s%s%d",
                    dd, mm, yyy, (es.getStudent().getGender() == 'M' ? "50[0-4][0-9]{2}" : "50[5-9][0-9]{2}"), controlNumber);

            if(!inputEMSO.matches(reconstructedEMSO))
                list.add("neveljaven EMŠO");
        }

        if(!es.getStudent().getName().matches("[A-Ž][a-ž]+")) {
            list.add("neveljaven vnos za ime");
        }
        if(!es.getStudent().getSurname().matches("[A-Ž][a-ž]+")) {
            list.add("neveljaven vnos za priimek");
        }

        List<Enrolment> studentEnrolments = enrolmentBean.getEnrolmentsForStudent(es.getStudent().getId());
        if(studentEnrolments.isEmpty()) {
            // student is enrolling for the first time, but is apparently not enrolling into 1st year of school
            if(es.getEnrolmentToken().getYear() != 1)
                list.add("neveljaven letnik za prvi vpis");
        } else {
            Enrolment maxEnrolment = Collections.max(studentEnrolments, new Comparator<Enrolment>() {
                @Override
                public int compare(Enrolment o1, Enrolment o2) {
                    return Integer.compare(o1.getYear(), o2.getYear());
                }
            });

            // year of enrolment is either same or higher by 1 than the latest enrolment ('maxEnrolment')
            int yearOfProgramDiff = es.getEnrolmentToken().getYear() - maxEnrolment.getYear();

            if(yearOfProgramDiff < 0 || yearOfProgramDiff > 1)
                list.add("neveljaven letnik vpisa");
        }

        int maxNumberOfYearsProgramme = es.getEnrolmentToken().getStudyProgram().getSemesters() / 2;
        // example that this prevents: student should not be able to enrol into 6th year of a 2 year program
        if(es.getEnrolmentToken().getYear() > maxNumberOfYearsProgramme)
            list.add(String.format("neveljaven letnik vpisa za študijski program - %s ima samo %d letnika/-e/-ov",
                    es.getEnrolmentToken().getStudyProgram().getName(), maxNumberOfYearsProgramme));

        // Preveri pravilnost kombinacije letnik+študijski program+modul
        list = checkCourses(es, list);

        // Preveri pravilnost kombinacije vrsta vpisa+letnik
        // ??

        // Preveri pravilnost kombinacije študijski program + vrsta študija (Klasius SRP)
        String enteredStudyProgram = es.getEnrolmentToken().getStudyProgram().getId();
        KlasiusSrv enteredKlasius = es.getEnrolmentToken().getKlasiusSrv();

        if(enteredKlasius == null)
            list.add("nedefinirana koda vrste študija (klasius)");
        else {
            switch (enteredStudyProgram) {
                case BVS_RI: {
                    if(enteredKlasius.getId() != 16203)
                        list.add("neveljavna koda vrste študija za izbran študijski program");
                    break;
                }
                case BUN_RI: {
                    if(enteredKlasius.getId() != 16204)
                        list.add("neveljavna koda vrste študija za izbran študijski program");
                    break;
                }
                case BM_RI: {
                    if(enteredKlasius.getId() != 17003)
                        list.add("neveljavna koda vrste študija za izbran študijski program");
                    break;
                }
                default: log.info("Warning: unimplemented study program - KLASIUS SRV check... Default action is NOP.");
            }
        }

        // Preveri, da študent ne more vnesti vrste vpisa 98.
        boolean invalidStudyType = (es.getEnrolmentToken().getType().getId() == 98);
        if(invalidStudyType)
            list.add("neveljavna vrsta vpisa"); // TODO: referentka lahko vnese 98, študent pa ne?

        // Preveri, da so avtomatsko dodani obvezni predmeti. [DONE]

        // Preveri seznam vpisanih.*

        // Preveri izpis vpisnega lista.*

        return list;
    }
}

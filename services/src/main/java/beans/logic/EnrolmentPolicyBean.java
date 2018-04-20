package beans.logic;

import beans.crud.*;
import com.arjuna.ats.jta.exceptions.NotImplementedException;
import entities.*;
import entities.curriculum.Curriculum;
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
    private static final String BUN_RM = "1000407";

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

    boolean checkYearProgramModuleCombo(EnrolmentSheet es) throws NotImplementedException {
        // TODO
        if(es.getEnrolmentToken().getYear() == 3) {
            String studyProgram = es.getEnrolmentToken().getStudyProgram().getId();
            boolean hasFreeChoice = es.getEnrolmentToken().isFreeChoice();

            if(studyProgram.equals(BUN_RI)) {

            }

            if(studyProgram.equals(BUN_RM)) {

            }

        }

        throw new NotImplementedException("Method checkYearProgramModuleCombo() in EnrolmentPolicyBean not implemented yet");
    }

    public List<String> checkEnrolment(EnrolmentSheet es, EnrolmentToken enToken){
        List<String> list = new ArrayList<>();
        if(enToken == null) {
            list.add("No token found");
            return list;
        }

        // Preveri, da po končanem vnosu ni moč popravljati podatkov (izkoriščen žeton)
        if(enToken.isUsed())
            list.add("Enrolment token already used");

        if(!enToken.validEnrolmentToken(es.getEnrolmentToken())) {
            list.add("No valid token for given enrolment");
            return list;
        }

        if(!postAddressBean.existsPostAddress(es.getStudent().getAddress1().getPost().getId())) {
            list.add("Address1 invalid post code");
        }
        if(es.getStudent().getAddress2() != null && !postAddressBean.existsPostAddress(es.getStudent().getAddress2().getPost().getId())) {
            list.add("Address2 invalid post code");
        }
        if(!municipalityBean.existsMunicipality(es.getStudent().getMunicipalityOfBirth().getId())) {
            list.add("Invalid municipality code");
        }
        if(!countryBean.existsCountry(es.getStudent().getCountryOfBirth().getId())) {
            list.add("Invalid country code");
        }
        if(!studyProgramBean.existsStudyProgram(es.getEnrolmentToken().getStudyProgram().getId())) {
            list.add("Invalid study program code");
        }
        if(!studyTypeBean.existsStudyType(es.getEnrolmentToken().getType().getId())) {
            list.add("Invalid study type code");
        }
        if(es.getEnrolmentToken().getKlasiusSrv() != null && !klasiusSrvBean.existsKlasiusSrv(es.getEnrolmentToken().getKlasiusSrv().getId())) {
            list.add("Invalid klasius_srv code");
        }
        if(!studyFormBean.existsStudyForm(es.getEnrolmentToken().getForm().getId())) {
            list.add("Invalid study form code");
        }
        if(!studyKindBean.existsStudyKind(es.getEnrolmentToken().getKind().getId())) {
            list.add("Invalid study kind code");
        }
        if(es.getEnrolmentToken().getYear() < 1 || es.getEnrolmentToken().getYear() > 3) {
            list.add("Invalid year range");
        }

        String date = es.getStudent().getDateOfBirth();
        String EMSO = date.split("-")[2].substring(0, 2)+date.split("-")[1]+date.split("-")[0].substring(1,4)+(es.getStudent().getGender() == 'M' ? "50[0-4][0-9]{3}" : "50[5-9][0-9]{3}");
        if(!es.getStudent().getEmso().matches(EMSO)) {
            list.add("Invalid EMSO number");
        }

        if(!es.getStudent().getName().matches("[A-Ž][a-ž]+")) {
            list.add("Invalid name format");
        }
        if(!es.getStudent().getSurname().matches("[A-Ž][a-ž]+")) {
            list.add("Invalid surname format");
        }

        if(municipalityBean.existsMunicipality(es.getStudent().getMunicipalityOfBirth().getId()) && es.getStudent().getCountryOfBirth().getId() != 705) {
            list.add("Invalid country and municipality combination");
        }

        List<Enrolment> studentEnrolments = enrolmentBean.getEnrolmentsForStudent(es.getStudent().getId());
        // student is enrolling for the first time, but is apparently not enrolling into 1st year of school
        if(studentEnrolments.isEmpty() && es.getEnrolmentToken().getYear() != 1) {
            list.add("Invalid year for first enrolment for this student");
        } else {
            /*
            TO-DO: popravi, da deluje za prvi vpis v prvi letnik
            Enrolment maxEnrolment = Collections.max(studentEnrolments, new Comparator<Enrolment>() {
                @Override
                public int compare(Enrolment o1, Enrolment o2) {
                    return Integer.compare(o1.getYear(), o2.getYear());
                }
            });
            // first condition: make sure student doesn't try to skip a year in school
            // second condition: student is somehow enrolling into a year that he already passed
            if(maxEnrolment.getYear() + 1 < es.getEnrolmentToken().getYear() ||  maxEnrolment.getYear() > es.getEnrolmentToken().getYear()) {
                list.add("Invalid year for enrolment for this student");
            }
            */
        }

        int maxNumberOfYearsProgramme = es.getEnrolmentToken().getStudyProgram().getSemesters() / 2;
        // example that this prevents: student should not be able to enrol into 6th year of a 2 year program
        if(es.getEnrolmentToken().getYear() > maxNumberOfYearsProgramme)
            list.add(String.format("Trying to enrol into a nonexisting year of school for this program. %s only has %d years...",
                    es.getEnrolmentToken().getStudyProgram().getName(), maxNumberOfYearsProgramme));

        // Preveri pravilnost kombinacije letnik+študijski program+modul
        boolean validYearProgramModuleCombo = true;
        try {
            // TODO: not implemented -> remove try-catch and the thrown exception after implementing
            validYearProgramModuleCombo = checkYearProgramModuleCombo(es);
        }
        catch (NotImplementedException e) {
            log.info(e.getMessage());
        }

        if(!validYearProgramModuleCombo)
            list.add("Invalid year of program + study program + module courses combination.");

        // Preveri pravilnost kombinacije vrsta vpisa+letnik

        // Preveri pravilnost kombinacije študijski program + vrsta študija (Klasius SRP)

        // Preveri, da študent ne more vnesti vrste vpisa 98.
        boolean invalidStudyType = es.getEnrolmentToken().getType().getId() == 98;
        if(invalidStudyType)
            list.add("Invalid study type (study type must not be \"98 Zaključek\")");

        // Preveri, da so avtomatsko dodani obvezni predmeti.

        // Preveri, da vpis še ni potrjen.

        // Preveri seznam vpisanih.*

        // Preveri izpis vpisnega lista.*

        return list;
    }
}

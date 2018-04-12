package beans.logic;

import beans.crud.*;
import entities.*;
import entities.logic.EnrolmentSheet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

import static org.eclipse.persistence.config.TargetDatabase.Database;

@ApplicationScoped
public class EnrolmentPolicyBean {

    @Inject
    private GradeBean GradeBean;

    @Inject
    private CurriculumBean cb;

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
    private EnrolmentTokenBean enrolmentTokenBean;

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

    public List<String> checkEnrolment(EnrolmentSheet es, EnrolmentToken enToken){
        List<String> list = new ArrayList<>();
        if(enToken == null) {
            list.add("No token found");
            return list;
        }
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
        String EMSO = date.split("-")[2].substring(0, 2)+date.split("-")[1]+date.split("-")[0].substring(1,4)+(es.getStudent().getGender() == 'M' ? "50[0-4][0-9]{3}" : "505[5-9][0-9]{3}");
        if(!es.getStudent().getEmso().matches(EMSO)) {
            list.add("Invalid EMSO number");
        }

        if(!es.getStudent().getName().matches("[A-Ž][a-ž]+")) {
            list.add("Invalid name format");
        }
        if(!es.getStudent().getSurname().matches("[A-Ž][a-ž]+")) {
            list.add("Invalid surname format");
        }

        if(municipalityBean.existsMunicipality(es.getStudent().getMunicipalityOfBirth().getId()) && !es.getStudent().getCountryOfBirth().getName().equals("Slovenija")) {
            list.add("Invalid country and municipality combination");
        }

        List<Enrolment> studentEnrolments = enrolmentBean.getEnrolmentsForStudent(es.getStudent().getId());
        if(studentEnrolments.isEmpty() && es.getEnrolmentToken().getYear() != 1) {
            list.add("Invalid year for first enrolment for this student");
        } else {
            Enrolment maxEnrolment = Collections.max(studentEnrolments, new Comparator<Enrolment>() {
                @Override
                public int compare(Enrolment o1, Enrolment o2) {
                    return Integer.compare(o1.getYear(), o2.getYear());
                }
            });
            if(maxEnrolment.getYear() + 1 < es.getEnrolmentToken().getYear() ||  maxEnrolment.getYear() > es.getEnrolmentToken().getYear()) {
                list.add("Invalid year for enrolment for this student");
            }
        }


        return list;
    }
}

package beans.logic;

import beans.crud.*;
import entities.*;
import entities.logic.EnrolmentSheet;

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

    @Inject
    private EnrolmentBean eb;

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
    private StudyDegreeBean studyDegreeBean;

    @Inject
    private StudyFormBean studyFormBean;

    @Inject
    private StudyKindBean studyKindBean;

    public boolean hasStudentFreeChoiceOfCurriculum(Student s){
        if(eb.getEnrolmentsForStudent(s.getId()).isEmpty())
            return false;

        Iterator<Enrolment> iters = eb.getEnrolmentsForStudent(s.getId()).iterator();
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

    public List<String> validCodes(EnrolmentSheet es){
        List<String> list = new ArrayList<String>();
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
        if(!studyTypeBean.existsStudyType(es.getEnrolmentToken().getType().getId())) {
            list.add("Invalid study type code");
        }
        year >= 1 && year <= 3
        return true;
    }
}

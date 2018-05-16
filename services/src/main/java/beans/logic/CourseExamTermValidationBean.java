package beans.logic;

import beans.crud.CourseExamTermBean;
import beans.crud.ExamSignUpBean;
import entities.curriculum.CourseExamTerm;
import entities.curriculum.ExamSignUp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CourseExamTermValidationBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject private CourseExamTermBean cetb;
    @Inject private ExamSignUpBean esub;

    private List<String> checkIfExamTermHasGradesEntered(CourseExamTerm cet, List<String> errList) {
        List<ExamSignUp> signUps = esub.getExamSignUpsByExamTerm(cet.getId());

        for(ExamSignUp esu: signUps)
            // if we find someone who has grade already entered, changing/deleting the exam term becomes impossible
            if(esu.getWrittenScore() != null || esu.getSuggestedGrade() != null) {
                errList.add("nekdo izmed vpisanih že ima vpisano oceno izpita ali predlagano končno oceno");
                return errList;
            }

        return errList;
    }

    private List<String> validateDate(CourseExamTerm cet, List<String> errList) {
        Date enteredDatetime = cet.getDatetimeObject();
        Date currDate = new Date();

        // date of exam needs to be in the future
        if(enteredDatetime.before(currDate))
            errList.add("datum izpita je že mimo");

        // bonus: date between 2 consecutive exams for the same course organization needs to be at least 7 days apart
        List<CourseExamTerm> examTerms = cetb.getExamTermsByCourse(cet.getCourseOrganization().getId());
        if(examTerms != null) {
            Integer idExamTerm = cet.getId();

            for (CourseExamTerm term: examTerms) {
                // when updating an entity, skip the check with the same entity (before it was updated)
                if(term.getId().equals(idExamTerm)) {
                    continue;
                }

                /* skip the check with an exam term that is not of same type (i.e. only compare oral exams with oral exams
                and written with written exams.*/
                if(!term.getType().equals(cet.getType()))
                    continue;

                log.info(String.format("Term date: %s", term.getDatetime()));
                long timeDiff = enteredDatetime.getTime() - term.getDatetimeObject().getTime();
                int daysDiff = (int) (timeDiff / (1000 * 60 * 60 * 24));
                log.info(String.format("Days diff is %d", daysDiff));

                if (daysDiff < 7 && daysDiff > -7) {
                    errList.add(String.format("med datumom dveh izpitov pri istem predmetu mora biti vsaj 7 dni razlike",
                            daysDiff));
                    break;
                }
            }
        }
        return errList;
    }

    // caller method
    public List<String> validateExamTermData(CourseExamTerm cet) {
        List<String> errors = new ArrayList<>();

        errors = validateDate(cet, errors);
        errors = checkIfExamTermHasGradesEntered(cet, errors);

        return errors;
    }
}

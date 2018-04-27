package beans.logic;

import beans.crud.CourseExamTermBean;
import entities.curriculum.CourseExamTerm;

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

    private List<String> validateDate(CourseExamTerm cet, List<String> errList) {
        Date enteredDatetime = cet.getDatetimeObject();
        Date currDate = new Date();

        // date of exam needs to be in the future
        if(enteredDatetime.before(currDate))
            errList.add("datum izpita je že mimo");

        // bonus: date between 2 consecutive exams for the same course organization needs to be at least 7 days apart
        List<CourseExamTerm> examTerms = cetb.getExamTermsByCourse(cet.getCourse().getId());
        if(examTerms != null) {
            Integer idCourseOrganization = cet.getId();

            for (CourseExamTerm term : examTerms) {
                // when updating an entity, skip the check with the same entity (before it was updated)
                if(term.getId() == idCourseOrganization)
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

        return errors;
    }
}

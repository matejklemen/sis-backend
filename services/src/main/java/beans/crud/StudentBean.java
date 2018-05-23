package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.interfaces.CriteriaFilter;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.Enrolment;
import entities.Student;
import entities.curriculum.Course;
import entities.curriculum.StudentCourses;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<Student> getStudents(QueryParameters query) {
        return JPAUtils.queryEntities(em, Student.class, query);
    }

    @Transactional
    public Student getStudent(int id) {
        Student s = em.find(Student.class, id);
        if(s == null) {
            log.info("Student is apparently null...");
            throw new NoResultException("No student by this id");
        }
        return s;
    }

    public Student getStudentByRegisterNumber(String regNumber) {
        TypedQuery<Student> q = em.createQuery("SELECT s FROM student s WHERE s.registerNumber = :reg_number", Student.class);
        q.setParameter("reg_number", regNumber);

        List<Student> res = q.getResultList();

        if(res != null && !res.isEmpty())
            return res.get(0);

        return null;
    }

    @Transactional
    public boolean existsStudent(int id) {
        return em.find(Student.class, id) != null;
    }

    @Transactional
    public Student insertStudent(Student st) {
        log.info("Inserting: " + st.toString());
        em.persist(st);
        em.flush();
        return st;
    }

    @Transactional
    public Student updateStudent(Student st) {
        log.info("Updating: " + st.toString());
        em.merge(st);
        em.flush();
        return st;
    }

    @Transactional
    public List searchStudents(QueryParameters paramQuery, String searchQuery) {
        List<Student> queryResult = JPAUtils.queryEntities(em, Student.class, paramQuery, new CriteriaFilter<Student>() {
            @Override
            public Predicate createPredicate(Predicate predicate, CriteriaBuilder cBuilder, Root<Student> root) {
                // build LIKE statements
                String searchQueryL = searchQuery.toLowerCase() + "%";
                Predicate[] likes = {
                        cBuilder.like(cBuilder.lower(root.get("name")), searchQueryL),
                        cBuilder.like(cBuilder.lower(root.get("surname")), searchQueryL),
                        cBuilder.like(cBuilder.lower(root.get("registerNumber")), searchQueryL),
                };
                // join LIKE statements with OR and add them to existing criterias with AND
                return cBuilder.and(predicate, cBuilder.or(likes));
            }
        });
        return queryResult;
    }

    @Transactional
    public Student getLastStudent(){
        log.info("Getting last student from table");
        TypedQuery<Student> q = em.createNamedQuery("Student.getAllReverse", Student.class);
        q.setMaxResults(1);
        return q.getSingleResult();
    }

    @Transactional
    public Student getStudentByLoginId(int loginId) {
        try{
            Query q = em.createNamedQuery("Student.getByLoginId", Student.class);
            q.setParameter("loginId", loginId);
            return (Student) q.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Transactional
    public boolean hasStudent(Student stu) {
        Query q = em.createNamedQuery("Student.getStudent", Student.class)
                .setParameter("name", stu.getName())
                .setParameter("surname", stu.getSurname())
                .setParameter("studyProgram", stu.getStudyProgram())
                .setParameter("email", stu.getEmail());

        return q.getResultList().size() > 0;
    }

    @Transactional
    public List<Student> getStudentsByCourse(QueryParameters queryParameters, int courseId, int studyYearId, String studyProgramId, Integer year) {
        List<Student> queryResult = JPAUtils.queryEntities(em, Student.class, queryParameters, new CriteriaFilter<Student>() {
            @Override
            public Predicate createPredicate(Predicate predicate, CriteriaBuilder cBuilder, Root<Student> root) {

                CriteriaQuery cq = cBuilder.createQuery();
                Root rootEnrolment = cq.from(Enrolment.class);
                Root rootStudentCourses = cq.from(StudentCourses.class);
                Root rootCourse = cq.from(Course.class);

                Predicate[] keys = {
                        cBuilder.equal(root.get("id"), rootEnrolment.get("student").get("id")), // student.id = enrolment.student.id
                        cBuilder.equal(rootEnrolment.get("id"), rootStudentCourses.get("enrolment").get("id")), // enrolment.id = studentcourses.enrolment.id
                        cBuilder.equal(rootStudentCourses.get("course").get("id"), rootCourse.get("id")), // studentcourses.course.id = course.id
                        cBuilder.equal(rootEnrolment.get("confirmed"), true) // enrolment must be confirmed
                };

                Predicate coursePredicate = cBuilder.equal(rootCourse.get("id"), courseId);
                Predicate studyYearPredicate = cBuilder.equal(rootEnrolment.get("studyYear").get("id"), studyYearId);

                Predicate studyProgramPredicate, yearPredicate;
                if(studyProgramId != null && year != null) {
                    studyProgramPredicate = cBuilder.equal(rootEnrolment.get("studyProgram").get("id"), studyProgramId);
                    yearPredicate = cBuilder.equal(rootEnrolment.get("year"), year);
                    return cBuilder.and(predicate, cBuilder.and(keys), coursePredicate, studyYearPredicate, studyProgramPredicate, yearPredicate);
                } else if(studyProgramId != null && year == null) {
                    studyProgramPredicate = cBuilder.equal(rootEnrolment.get("studyProgram").get("id"), studyProgramId);
                    return cBuilder.and(predicate, cBuilder.and(keys), coursePredicate, studyYearPredicate, studyProgramPredicate);
                } else if(studyProgramId == null && year != null) {
                    yearPredicate = cBuilder.equal(rootEnrolment.get("year"), year);
                    return cBuilder.and(predicate, cBuilder.and(keys), coursePredicate, studyYearPredicate, yearPredicate);
                } else {
                    return cBuilder.and(predicate, cBuilder.and(keys), coursePredicate, studyYearPredicate);
                }
            }
        });
        return queryResult;
    }
}

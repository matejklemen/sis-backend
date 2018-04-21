package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.interfaces.CriteriaFilter;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
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
    public Student putStudent(Student st){
        log.info("Putting: " + st.toString());
        em.persist(st);
        em.flush();
        return st;
    }

    @Transactional
    public Student updateStudent(Student st){
        log.info("Updating: " + st.toString());
        em.merge(st);
        em.flush();
        return st;
    }

    @Transactional
    public List<Student> getStudents(QueryParameters query) {
        List<Student> students = JPAUtils.queryEntities(em, Student.class, query);
        return students;
    }

    @Transactional
    public Student getStudentById(int id) {
        log.info("Getting by id: " + id);
        Student sd = em.find(Student.class, id);
        if(sd == null) throw new NoResultException("No student by this id");
        return sd;
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
        try{
            Query q = em.createNamedQuery("Student.getStudent", Student.class)
                    .setParameter("name", stu.getName())
                    .setParameter("surname", stu.getSurname())
                    .setParameter("studyProgram", stu.getStudyProgram())
                    .setParameter("email", stu.getEmail());

            q.getSingleResult();
            return true;
        } catch(NoResultException e) {
            return false;
        }
    }
}

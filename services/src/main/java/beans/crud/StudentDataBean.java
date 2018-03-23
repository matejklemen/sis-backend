package beans.crud;

import entities.StudentData;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
public class StudentDataBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public StudentData putStudent(StudentData st){
        em.persist(st);
        em.flush();
        return st;
    }

}

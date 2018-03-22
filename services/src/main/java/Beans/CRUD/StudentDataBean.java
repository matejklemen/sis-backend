package Beans.CRUD;

import entities.StudentData;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentDataBean {

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        log.info("Inicializacija " + getClass().getSimpleName());
    }

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Transactional
    public StudentData putStudent(StudentData st){
        log.info("bom persistal: "+st.toString());
        em.persist(st);
        em.flush();
        return st;
    }

}

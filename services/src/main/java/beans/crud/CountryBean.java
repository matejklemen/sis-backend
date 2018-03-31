package beans.crud;

import entities.Country;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CountryBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<Country> getCountries() {
        TypedQuery<Country> q = em.createNamedQuery("Country.getAll", Country.class);
        //q.setMaxResults(100);
        return q.getResultList();
    }

    @Transactional
    public Country getCountry(int id) {
        log.info("Getting by id: " + id);
        Country c = em.find(Country.class, id);
        if(c == null) throw new NoResultException("No country by this id");
        return c;
    }

}

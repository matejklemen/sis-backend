package beans.crud;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.address.Country;
import utils.SearchAllCriteriaFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CountryBean {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @PersistenceContext(unitName = "sis-jpa")
    private EntityManager em;

    @Transactional
    public List<Country> getCountries(QueryParameters query) {
        return JPAUtils.queryEntities(em, Country.class, query);
    }

    @Transactional
    public List<Country> getCountries(QueryParameters query, String searchQuery) {
        if(searchQuery == null) return getCountries(query);
        return JPAUtils.queryEntities(em, Country.class, query, new SearchAllCriteriaFilter<>(searchQuery));
    }

    @Transactional
    public Country getCountry(int id) {
        Country c = em.find(Country.class, id);
        if(c == null) throw new NoResultException("No country by this id");
        return c;
    }

    @Transactional
    public boolean existsCountry(int id) {
        return em.find(Country.class, id) != null;
    }

    @Transactional
    public Country insertCountry(Country c) {
        em.persist(c);
        em.flush();
        return c;
    }

    @Transactional
    public void deleteCountry(int id) {
        Country c = em.find(Country.class, id);
        if(c != null) {
            c.setDeleted(!c.getDeleted());
            em.merge(c);
        } else {
            throw new NoResultException("Course by ID doesn't exist");
        }
    }

    @Transactional
    public Country updateCountry(Country c) {
        em.merge(c);
        em.flush();
        return c;
    }

}

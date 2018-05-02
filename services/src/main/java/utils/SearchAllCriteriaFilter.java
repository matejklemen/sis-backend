package utils;

import com.kumuluz.ee.rest.interfaces.CriteriaFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

// Eno Java diplomo prosim

public class SearchAllCriteriaFilter<T> implements CriteriaFilter<T> {

    private String searchQuery;

    public SearchAllCriteriaFilter(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public Predicate createPredicate(Predicate predicate, CriteriaBuilder criteriaBuilder, Root root) {
        Root<T> typedRoot = root;

        String searchQueryL = "%" + searchQuery.toLowerCase() + "%";

        // build LIKE statements for every attribute
        Set<Attribute<? super T, ?>> attrs = typedRoot.getModel().getAttributes();
        List<Predicate> likes = new ArrayList<>(attrs.size());

        for(Attribute<? super T, ?> attr : attrs) {

            if(attr.getJavaType() == String.class) {
                likes.add(criteriaBuilder.like(criteriaBuilder.lower(typedRoot.get(attr.getName())), searchQueryL));
            }
            else if(attr.getJavaType() == int.class) {
                try {
                    int searchQueryInt = Integer.parseInt(searchQuery);
                    likes.add(criteriaBuilder.equal(root.get(attr.getName()), searchQueryInt));
                } catch (NumberFormatException ignored) {}
            }

        }

        Predicate[] likesArray = likes.toArray(new Predicate[0]);
        return criteriaBuilder.and(predicate, criteriaBuilder.or(likesArray));
    }
}

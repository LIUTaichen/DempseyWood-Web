package com.dempseywood.specification;

import com.dempseywood.model.Haul;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.Date;

public class HaulSpecs {

    public static Specification<Haul> isBefore(Date toDate) {
        return new Specification<Haul>() {
            public Predicate toPredicate(Root<Haul> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                ///builder.lessThanOrEqualTo(root.get(_Haul), toDate);
                return builder.lessThanOrEqualTo(root.get(""), toDate);

            }
        };
    }

/*    public static Specification<Haul> isAfter() {
        return new Specification<Customer>() {
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                LocalDate date = new LocalDate().minusYears(2);
                return builder.lessThan(root.get(_Customer.createdAt), date);
            }
        };
    }*/
}

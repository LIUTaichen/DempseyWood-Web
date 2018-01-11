package com.dempseywood.specification;

import com.dempseywood.model.Haul;

import com.dempseywood.model.Haul_;
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
                return builder.lessThan(root.get(Haul_.unloadTime), toDate);

            }
        };
    }

    public static Specification<Haul> isAfter(Date fromDate) {
        return new Specification<Haul>() {
            public Predicate toPredicate(Root<Haul> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                return builder.greaterThan(root.get(Haul_.loadTime), fromDate);
            }
        };
    }

    public static Specification<Haul> hasImei(String imei) {
        return new Specification<Haul>() {
            public Predicate toPredicate(Root<Haul> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                return builder.equal(root.get(Haul_.imei), imei);
            }
        };
    }
}

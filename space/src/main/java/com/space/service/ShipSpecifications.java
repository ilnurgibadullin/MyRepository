package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ShipSpecifications {
    public static Specification<Ship> findAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                                              Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                                              Double maxRating) {
        return (Specification<Ship>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%"+name+"%")));
            }
            if (planet != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("planet"), "%"+planet+"%")));
            }
            if (shipType != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("shipType"), shipType)));
            }
            if (after != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("prodDate").as(Date.class), new Date(after))));
            }
            if (before != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("prodDate").as(Date.class), new Date(before))));
            }
            if (isUsed != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isUsed"), isUsed)));
            }
            if (minSpeed != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed)));
            }
            if (maxSpeed != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed)));
            }
            if (minCrewSize != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize)));
            }
            if (maxCrewSize != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize)));
            }
            if (minRating != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating)));
            }
            if (maxRating != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating)));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

package com.ticketflow.eventmanager.event.repository.specification;

import com.ticketflow.eventmanager.event.controller.filter.CategoryFilter;
import com.ticketflow.eventmanager.event.model.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {

    public static Specification<Category> withFilter(CategoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (filter.getName() != null) {
                predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }

            if (filter.getDescription() != null) {
                predicates.add(cb.like(root.get("description"), "%" + filter.getDescription() + "%"));
            }

            if (filter.getAgeGroup() != null) {
                predicates.add(cb.equal(root.get("ageGroup"), filter.getAgeGroup()));
            }

            if (filter.getOwner() != null) {
                predicates.add(cb.equal(root.get("owner"), filter.getOwner()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

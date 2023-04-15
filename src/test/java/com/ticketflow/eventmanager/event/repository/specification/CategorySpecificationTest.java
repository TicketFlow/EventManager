package com.ticketflow.eventmanager.event.repository.specification;

import com.ticketflow.eventmanager.event.controller.filter.CategoryFilter;
import com.ticketflow.eventmanager.event.model.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategorySpecificationTest {

    @Test
    void testIdFilter() {
        CategoryFilter filter = new CategoryFilter();
        filter.setId(1L);

        Specification<Category> spec = CategorySpecification.withFilter(filter);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Category> query = mock(CriteriaQuery.class);
        Root<Category> root = mock(Root.class);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(root.get("id"), 1L)).thenReturn(predicate);

        spec.toPredicate(root, query, cb);

        verify(cb).equal(root.get("id"), 1L);
        verify(cb).and(predicate);
    }

    @Test
    void testNameFilter() {
        CategoryFilter filter = new CategoryFilter();
        filter.setName("categoryName");

        Specification<Category> spec = CategorySpecification.withFilter(filter);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Category> query = mock(CriteriaQuery.class);
        Root<Category> root = mock(Root.class);

        Predicate predicate = mock(Predicate.class);
        when(cb.like(root.get("name"), "%categoryName%")).thenReturn(predicate);

        spec.toPredicate(root, query, cb);

        verify(cb).like(root.get("name"), "%categoryName%");
        verify(cb).and(predicate);
    }

    @Test
    void testDescriptionFilter() {
        CategoryFilter filter = new CategoryFilter();
        filter.setDescription("category description");

        Specification<Category> spec = CategorySpecification.withFilter(filter);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Category> query = mock(CriteriaQuery.class);
        Root<Category> root = mock(Root.class);

        Predicate predicate = mock(Predicate.class);
        when(cb.like(root.get("description"), "%category description%")).thenReturn(predicate);

        spec.toPredicate(root, query, cb);

        verify(cb).like(root.get("name"), "%category description%");
        verify(cb).and(predicate);
    }

    @Test
    void testAgeGroupFilter() {
        String ageGroup = "Livre";
        CategoryFilter filter = new CategoryFilter();
        filter.setAgeGroup(ageGroup);

        Specification<Category> spec = CategorySpecification.withFilter(filter);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Category> query = mock(CriteriaQuery.class);
        Root<Category> root = mock(Root.class);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(root.get("ageGroup"), ageGroup)).thenReturn(predicate);

        spec.toPredicate(root, query, cb);

        verify(cb).equal(root.get("ageGroup"), ageGroup);
        verify(cb).and(predicate);
    }

    @Test
    void testOwnerFilter() {
        String ownerId = "abc123";
        CategoryFilter filter = new CategoryFilter();
        filter.setOwner(ownerId);

        Specification<Category> spec = CategorySpecification.withFilter(filter);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Category> query = mock(CriteriaQuery.class);
        Root<Category> root = mock(Root.class);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(root.get("owner"), ownerId)).thenReturn(predicate);

        spec.toPredicate(root, query, cb);

        verify(cb).equal(root.get("owner"), ownerId);
        verify(cb).and(predicate);
    }

}
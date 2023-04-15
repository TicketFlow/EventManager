package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.controller.filter.CategoryFilter;
import com.ticketflow.eventmanager.event.model.Category;

public class CategoryTestBuilder {

    private static final Long ID = 1L;

    private static final String NAME = "Example Name";

    private static final String DESCRIPTION = "Example Description";

    private static final String AGE_GROUP = "Example Age Group";
    private static final String owner = "1";


    public static Category createDefaultCategory() {
        return buildModelWithDefaultValues()
                .build();
    }

    public static CategoryDTO createDefaultCategoryDTO() {
        return buildDTOWithDefaultValues()
                .build();
    }

    public static Category.CategoryBuilder buildModelWithDefaultValues() {
        return Category.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .owner(owner)
                .ageGroup(AGE_GROUP);
    }

    public static CategoryDTO.CategoryDTOBuilder buildDTOWithDefaultValues() {
        return CategoryDTO.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .owner(owner)
                .ageGroup(AGE_GROUP);
    }

    public static CategoryFilter.CategoryFilterBuilder buildCategoryFilterWithDefaultValues() {
        return CategoryFilter.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .owner(owner)
                .ageGroup(AGE_GROUP);

    }

}

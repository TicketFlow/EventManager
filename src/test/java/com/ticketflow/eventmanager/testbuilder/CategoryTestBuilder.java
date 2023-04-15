package com.ticketflow.eventmanager.testbuilder;

import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.model.Category;

public class CategoryTestBuilder {

    private static final Long ID = 1L;

    private static final String NAME = "Example Name";

    private static final String DESCRIPTION = "Example Description";

    private static final String AGE_GROUP = "Example Age Group";
    private static final String owner = "1";

    public static CategoryTestBuilder init() {
        return new CategoryTestBuilder();
    }

    public static Category createDefaultCategory() {
        return init()
                .buildModelWithDefaultValues()
                .build();
    }

    public static CategoryDTO createDefaultCategoryDTO() {
        return init()
                .buildDTOWithDefaultValues()
                .build();
    }

    public Category.CategoryBuilder buildModelWithDefaultValues() {
        return Category.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .owner(owner)
                .ageGroup(AGE_GROUP);
    }

    public CategoryDTO.CategoryDTOBuilder buildDTOWithDefaultValues() {
        return CategoryDTO.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .owner(owner)
                .ageGroup(AGE_GROUP);
    }

}

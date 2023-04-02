package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.exception.CategoryException;
import com.ticketflow.eventmanager.event.exception.NotFoundException;
import com.ticketflow.eventmanager.event.exception.util.CategoryErrorCode;
import com.ticketflow.eventmanager.event.model.Category;
import com.ticketflow.eventmanager.event.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Qualifier("modelMapperConfig")
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        validateCategoryCreate(categoryDTO);
        Category category = toModel(categoryDTO);
        Category categorySaved = categoryRepository.save(category);

        return toDTO(categorySaved);
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        validateCategoryUpdate(categoryDTO);

        Category category = findById(categoryDTO.getId());

        updateCategoryFields(categoryDTO, category);

        Category updatedCategory = categoryRepository.save(category);

        return toDTO(updatedCategory);
    }

    private void updateCategoryFields(CategoryDTO categoryDTO, Category category) {
        updateCategoryFieldIfNotNullOrEmpty(categoryDTO.getAgeGroup(), category::setAgeGroup);
        updateCategoryFieldIfNotNullOrEmpty(categoryDTO.getDescription(), category::setDescription);
        updateCategoryFieldIfNotNullOrEmpty(categoryDTO.getName(), category::setName);
    }

    private void validateCategoryUpdate(CategoryDTO categoryDTO) {
        validateCategoryId(categoryDTO.getId());
        checkIfCategoryNameIsUnique(categoryDTO);
    }

    private void validateCategoryCreate(CategoryDTO categoryDTO) {
        checkIfCategoryNameIsUnique(categoryDTO);
    }

    private void checkIfCategoryNameIsUnique(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findByName(categoryDTO.getName());
        if (category != null && !category.getId().equals(categoryDTO.getId())) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_NAME_DUPLICATED.withParams(categoryDTO.getName()));
        }
    }

    private void validateCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_ID_REQUIRED.withParams());
        }
    }

    private void updateCategoryFieldIfNotNullOrEmpty(String fieldValue, Consumer<String> fieldSetter) {
        if (fieldValue != null && !fieldValue.isEmpty()) {
            fieldSetter.accept(fieldValue);
        }
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND.withParams(id)));
    }

    private Category toModel(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    private CategoryDTO toDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}

package com.ticketflow.eventmanager.event.service;

import com.ticketflow.eventmanager.event.controller.dto.CategoryDTO;
import com.ticketflow.eventmanager.event.exception.CategoryException;
import com.ticketflow.eventmanager.event.exception.NotFoundException;
import com.ticketflow.eventmanager.event.exception.util.CategoryErrorCode;
import com.ticketflow.eventmanager.event.model.Category;
import com.ticketflow.eventmanager.event.model.Event;
import com.ticketflow.eventmanager.event.repository.CategoryRepository;
import com.ticketflow.eventmanager.event.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final UserService userService;

    @Qualifier("modelMapperConfig")
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, EventRepository eventRepository, UserService authenticatedUserService, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.userService = authenticatedUserService;
        this.modelMapper = modelMapper;
    }

    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Criando uma nova categoria {}", categoryDTO.getName());
        validateCategoryCreate(categoryDTO);
        categoryDTO.setOwner(userService.getCurrentUserId());

        Category category = toModel(categoryDTO);
        return saveAndConvertToDTO(category);
    }


    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        log.info("Atualizando a categoria {}", categoryDTO.getId());
        validateCategoryUpdate(categoryDTO);

        Category category = findById(categoryDTO.getId());
        updateCategoryFields(categoryDTO, category);

        return saveAndConvertToDTO(category);
    }

    public void deleteCategory(Long categoryId) {
        log.info("Excluindo a categoria {}", categoryId);

        Category category = findById(categoryId);
        checkIfCategoryIdBeingUsed(categoryId);

        categoryRepository.delete(category);
    }

    public void checkIfCategoryIdBeingUsed(Long categoryId) {
        List<Event> events = eventRepository.findAllByCategoryId(categoryId);

        if (!events.isEmpty()) {
            List<Long> eventIds = new ArrayList<>();
            events.forEach(event -> eventIds.add(event.getId()));

            throw new CategoryException(CategoryErrorCode.CATEGORY_BEING_USED.withParams(categoryId, eventIds));
        }
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryErrorCode.CATEGORY_NOT_FOUND.withParams(id)));
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

    private CategoryDTO saveAndConvertToDTO(Category categoryDTO) {
        Category categorySaved = categoryRepository.save(categoryDTO);
        return toDTO(categorySaved);
    }

    private Category toModel(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    private CategoryDTO toDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}

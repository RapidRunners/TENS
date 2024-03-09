package az.code.tensapi.service;

import az.code.tensapi.dto.request.CategoryRequest;
import az.code.tensapi.dto.response.CategoryResponse;
import az.code.tensapi.entity.Category;
import az.code.tensapi.exception.CategoryNotFoundException;
import az.code.tensapi.exception.ErrorCodes;
import az.code.tensapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryResponse> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .collect(Collectors.toList());
    }

    public CategoryResponse findById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCodes.CATEGORY_NOT_FOUND));

        return modelMapper.map(category, CategoryResponse.class);
    }

    public CategoryResponse save(CategoryRequest request) {
        Category category = modelMapper.map(request, Category.class);

        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }


    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCodes.CATEGORY_NOT_FOUND));

        if (categoryRequest.getName() != null) {
            category.setName(categoryRequest.getName());
        }
        if (categoryRequest.getDescription() != null) {
            category.setDescription(categoryRequest.getDescription());
        }

        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory, CategoryResponse.class);
    }

    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCodes.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }
}

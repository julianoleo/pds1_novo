package com.iftm.course.services;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iftm.course.dto.CategoryDTO;
import com.iftm.course.entities.Category;
import com.iftm.course.repositories.CategoryRepository;
import com.iftm.course.services.exceptions.DatabaseException;
import com.iftm.course.services.exceptions.ResourceNotFoundException;
@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	public List<CategoryDTO> findAll() {
		return categoryRepository.findAll().stream().map(e -> new CategoryDTO(e)).collect(Collectors.toList());
	}
	public CategoryDTO findById(Long id) {
		return new CategoryDTO(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		return new CategoryDTO(categoryRepository.save(dto.toEntity()));
	}
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = categoryRepository.getOne(id);
			updateData(entity, dto);
			return new CategoryDTO(categoryRepository.save(entity));
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	private void updateData(Category entity, CategoryDTO dto) {
		entity.setName(dto.getName());
	}
	public void delete(Long id) {
		try {
			categoryRepository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage()); 
		}
	}
}
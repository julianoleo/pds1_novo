package com.iftm.course.services;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.course.dto.CategoryDTO;
import com.iftm.course.dto.ProductCategoriesDTO;
import com.iftm.course.dto.ProductDTO;
import com.iftm.course.entities.Product;
import com.iftm.course.repositories.CategoryRepository;
import com.iftm.course.repositories.ProductRepository;
import com.iftm.course.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	public List<ProductDTO> findAll() {
		return productRepository.findAll().stream().map(e -> new ProductDTO(e)).collect(Collectors.toList());
	}
	public ProductDTO findById(Long id) {
		return new ProductDTO(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}

	@Transactional
	public ProductDTO insert(ProductCategoriesDTO dto) {
		Product entity = dto.toEntity();
		setProductCategories(entity, dto.getCategories());
		return new ProductDTO(productRepository.save(entity));
	}

	private void setProductCategories(Product entity, List<CategoryDTO> categories) {
		entity.getCategories().clear();
		for (CategoryDTO dto : categories) entity.getCategories().add(categoryRepository.getOne(dto.getId()));
	}
} 
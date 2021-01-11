package com.iftm.course.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.iftm.course.dto.ProductCategoriesDTO;
import com.iftm.course.dto.ProductDTO;
import com.iftm.course.services.ProductService;
@RestController
@RequestMapping(value = "/products")
public class ProductResource {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public 	ResponseEntity<List<ProductDTO>> findAll() {
		return ResponseEntity.ok().body(productService.findAll());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		return ResponseEntity.ok().body(productService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductCategoriesDTO dto) {
		ProductDTO newDto = productService.insert(dto);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri())
				.body(newDto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductCategoriesDTO dto) {
		return ResponseEntity.ok().body(productService.update(id, dto));
	}
}
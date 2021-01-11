package com.iftm.course.services;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iftm.course.dto.UserDTO;
import com.iftm.course.dto.UserInsertDTO;
import com.iftm.course.entities.User;
import com.iftm.course.repositories.UserRepository;
import com.iftm.course.services.exceptions.DatabaseException;
import com.iftm.course.services.exceptions.ResourceNotFoundException;
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<UserDTO> findAll() {
		return userRepository.findAll().stream().map(user -> new UserDTO(user)).collect(Collectors.toList());
	}
	
	public UserDTO findById(Long id) {
		return new UserDTO(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		return new UserDTO(userRepository.save(dto.toEntity()));
	}
	
	public void delete(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}
	
	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
			User entity = userRepository.getOne(id);
			updateData(entity, dto);
			return new UserDTO(userRepository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	private void updateData(User entity, UserDTO dto) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
	}
}
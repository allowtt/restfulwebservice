package com.example.restfulwebservice.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


@RestController
@RequestMapping("/admin")
public class AdminUserController {
	
	private UserDaoService service;
	
	public AdminUserController(UserDaoService service) {
		this.service = service;
	}

	@GetMapping("/users")
	public MappingJacksonValue retrieveAllUsers() {
		List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
        		.filterOutAllExcept("id", "name", "joinDate", "password");
//        		.filterOutAllExcept("id", "name", "password", "ssn");
        
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);
        
		return mapping;
	}
	
	// GET /admin/users1 -> /admin/v1/users/1
//	@GetMapping(value="/users/{id}/", params = "version=1")
//	@GetMapping(value = "/users/{id}", headers="X-API-VERSION=1")
	@GetMapping(value="/users/{id}", produces = "application/vnd.company.appv1+json")
	public MappingJacksonValue retrieveUser(@PathVariable int id) {
		User user = service.findOne(id);
		
		if(user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}
		
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
//        		.filterOutAllExcept("id", "name", "joinDate", "ssn");
        		.filterOutAllExcept("id", "name", "password", "ssn");
        
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        
		return mapping;
	}
	
	// GET /admin/users1 -> /admin/v1/users/1
//	@GetMapping(value="/users/{id}/", params = "version=2")	
//	@GetMapping(value = "/users/{id}", headers="X-API-VERSION=2")
	@GetMapping(value="/users/{id}", produces = "application/vnd.company.appv2+json")
	public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
		User user = service.findOne(id);
		
		if(user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}
		
		//User -> User2
		UserV2 userV2 = new UserV2();
		BeanUtils.copyProperties(user, userV2);
		userV2.setGrade("VIP");
		
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
//        		.filterOutAllExcept("id", "name", "joinDate", "ssn");
				.filterOutAllExcept("id", "name", "joinDate", "grade");
		
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(userV2);
		mapping.setFilters(filters);
		
		return mapping;
	}	
	// GET /admin/users1 -> /admin/v1/users/1
//	@GetMapping("/v2/users/{id}")	
//	public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
//		User user = service.findOne(id);
//		
//		if(user == null) {
//			throw new UserNotFoundException(String.format("ID[%s] not found", id));
//		}
//		
//		//User -> User2
//		UserV2 userV2 = new UserV2();
//		BeanUtils.copyProperties(user, userV2);
//		userV2.setGrade("VIP");
//		
//		
//        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
////        		.filterOutAllExcept("id", "name", "joinDate", "ssn");
//        		.filterOutAllExcept("id", "name", "joinDate", "grade");
//        
//        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);
//        
//        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
//        mapping.setFilters(filters);
//        
//		return mapping;
//	}	
	
	@PostMapping("/users")
	public ResponseEntity<User> creatUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable int id) {
		
        User updateUser = service.updateById(id, user);

        if (updateUser == null) {

            throw  new UserNotFoundException(String.format("ID[%s] is not Found", id));

        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()

                .path("/{id}")

                .buildAndExpand(updateUser.getId())

                .toUri();

        return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User user = service.deleteById(id);
		
		if(user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}
		
	}

}

package com.casestudy.service;


import com.casestudy.dto.RoleDto;
import com.casestudy.dto.UserDto;
import com.casestudy.exception.UpdateNotAllowedException;
import com.casestudy.proxy.ProxyClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.casestudy.entity.User;
import com.casestudy.exception.UserNotFoundException;
import com.casestudy.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProxyClient client;

    @Autowired
    private HandleUserSecurity handleUserSecurity;

    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        allUsers.forEach(user -> user.setRole(insertRole(user.getEmail())));
        return allUsers;
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }


    public UserDto updateUser(Long id, User updatedUser, HttpServletRequest request) throws AccessDeniedException {
        String authenticatedEmail = getAuthenticatedUserEmail(request);
        log.info("Authenticated email: {}",authenticatedEmail);

        User user = getUserById(id);
        log.info("Email get by id: {}",user.getEmail());

        if (!user.getEmail().equals(authenticatedEmail)) {
            throw new UpdateNotAllowedException("You are not allowed to update this user's details.");
        }

        UserDto dto = new UserDto();

        if (updatedUser != null) {
            if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
                user.setName(updatedUser.getName());
                dto.setName(updatedUser.getName());
            }

            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                user.setEmail(updatedUser.getEmail());
                dto.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getGender() != null && !updatedUser.getGender().isEmpty()) {
                user.setGender(updatedUser.getGender());
                dto.setGender(updatedUser.getGender());
            }

            if (updatedUser.getAge() > 0 && updatedUser.getAge() >= 18) {
                user.setAge(updatedUser.getAge());
                dto.setAge(updatedUser.getAge());
            }

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(updatedUser.getPassword());
            }

            if (updatedUser.getNumber() != null && updatedUser.getNumber().length() == 10) {
                user.setNumber(updatedUser.getNumber());
                dto.setNumber(updatedUser.getNumber());
            }

            String updatedRoles = insertRole(authenticatedEmail);
            user.setRole(updatedRoles);
            dto.setRole(updatedRoles);
            log.info("Updated Role: {}", updatedRoles);
        }

        userRepository.save(user);
        return dto;
    }


    public void deleteUser(Long id) {
        client.deleteUser(id);
    }

	public User getUserByEmail(String email) {
		Optional<User> user =  userRepository.findByEmail(email);
		if(user.isPresent()) {
			return user.get();
		}
		throw new UserNotFoundException("User is not found with this email.");
	}


    public String insertRole(String email){
        User userByEmail = getUserByEmail(email);
        String rolesByEmail = client.getRolesById(email);
        log.info(rolesByEmail);
        userByEmail.setRole(rolesByEmail);
        return rolesByEmail;
    }


    public String getAuthenticatedUserEmail(HttpServletRequest request) throws AccessDeniedException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Header: {}",authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return handleUserSecurity.extractUsername(token);
        }
        throw new AccessDeniedException("Invalid or missing token.");
    }
}


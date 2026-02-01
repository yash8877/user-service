package com.casestudy.service;

import com.casestudy.dto.UserDto;
import com.casestudy.entity.User;
import com.casestudy.exception.UserNotFoundException;
import com.casestudy.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @Mock
    private HandleUserSecurity handleUserSecurity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("pass123");
        user.setRole("USER");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.getUserById(1L);

        assertEquals("John", found.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void testUpdateUser() throws AccessDeniedException {
        // Simulate authenticated user
        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
        authenticatedUser.setEmail("jane@example.com");
        authenticatedUser.setName("Jane");
        authenticatedUser.setRole("USER");

        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("janedoe@example.com");
        updatedUser.setPassword("newpass");
        updatedUser.setRole("ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(authenticatedUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer some-valid-jwt-token");

        when(handleUserSecurity.extractUsername("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6IlJPTEVfVVNFUiIsInN1YiI6ImFhcnRpamFuZ3JhNDgyQGdtYWlsLmNvbSIsImlhdCI6MTc0OTM1OTQwNiwiZXhwIjoxNzQ5NDY3NDA2fQ.yCPFv_2WQSEflfVr_g-Rzwss8JUn_o3wBZxZG8qzEMY")).thenReturn(authenticatedUser.getEmail());
        UserDto result = userService.updateUser(1L, updatedUser, request);

        // Validate the result
        assertEquals("Jane Doe", result.getName());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(2L));
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User found = userService.getUserByEmail("john@example.com");

        assertEquals("John", found.getName());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("notfound@example.com"));
    }
}


package com.casestudy.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.casestudy.entity.User;
import com.casestudy.service.UserService;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAllUsers();
        if (!all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    

    @PutMapping("/update-id/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Long id, @RequestBody User user, HttpServletRequest request) throws AccessDeniedException {
        userService.updateUser(id, user,request);
        return ResponseEntity.ok(Map.of("message", "User updated successfully."));
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/get-roles/{email}")
    public String showRoles(@PathVariable String email){
        return userService.insertRole(email);
    }

}


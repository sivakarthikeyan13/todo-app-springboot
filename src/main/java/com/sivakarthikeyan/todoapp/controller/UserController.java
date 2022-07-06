package com.sivakarthikeyan.todoapp.controller;


import com.sivakarthikeyan.todoapp.domain.User;
import com.sivakarthikeyan.todoapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/api/signin")
    public ResponseEntity<?> fetchUserLogin(@RequestBody User user) {
        logger.info("User-Login api called...");
        User loginResponse = userService.fetchUserLogin(user);
        logger.info("User-Login api response sent successfully...");
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> createNewUser(@RequestBody User user){
        logger.info("User-Register api called...");
        User registerResponse = userService.createNewUser(user);
        logger.info("User-Register api response sent successfully...");
        return new ResponseEntity<User>(registerResponse, HttpStatus.CREATED);
//        return ResponseEntity.ok(registerResponse);
    }


    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Update-User api called...");
        User result = userService.updateUser(id, user);
        logger.info("Update-User api response sent successfully...");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Delete-User api called...");
        userService.deleteUser(id);
        logger.info("Delete-User api response sent successfully...");
        return ResponseEntity.ok(Collections.singletonMap("response","User Deleted"));
    }


}

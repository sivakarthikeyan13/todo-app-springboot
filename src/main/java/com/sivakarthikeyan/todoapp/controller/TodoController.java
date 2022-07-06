package com.sivakarthikeyan.todoapp.controller;

import com.sivakarthikeyan.todoapp.domain.TodoItem;
import com.sivakarthikeyan.todoapp.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    // fetch all todo items from database
    @GetMapping("/api/todoItems/{userId}")
    public ResponseEntity<?> fetchAllTodoItems(@PathVariable Long userId) {
        logger.info("Get-TodoItems api called...");
        List<TodoItem> todoItems = todoService.fetchAllTodoItems(userId);

        logger.info("Get-TodoItems api response sent successfully...");
        return ResponseEntity.ok(todoItems);  // .ok() is short for status(HttpStatus.OK).body()
    }

    @PostMapping("/api/todoItems")
    public ResponseEntity<?> createNewTodoItem(@RequestBody TodoItem todoItem) {
        logger.info("Create-TodoItem api called...");
        TodoItem newTodoItem = todoService.createNewTodoItem(todoItem);

        logger.info("Create-TodoItem api response sent successfully...");
        return ResponseEntity.ok(newTodoItem);
    }

    @PutMapping("/api/todoItems/{id}")
    public ResponseEntity<?> updateTodoItems(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        logger.info("Update-TodoItem api called...");
        //call the service and get data back
        TodoItem updatedTodoItem = todoService.updateTodoItem(id, todoItem);

        //send it to front-end
        logger.info("Update-TodoItem api response sent successfully...");
        return ResponseEntity.ok(updatedTodoItem);
    }

    @DeleteMapping("/api/todoItems/{id}")
    public ResponseEntity<?> deleteTodoItems(@PathVariable Long id) {
        logger.info("Delete-TodoItem api called...");
        //call the service
        todoService.deleteTodoItem(id);

        logger.info("Delete-TodoItem api response sent successfully...");
        return ResponseEntity.ok(Collections.singletonMap("response","Item Deleted"));
    }

    @PostMapping("/api/sendEmail/{userId}")
    public ResponseEntity<?> sendEmail(@PathVariable Long userId, @RequestBody String toEmail) {
        todoService.sendMail(userId, toEmail);
        return ResponseEntity.ok(Collections.singletonMap("response","Mail sent successfully"));
    }

}

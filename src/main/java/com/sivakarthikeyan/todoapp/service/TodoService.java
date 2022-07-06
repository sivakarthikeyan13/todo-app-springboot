package com.sivakarthikeyan.todoapp.service;


import com.sivakarthikeyan.todoapp.domain.TodoItem;
import com.sivakarthikeyan.todoapp.exception.ApiRequestException;
import com.sivakarthikeyan.todoapp.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    Logger logger = LoggerFactory.getLogger(TodoService.class);

    @Autowired
    private TodoRepository todoRepository;

    public List<TodoItem> fetchAllTodoItems(Long userId) {
        List<TodoItem> todoItemList =  todoRepository.findAllByUserIdOrderByDate(userId);
        logger.info("Todo list of user collected successfully...");
        return todoItemList;
    }

    public TodoItem updateTodoItem(Long id, TodoItem todoItem) {

        todoRepository.findById(id).
                orElseThrow(() -> new ApiRequestException("Task id " + id + " is Invalid"));

        todoItem.setId(id);
        logger.info("Todo item with id-" + id + " updated successfully...");
        return todoRepository.save(todoItem);

    }

    public TodoItem createNewTodoItem(TodoItem todoItem) {
        todoRepository.save(todoItem);
        logger.info("new todo item created successfully...");
        return todoItem;
    }

    public void deleteTodoItem(Long id) {
        boolean itemExists = todoRepository.existsById(id);
        if(!itemExists) {
            throw new ApiRequestException("task with id " + id + " does not exists");
        }
        todoRepository.deleteById(id);
        logger.info("Todo item with id-" + id + " deleted successfully...");
    }

    @Autowired
    private EmailSenderService senderService;

    public void sendMail(Long id, String toEmail){

        List<TodoItem> items = todoRepository.findAllByUserIdAndDate(id, LocalDate.now());

		senderService.sendEmail(
				toEmail,
				"Todo App - Today's Tasks",
				items.toString()
		);
    }


}

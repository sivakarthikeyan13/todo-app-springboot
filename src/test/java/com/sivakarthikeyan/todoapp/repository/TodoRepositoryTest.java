package com.sivakarthikeyan.todoapp.repository;

import com.sivakarthikeyan.todoapp.domain.TodoItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findAllByUserIdOrderByDate() {
        //given
        Long userId = 1L;
        TodoItem todoItem1 = new TodoItem(
                userId,
                "testTask1",
                false,
                LocalDate.of(2022, 5, 3)
        );
        TodoItem todoItem2 = new TodoItem(
                userId,
                "testTask2",
                false,
                LocalDate.of(2022, 5, 2)
        );
        underTest.save(todoItem1);
        underTest.save(todoItem2);
        List<TodoItem> todoItemList = List.of(todoItem2, todoItem1);
        //when
        List<TodoItem> expected = underTest.findAllByUserIdOrderByDate(userId);

        //then
        assertThat(expected).isEqualTo(todoItemList);
    }
}
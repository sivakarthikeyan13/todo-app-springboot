package com.sivakarthikeyan.todoapp.service;

import com.sivakarthikeyan.todoapp.domain.TodoItem;
import com.sivakarthikeyan.todoapp.domain.User;
import com.sivakarthikeyan.todoapp.exception.ApiRequestException;
import com.sivakarthikeyan.todoapp.repository.TodoRepository;
import com.sivakarthikeyan.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TodoService(todoRepository);
    }

    @Test
    void canFetchAllTodoItems() {
        //given
        Long userId = 1L;
//        given(todoRepository.findAllByUserId(anyLong()).willReturn());
        //when
        List<TodoItem> items = underTest.fetchAllTodoItems(userId);
        //then
        assertThat(items.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void canUpdateTodoItem() {
        //given
        TodoItem todo = new TodoItem(
                1L,
                10L,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );

        TodoItem newTodo = new TodoItem(
                10L,
                "testTask",
                true,
                LocalDate.of(2022, 5, 3)
        );
        Long id = 1L;
        given(todoRepository.findById(id)).willReturn(Optional.of(todo));
        //when
        underTest.updateTodoItem(id, newTodo);
        //then
        verify(todoRepository).save(newTodo);
        verify(todoRepository).findById(id);
    }

    @Test
    void willThrowWhenUpdateTodoItem() {
        TodoItem todo = new TodoItem(
                1L,
                10L,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );

        TodoItem newTodo = new TodoItem(
                10L,
                "testTask",
                true,
                LocalDate.of(2022, 5, 3)
        );
        Long id = 2L;
        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() ->underTest.updateTodoItem(id,newTodo))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Task id " + id + " is Invalid");

        verify(todoRepository, never()).save(any());

    }

    @Test
    void canCreateNewTodoItem() {
        //given
        TodoItem todo = new TodoItem(
                1L,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        underTest.createNewTodoItem(todo);

        //then
        ArgumentCaptor<TodoItem> userArgumentCaptor = ArgumentCaptor.forClass(TodoItem.class);
        verify(todoRepository).save(userArgumentCaptor.capture());
        TodoItem capturedTodo = userArgumentCaptor.getValue();
        assertThat(capturedTodo).isEqualTo(todo);
    }

    @Test
    void canDeleteTodoItem() {
        //given
        Long id = 1L;
        given(todoRepository.existsById(anyLong())).willReturn(true);

        //when
        underTest.deleteTodoItem(id);

        //then
        verify(todoRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteTodoItem() {
        //given
        Long id = 1L;
        given(todoRepository.existsById(anyLong())).willReturn(false);
        //when
        //then
        assertThatThrownBy(() ->underTest.deleteTodoItem(id))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("task with id " + id + " does not exists");
        verify(todoRepository, never()).deleteById(id);
    }
}
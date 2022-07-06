package com.sivakarthikeyan.todoapp.repository;


import com.sivakarthikeyan.todoapp.domain.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoItem, Long> {

    @Query(value = "SELECT * FROM todo_item WHERE user_id=?1 ORDER BY date", nativeQuery = true)
    List<TodoItem> findAllByUserIdOrderByDate(Long userId);

//    Boolean existsByUserId(Long userId);

    List<TodoItem> findAllByUserIdAndDate(Long userId, LocalDate date);

}

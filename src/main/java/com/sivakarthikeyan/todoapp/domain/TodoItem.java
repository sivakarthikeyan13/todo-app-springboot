package com.sivakarthikeyan.todoapp.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class TodoItem {

    @Id
    @SequenceGenerator(name = "item_sequence", sequenceName = "item_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "item_sequence")
    private Long id;
    private Long userId;
    private String task;
    private Boolean isDone;
    private LocalDate date;

    public TodoItem() {
    }

    public TodoItem(Long id, Long userId, String task, Boolean isDone, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.task = task;
        this.isDone = isDone;
        this.date = date;
    }

    public TodoItem(Long userId, String task, Boolean isDone, LocalDate date) {
        this.userId = userId;
        this.task = task;
        this.isDone = isDone;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public LocalDate getDate() {

        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String isCompleted = (isDone)? "Completed":"Pending";
        return  "\r\n" + task + " - " + isCompleted;
    }
}

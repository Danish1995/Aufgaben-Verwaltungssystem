package com.danish.taskmanager.repository;

import com.danish.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Integer> {
}

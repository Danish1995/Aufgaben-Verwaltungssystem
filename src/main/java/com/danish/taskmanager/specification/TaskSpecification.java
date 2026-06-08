package com.danish.taskmanager.specification;

import com.danish.taskmanager.dto.TaskFilter;
import com.danish.taskmanager.entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {


    public static Specification<Task> withFilters(TaskFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
                predicates.add(cb.equal(root.get("status"),
                        Task.Status.valueOf(filter.getStatus())));
            }

            if (filter.getPriority() != null && !filter.getPriority().isBlank()) {
                predicates.add(cb.equal(root.get("priority"),
                        Task.Priority.valueOf(filter.getPriority())));
            }

            if (filter.getAssignedUserId() != null) {
                predicates.add(cb.equal(
                        root.get("assignedUser").get("id"),
                        filter.getAssignedUserId()));
            }

            if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
                String like = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("description")), like)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
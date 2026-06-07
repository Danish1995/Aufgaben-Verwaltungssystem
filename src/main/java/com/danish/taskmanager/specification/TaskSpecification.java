package com.danish.taskmanager.specification;

import com.danish.taskmanager.dto.TaskFilter;
import com.danish.taskmanager.entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    /* Specification class creates the query conditions (WHERE clause), and Spring Data JPA/Hibernate uses those conditions to generate the actual SQL query.
    * (root, query, cb) are the tools JPA gives  to build those conditions:
    *   root → which entity/table and columns you're querying
        cb (CriteriaBuilder) → creates conditions (=, LIKE, AND, OR)
        query → the overall query being built (often unused in simple cases)
    Without Root, CriteriaBuilder, and CriteriaQuery, a Specification wouldn't know what table, what columns,
    or what conditions to generate. They are the building blocks JPA provides for constructing dynamic queries.

    * */

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
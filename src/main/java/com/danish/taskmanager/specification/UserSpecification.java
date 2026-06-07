package com.danish.taskmanager.specification;

import com.danish.taskmanager.dto.TaskFilter;
import com.danish.taskmanager.dto.UserFilter;
import com.danish.taskmanager.entity.Task;
import com.danish.taskmanager.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    /* Specification class creates the query conditions (WHERE clause), and Spring Data JPA/Hibernate uses those conditions to generate the actual SQL query.
    * (root, query, cb) are the tools JPA gives  to build those conditions:
    *   root → which entity/table and columns you're querying
        cb (CriteriaBuilder) → creates conditions (=, LIKE, AND, OR)
        query → the overall query being built (often unused in simple cases)
    Without Root, CriteriaBuilder, and CriteriaQuery, a Specification wouldn't know what table, what columns,
    or what conditions to generate. They are the building blocks JPA provides for constructing dynamic queries.

    * */

    public static Specification<User> withFilters(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                String like = "%" + filter.getName().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), like));
            }

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                String like = "%" + filter.getEmail().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("email")), like));
            }

            if (filter.getRole() != null && !filter.getRole().isBlank()) {
                predicates.add(cb.equal(root.get("role"),
                        User.Role.valueOf(filter.getRole())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
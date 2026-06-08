## TaskSpecification Builds Dynamic WHERE Clause

Service:

```java
Specification<Task> spec =
        TaskSpecification.withFilters(filter);
```

The filter object is passed to the Specification class.

---

### What is a Specification?

A `Specification<Task>` is a dynamic query object used by Spring Data JPA.

Its job is to build the SQL `WHERE` clause based on the filters provided by the user.

Without a Specification:

```java
taskRepository.findAll();
```

SQL:

```sql
SELECT *
FROM tasks;
```

With a Specification:

```java
taskRepository.findAll(spec, pageable);
```

SQL:

```sql
SELECT *
FROM tasks
WHERE status = 'TODO';
```

---

### Build Predicates

```java
List<Predicate> predicates = new ArrayList<>();
```

A Predicate represents a single condition.

Examples:

```sql
status = 'TODO'
```

```sql
priority = 'HIGH'
```

```sql
title LIKE '%login%'
```

Each filter adds a Predicate to the list.

---

### Status Filter

```java
predicates.add(
    cb.equal(
        root.get("status"),
        Task.Status.valueOf(filter.getStatus())
    )
);
```

Generates:

```sql
status = 'TODO'
```

---

### Priority Filter

```java
predicates.add(
    cb.equal(
        root.get("priority"),
        Task.Priority.valueOf(filter.getPriority())
    )
);
```

Generates:

```sql
priority = 'HIGH'
```

---

### Assigned User Filter

```java
predicates.add(
    cb.equal(
        root.get("assignedUser").get("id"),
        filter.getAssignedUserId()
    )
);
```

Generates:

```sql
assigned_user_id = 5
```

---

### Keyword Search

```java
cb.or(
    cb.like(cb.lower(root.get("title")), like),
    cb.like(cb.lower(root.get("description")), like)
)
```

Generates:

```sql
LOWER(title) LIKE '%login%'
OR LOWER(description) LIKE '%login%'
```

---

### Combine All Conditions

```java
return cb.and(
    predicates.toArray(new Predicate[0])
);
```

All predicates are combined using AND.

Generated SQL:

```sql
WHERE status = 'TODO'
AND priority = 'HIGH'
AND assigned_user_id = 5
AND (
    LOWER(title) LIKE '%login%'
    OR LOWER(description) LIKE '%login%'
)
```

---

### What are root, query, and cb?

```java
(root, query, cb) -> { ... }
```

These are provided automatically by JPA.

#### root

```java
root.get("status")
```

Represents the Task entity and its fields.

Equivalent to:

```sql
tasks.status
```

---

#### cb (CriteriaBuilder)

Used to create query conditions.

Examples:

```java
cb.equal(...)
cb.like(...)
cb.and(...)
cb.or(...)
```

Equivalent SQL:

```sql
=
LIKE
AND
OR
```

---

#### query

Represents the overall SQL query being built.

Usually not needed for simple filtering.

---

### Result

TaskFilter:

```java
status = TODO
priority = HIGH
assignedUserId = 5
keyword = "login"
```

↓

TaskSpecification:

```java
Specification<Task>
```

↓

Repository:

```java
taskRepository.findAll(spec, pageable);
```

↓

Generated SQL:

```sql
SELECT *
FROM tasks
WHERE status = 'TODO'
AND priority = 'HIGH'
AND assigned_user_id = 5
AND (
    LOWER(title) LIKE '%login%'
    OR LOWER(description) LIKE '%login%'
)
```

↓

Filtered and paginated task results returned to the controller.

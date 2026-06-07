# Spring Boot + Thymeleaf Filter Flow

## 1. User Fills Filter Form

```html
<form th:action="@{/api/tasks}" method="get">

    <input name="keyword" />

    <select name="status">
        <option value="TODO">To Do</option>
    </select>

    <button type="submit">Filter</button>

</form>
```

Example user input:

* keyword = `login`
* status = `TODO`
* priority = `HIGH`
* assignedUserId = `5`

---

## 2. User Clicks Filter

```text
[Filter Button]
       ↓
Submit Form
```

Because the form uses:

```html
method="get"
```

the browser automatically creates URL query parameters from all fields that have a `name` attribute.

---

## 3. Browser Builds URL

Generated URL:

```text
/api/tasks?keyword=login
          &status=TODO
          &priority=HIGH
          &assignedUserId=5
```

or on a single line:

```text
/api/tasks?keyword=login&status=TODO&priority=HIGH&assignedUserId=5
```

---

## 4. Spring Receives Request

Controller:

```java
@GetMapping("/api/tasks")
public String getTasks(
        TaskFilter filter,
        Pageable pageable,
        Model model) {

    Page<TaskResponseDTO> tasks =
            taskService.getFilteredTasks(filter, pageable);

    return "tasks";
}
```

Spring automatically reads query parameters and populates the filter object.

Equivalent to:

```java
TaskFilter filter = new TaskFilter();

filter.setKeyword("login");
filter.setStatus(TODO);
filter.setPriority(HIGH);
filter.setAssignedUserId(5L);
```

---

## 5. Service Layer

```java
public Page<TaskResponseDTO> getFilteredTasks(
        TaskFilter filter,
        Pageable pageable) {

    Specification<Task> spec =
            TaskSpecification.withFilters(filter);

    return taskRepository
            .findAll(spec, pageable)
            .map(taskMapper::toDTO);
}
```

### What happens?

#### Build Dynamic Query

```java
Specification<Task> spec =
        TaskSpecification.withFilters(filter);
```

Creates a query based on provided filters.

Example SQL:

```sql
SELECT *
FROM tasks
WHERE status = 'TODO'
AND priority = 'HIGH'
AND title LIKE '%login%'
```

---

#### Execute Query

```java
taskRepository.findAll(spec, pageable);
```

Runs the query against the database.

Pagination is applied automatically.

---

#### Convert Entity → DTO

```java
.map(taskMapper::toDTO)
```

Equivalent to:

```java
.map(task -> taskMapper.toDTO(task))
```

Converts:

```java
Task
```

into:

```java
TaskResponseDTO
```

---

## 6. Controller Returns View

```java
return "tasks";
```

Spring sends data to Thymeleaf.

---

## 7. Thymeleaf Renders Page

Controller:

```java
model.addAttribute("currentKeyword", "login");
model.addAttribute("currentStatus", "TODO");
```

Template:

```html
<input
    name="keyword"
    th:value="${currentKeyword}">
```

renders as:

```html
<input
    name="keyword"
    value="login">
```

---

## 8. Selected Filters Stay Visible

```html
<option value="TODO"
        th:selected="${currentStatus == 'TODO'}">
    To Do
</option>
```

renders as:

```html
<option value="TODO" selected>
    To Do
</option>
```

This keeps the user's selected filters after the page reloads.

---

# Complete Request Flow

```text
User enters values
        ↓
Clicks Filter
        ↓
Browser reads form fields
        ↓
Builds URL query parameters
        ↓
/api/tasks?keyword=login&status=TODO
        ↓
Spring receives request
        ↓
Creates TaskFilter object
        ↓
Calls Service Layer
        ↓
Builds Specification
        ↓
Queries Database
        ↓
Returns Page<TaskResponseDTO>
        ↓
Controller adds model attributes
        ↓
Thymeleaf renders HTML
        ↓
th:value and th:selected restore filter values
        ↓
User sees filtered results
```

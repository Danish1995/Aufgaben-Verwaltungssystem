# MockMvc Notes

## Service Test ( Mokito )
- service logic is REAL
- repository and mapper are mocked

## Controller Test (@WebMvcTest)
- controller is REAL
- services are mocked

mockMvc.perform(get("/tasks/all-tasks"))
simulates a browser request without starting a real server.

Controller flow:
request → controller → mocked service → model → view

1. mockMvc.perform(get("/tasks/all-tasks"))is Equivalent to browser visiting: http://localhost:8080/tasks/all-tasks but without actuall server
2. So Spring searches controller mappings @GetMapping("/all-tasks") in side @RequestMapping("/tasks") cmobines it tasks/all-tasks
3. Controller method runs and return all task list, which we pass as a fake list
4. spring stores list of fake task into model atrtribute tasks

### **do not we need to handle taskService.save(dto);?**

1. like if this is called then.... , like we do in mokito?
2. In this test we call controller endpoint for saving task.
3. Inside controller, taskService.save(dto) is called.
4. But in @WebMvcTest the service layer is FAKE (@MockBean).
5. So no real save logic or database operation happens.
6. Mockito only records that save() method was called.
7. That is why we verify it using:
8. verify(taskService, times(1)).save(any());
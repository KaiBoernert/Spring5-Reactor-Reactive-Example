package de.oio.server.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.oio.server.model.TaskItem;
import de.oio.server.repository.TaskItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskItemServiceImpl implements TaskItemService {
	private static long nextId = 1;

	private TaskItemRepository repository;

	@Autowired
	public TaskItemServiceImpl(TaskItemRepository repository) {
		this.repository = repository;
	}

	@Override
	public Flux<TaskItem> getAll() {
		return repository.findAll();
	}

	@Override
	public Mono<TaskItem> getTask(Long id) {
		return repository.findById(id);
	}

	@Override
	public Mono<TaskItem> createTask(String title, String description, Date dueDate) {
		final TaskItem task = new TaskItem();
		task.setId(nextId++);
		task.setTitle(title);
		task.setDescription(description);
		task.setDueDate(dueDate);
		return repository.save(task);
	}

	@Override
	public Mono<TaskItem> updateTask(Long id, TaskItem task) {
		if (!id.equals(task.getId())) {
			return Mono.error(new IllegalArgumentException("Cannot update a newly created task!"));
		}
		return repository.save(task);
	}

	@Override
	public void finishTask(Long id) {
		getTask(id).subscribe(task -> {
			task.setDone(true);
			//a real application should have validation here
			repository.save(task).subscribe();
		});
	}

}

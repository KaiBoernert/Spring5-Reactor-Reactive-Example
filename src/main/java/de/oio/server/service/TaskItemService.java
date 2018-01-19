package de.oio.server.service;

import java.util.Date;

import de.oio.server.model.TaskItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskItemService {

	Flux<TaskItem> getAll();

	Mono<TaskItem> getTask(Long id);

	Mono<TaskItem> createTask(String title, String description, Date dueDate);

	Mono<TaskItem> updateTask(Long id, TaskItem task);

	void finishTask(Long id);

}

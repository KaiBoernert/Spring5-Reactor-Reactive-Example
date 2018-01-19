package de.oio.server.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.oio.server.model.TaskItem;
import de.oio.server.service.TaskItemService;
import de.oio.shared.util.DateFormatUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This is the Spring-WebFlux controller, similar to normal Spring-MVC it allows
 * to define multiple methods that represent different WebApis. For each reqest
 * a Flux is created synchronous, wich will contain the response for the client
 * sometime in the future. The 'normal' Servlet based approach of using one
 * Thread for one Request is no longer true. This btw also means that Spring
 * Security cannot be used.
 * 
 * @author kboerner
 */
@RestController
@RequestMapping("task")
public class TaskItemController {

	private TaskItemService service;

	@Autowired
	public TaskItemController(TaskItemService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Flux<TaskItem> getAll() {
		return service.getAll();
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public Mono<TaskItem> getTask(@PathVariable("id") Long id) {
		return service.getTask(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Mono<TaskItem> createTask(@RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("dueDate") String dueDate) {
		try {
			return service.createTask(title, description, DateFormatUtils.getDateFormat().parse(dueDate));
		} catch (ParseException e) {
			return Mono.error(e);
		}
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public Mono<TaskItem> updateTask(@PathVariable("id") Long id, @RequestBody TaskItem task) {
		return service.updateTask(id, task);
	}

	@RequestMapping(value = "{id}/finish", method = RequestMethod.POST)
	public void finishTask(@PathVariable("id") Long id) {
		service.finishTask(id);
	}
}

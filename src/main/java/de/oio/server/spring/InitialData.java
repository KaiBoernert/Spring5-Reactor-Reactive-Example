package de.oio.server.spring;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.oio.server.model.TaskItem;
import de.oio.server.repository.TaskItemRepository;

@Component
public class InitialData {

	private TaskItemRepository repository;

	@Autowired
	public InitialData(TaskItemRepository repository) {
		this.repository = repository;
	}

	@PostConstruct
	public void addInitialData() {
		TaskItem task = new TaskItem("Title", "very important", new Date(new Date().getTime() + 133700), false);
		task.setId(1337L);
		repository.save(task).subscribe();
	}
}

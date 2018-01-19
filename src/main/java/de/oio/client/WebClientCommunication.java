package de.oio.client;

import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import de.oio.server.model.TaskItem;
import de.oio.shared.util.DateFormatUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The remote communication interface for the application, using Spring5 Webclient this allows a reactive communication with the backend.
 * Webclient itself works (default) with netty, and offers a nio-based network abstraction.
 * @author kboerner
 *
 */
public class WebClientCommunication {


	public static Mono<TaskItem> getTaskItemFor(Long taskId) {
		return WebClient.create("http://127.0.0.1:8080/task/" + taskId).get().accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToMono(TaskItem.class);
	}

	public static Mono<ClientResponse> createTaskItem(TaskItem toCreate) {
		final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("title", toCreate.getTitle());
		formData.add("description", toCreate.getDescription());
		formData.add("dueDate", DateFormatUtils.getDateFormat().format(toCreate.getDueDate()));
		return WebClient.create("http://127.0.0.1:8080/task").post().accept(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(formData)).exchange();
	}

	public static Mono<ClientResponse> updateTaskItem(TaskItem toUpdate) {
		return WebClient.create("http://127.0.0.1:8080/task/"+toUpdate.getId()).put().accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(toUpdate))
				.exchange();
	}

	public static Flux<TaskItem> getAllTasks() {
		return WebClient.create("http://127.0.0.1:8080/task").get().accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToFlux(TaskItem.class);
	}

	public static Mono<ClientResponse> setDone(Long id) {
		return WebClient.create("http://127.0.0.1:8080/task/"+id+"/finish").post().accept(MediaType.APPLICATION_JSON).exchange();
	}
}

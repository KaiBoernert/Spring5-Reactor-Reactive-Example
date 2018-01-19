package de.oio.client;

import java.util.function.Consumer;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import de.oio.server.model.TaskItem;
import de.oio.shared.util.DateFormatUtils;
import reactor.core.Disposable;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

public class TaskListUi {

	protected Disposable consoleSubscriber;

	public TaskListUi(DirectProcessor<String> consoleInput) {

		Flux<TaskItem> input = WebClientCommunication.getAllTasks();

		System.out.println("TaskList:");
		input.subscribe(new Subscriber<TaskItem>() {

			@Override
			public void onComplete() {
				System.out.println("Nummer zum Editieren eingeben");
				System.out.println("c: Neuen Task anlegen");
				System.out.println("q: Zurück zum Menu");
				consoleSubscriber = consoleInput.subscribe(new Consumer<String>() {

					@Override
					public void accept(String t) {
						if (t.equals("q")) {
							consoleSubscriber.dispose();
							new TaskMainView(consoleInput);
						}
						if (t.equals("c")) {
							consoleSubscriber.dispose();
							new TaskCreateEditUi(consoleInput);
						}
						try {
							consoleSubscriber.dispose();
							long taskId = Long.parseLong(t);
							new TaskCreateEditUi(consoleInput, taskId);
						} catch (Exception e) {
							System.out.println("Nummer nicht verstanden");
						}
					}
				});
			}

			@Override
			public void onError(Throwable arg0) {
				arg0.printStackTrace();
				System.out.println("Fehler beim abholen der List");
				if (consoleSubscriber != null) {
					consoleSubscriber.dispose();
				}
				new TaskMainView(consoleInput);
			}

			@Override
			public void onNext(TaskItem arg0) {
				System.out.println("Task: " + arg0.getId());
				System.out.println("Title: " + arg0.getTitle());
				System.out.println("Date: " + DateFormatUtils.getDateFormat().format(arg0.getDueDate()));
				System.out.println("Done: " + arg0.isDone());
				System.out.println("-----");
			}

			@Override
			public void onSubscribe(Subscription arg0) {
				arg0.request(Long.MAX_VALUE);
			}
		});

	}

}

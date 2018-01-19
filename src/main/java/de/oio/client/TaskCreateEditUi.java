package de.oio.client;

import java.text.ParseException;
import java.util.function.Consumer;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.reactive.function.client.ClientResponse;

import de.oio.server.model.TaskItem;
import de.oio.shared.util.DateFormatUtils;
import reactor.core.Disposable;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Mono;


public class TaskCreateEditUi {

	private Disposable consoleSubscription;

	public TaskCreateEditUi(DirectProcessor<String> consoleInput) {
		this(consoleInput, null);
	}

	public TaskCreateEditUi(DirectProcessor<String> consoleInput, Long taskId) {
		if (taskId == null) {
			editUi(new TaskItem(), consoleInput);
		} else {
			loadData(consoleInput, taskId);
		}
	}

	private void loadData(DirectProcessor<String> consoleInput, Long taskId) {
		Mono<TaskItem> resultOrEmpty = WebClientCommunication.getTaskItemFor(taskId);
		resultOrEmpty.subscribe(new Subscriber<TaskItem>() {

			private boolean foundTask;

			@Override
			public void onComplete() {
				if (!foundTask) {
					System.out.println("Task for Id " + taskId + " is not known");
					new TaskListUi(consoleInput);
				}
			}

			@Override
			public void onError(Throwable arg0) {
				arg0.printStackTrace();
			}

			@Override
			public void onNext(TaskItem arg0) {
				editUi(arg0, consoleInput);
				foundTask = true;
			}

			@Override
			public void onSubscribe(Subscription arg0) {
				arg0.request(1);
			}
		});
	}

	private void editUi(TaskItem toEditOrEmpty, DirectProcessor<String> consoleInput) {
		System.out.println("1: Title: " + toEditOrEmpty.getTitle());
		if (toEditOrEmpty.getDueDate() == null) {
			System.out.println("2: Date: leer");
		} else {
			System.out.println("2: Date: " + DateFormatUtils.getDateFormat().format(toEditOrEmpty.getDueDate()));
		}
		if (toEditOrEmpty.getId() != null) {
			System.out.println("3: Done: " + toEditOrEmpty.isDone());
		}
		System.out.println("4: Description: " + toEditOrEmpty.getDescription());
		System.out.println("5: Save");
		System.out.println("q: Cancel");

		consoleSubscription = consoleInput.subscribe(new Consumer<String>() {
			@Override
			public void accept(String t) {
				if (t.equals("1")) {
					consoleSubscription.dispose();
					taskFieldEdit(consoleInput, "Title", toEditOrEmpty, new Consumer<String>() {

						@Override
						public void accept(String t) {
							toEditOrEmpty.setTitle(t);
						}
					});
				}
				if (t.equals("2")) {
					consoleSubscription.dispose();
					taskFieldEdit(consoleInput, "Date", toEditOrEmpty, new Consumer<String>() {

						@Override
						public void accept(String t) {
							try {
								toEditOrEmpty.setDueDate(DateFormatUtils.getDateFormat().parse(t));
							} catch (ParseException e) {
								throw new RuntimeException(e);
							}
						}
					});
				}
				if (toEditOrEmpty.getId() != null) {
					if (t.equals("3")) {
						consoleSubscription.dispose();
						WebClientCommunication.setDone(toEditOrEmpty.getId())
								.subscribe(new Subscriber<ClientResponse>() {

									@Override
									public void onComplete() {
										System.out.println("Set completed!");
										back(consoleInput);
									}

									@Override
									public void onError(Throwable arg0) {
										System.out.println("Could not set compplete");
										back(consoleInput);
									}

									@Override
									public void onNext(ClientResponse arg0) {

									}

									@Override
									public void onSubscribe(Subscription arg0) {
										arg0.request(1);
									}
								});
						;
					}
				}
				if (t.equals("4")) {
					consoleSubscription.dispose();
					taskFieldEdit(consoleInput, "Description", toEditOrEmpty, new Consumer<String>() {

						@Override
						public void accept(String t) {
							toEditOrEmpty.setDescription(t);
						}
					});
				}
				if (t.equals("5")) {
					sendToServer(consoleInput, toEditOrEmpty);
				}
				if (t.equals("q")) {
					back(consoleInput);
				}
			}
		});

	}

	protected void sendToServer(DirectProcessor<String> consoleInput, TaskItem toEditOrEmpty) {
		Mono<ClientResponse> updated;
		if (toEditOrEmpty.getId() == null) {
			updated = WebClientCommunication.createTaskItem(toEditOrEmpty);
		} else {
			updated = WebClientCommunication.updateTaskItem(toEditOrEmpty);
		}
		updated.subscribe(new Subscriber<ClientResponse>() {

			@Override
			public void onComplete() {
				System.out.println("Sucessfully updated");
				back(consoleInput);
			}

			@Override
			public void onError(Throwable arg0) {
				System.out.println("Error on update!");
				back(consoleInput);
			}

			@Override
			public void onNext(ClientResponse arg0) {

			}

			@Override
			public void onSubscribe(Subscription arg0) {
				arg0.request(1);
			}
		});
	}

	private void back(DirectProcessor<String> consoleInput) {
		if (consoleSubscription != null) {
			consoleSubscription.dispose();
		}
		new TaskMainView(consoleInput);
	}

	protected void taskFieldEdit(DirectProcessor<String> consoleInput, String feldName, TaskItem toEditOrEmpty,
			Consumer<String> consumer) {
		System.out.println("Feld ändern " + feldName);
		consoleSubscription = consoleInput.subscribe(new Consumer<String>() {

			@Override
			public void accept(String t) {
				try {
					consumer.accept(t);
					consoleSubscription.dispose();
					editUi(toEditOrEmpty, consoleInput);
				} catch (Exception e) {
					System.out.println("Input ist im falschen Format, erneut versuchen");
				}
			}
		});
	}

}

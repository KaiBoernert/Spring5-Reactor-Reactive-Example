package de.oio.client;

import java.util.function.Consumer;

import reactor.core.Disposable;
import reactor.core.publisher.DirectProcessor;

/**
 * The main Menu of the application, it subscribes to the console input, and
 * depending on the key pressed it will start a new Menu and unsubscribe from
 * the console.
 * 
 * @author kboerner
 */
public class TaskMainView {

	private Disposable consoleSubscription;

	public TaskMainView(DirectProcessor<String> consoleInput) {
		System.out.println("Main Menu:");
		System.out.println("1: Alle Tasks anzeigen");
		System.out.println("c: Task anlegen");
		System.out.println("q: Quit");

		consoleSubscription = consoleInput.subscribe(new Consumer<String>() {
			@Override
			public void accept(String t) {
				if (t.equals("1")) {
					consoleSubscription.dispose();
					new TaskListUi(consoleInput);
				}
				if (t.equals("c")) {
					consoleSubscription.dispose();
					new TaskCreateEditUi(consoleInput);
				}
				if (t.equals("q")) {
					consoleSubscription.dispose();
					System.out.println("Quitting");
					System.exit(0);
				}
			}
		});
	}

}

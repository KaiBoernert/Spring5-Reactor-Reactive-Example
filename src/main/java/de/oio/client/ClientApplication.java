package de.oio.client;

import java.util.Scanner;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import reactor.core.publisher.DirectProcessor;

/**
 * This is a very simple Console-Client. System.in is wrapped into an FluxProcessor, as it is Hot (not repeatable, shared)
 * @author kboerner
 */
public class ClientApplication {
	public static void main(String[] args) throws InterruptedException {
		@SuppressWarnings("resource")
		Scanner inputScanner = new Scanner(System.in);

		DirectProcessor<String> consoleInput = DirectProcessor.create();

		Logger logger = (Logger) LoggerFactory.getLogger("root");
		logger.setLevel(Level.WARN);

		Thread inputReader = new Thread() {
			@Override
			public void run() {
				while (true) {
					if (inputScanner.hasNext()) {
						consoleInput.onNext(inputScanner.nextLine());
					} else {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		inputReader.start();

		new TaskMainView(consoleInput);
	}

}

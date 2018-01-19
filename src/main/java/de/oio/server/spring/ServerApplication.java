package de.oio.server.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import de.oio.server.ComponentScanRoot;
import de.oio.server.model.TaskItem;
import de.oio.server.repository.TaskItemRepository;

/**
 * The Main class for the server, it enables the required package scans, and ensures that first EmbeddedMongoAutoConfiguration bean is created, (which will download&start a Mongodb)
 * @author kboerner
 */
@SpringBootApplication
@EnableReactiveMongoRepositories(basePackageClasses = TaskItemRepository.class)
@EntityScan(basePackageClasses = TaskItem.class)
@ComponentScan(basePackageClasses = ComponentScanRoot.class)
@ServletComponentScan
@AutoConfigureAfter(EmbeddedMongoAutoConfiguration.class)
public class ServerApplication extends AbstractReactiveMongoConfiguration {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	private final Environment environment;

	public ServerApplication(Environment environment) {
		this.environment = environment;
	}

	@Override
	protected String getDatabaseName() {
		return "reactive-mongo";
	}

	@Override
	@Bean
	@DependsOn("embeddedMongoServer")
	public MongoClient reactiveMongoClient() {
		int port = environment.getProperty("local.mongo.port", Integer.class);
		return MongoClients.create(String.format("mongodb://localhost:%d", port));
	}

}

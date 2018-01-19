package de.oio.server.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import de.oio.server.model.TaskItem;

/**
 * By extending from ReactiveCrudRepository, Spring will offer all CRUD methods with reactive Datatypes. Note that this repository type can not be used with JDBC-Databases, as they cannot be reactive by design. This is the reason this example uses MongoDB
 * @author kboerner
 *
 */
@Repository
public interface TaskItemRepository extends ReactiveCrudRepository<TaskItem, Long> {

}

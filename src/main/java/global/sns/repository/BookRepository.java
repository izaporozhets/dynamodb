package global.sns.repository;

import global.sns.model.Book;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface BookRepository extends CrudRepository<Book, String> {

}

package telran.java58.forum.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java58.forum.model.Post;

import java.util.Set;
import java.util.stream.Stream;

public interface ForumRepository extends MongoRepository<Post, String> {
    Stream<Post> findByAuthor(String author);

    Stream<Post> findByTagsIn(Set<String> tags);

    Stream<Post> findByDateCreatedBetween(String startDate, String endDate);

}

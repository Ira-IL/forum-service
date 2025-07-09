package telran.java58.forum.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Document(collection = "forum")
public class Post {
    String id;
    @Setter
    String title;
    String content;
    @Setter
    String author;
    @Setter
    LocalDate dateCreated = LocalDate.now();
    @Setter
    Set<String> tags;
    @Setter
    int likes = 0;
    @Setter
    List<Comment> comments = new ArrayList<>();

    public Post(String id, String title, String content, String author, Set<String> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
    }

//    public void addLike() {
//        likes++;
//    }

//    public void addComment(String author, String comment) {
//        comments.add(new Comment(author, comment, LocalDateTime.now(), 0));
//    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

}


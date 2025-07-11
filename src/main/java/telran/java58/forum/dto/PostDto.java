package telran.java58.forum.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String id;
    private String title;
    private String content;
    private String author;
    private String dateCreated;
    private Set<String> tags;
    private int likes;
    private List<String> comments;
}

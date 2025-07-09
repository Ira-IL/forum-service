package telran.java58.forum.dto;


import lombok.Getter;

@Getter
public class ForumAddPostDto {
    private String title;
    private String content;
    private String[] tags;
}

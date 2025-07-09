package telran.java58.forum.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class PostUpdateDto {
    String title;
    Set<String> tags;
}

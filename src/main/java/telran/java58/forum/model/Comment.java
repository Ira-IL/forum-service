package telran.java58.forum.model;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
  private String user;
  private String message;
  private LocalDateTime dateCreated;
  private int likes = 0;
}

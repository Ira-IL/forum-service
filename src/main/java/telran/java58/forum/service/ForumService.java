package telran.java58.forum.service;

import telran.java58.forum.dto.ForumAddPostDto;
import telran.java58.forum.dto.PostAddCommentDto;
import telran.java58.forum.dto.PostDto;
import telran.java58.forum.dto.PostUpdateDto;

import java.util.List;
import java.util.Set;

public interface ForumService {
    PostDto addPost(ForumAddPostDto post, String user);

    PostDto findPostById(String postId);

    void addLike(String postId);

    List<PostDto> findPostsByAuthor(String user);

    PostDto addComment(String postId, String commenter, PostAddCommentDto comment);

    PostDto removePost(String postId);

    List<PostDto> findPostsByTags(Set<String> tags);

    List<PostDto> findPostsByPeriod(String startDate, String endDate);

    PostDto updatePost(String postId, PostUpdateDto post);
}

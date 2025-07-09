package telran.java58.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import telran.java58.forum.dto.ForumAddPostDto;
import telran.java58.forum.dto.PostAddCommentDto;
import telran.java58.forum.dto.PostDto;
import telran.java58.forum.dto.PostUpdateDto;
import telran.java58.forum.service.ForumService;

import java.util.List;
import java.util.Set;

@RestController
public class ForumController{
    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @PostMapping("/forum/post/{user}")
    public PostDto addPost(@RequestBody ForumAddPostDto post, @PathVariable String user) {
        return forumService.addPost(post, user);
    }

    @GetMapping("/forum/post/{postId}")
    public PostDto findPostById(@PathVariable String postId) {
        return forumService.findPostById(postId);
    }

    @PatchMapping("/forum/post/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable String postId) {
        forumService.addLike(postId);
    }

    @GetMapping("/forum/posts/author/{user}")
    public List<PostDto> findPostsByAuthor(@PathVariable String user) {
        return forumService.findPostsByAuthor(user);
    }

    @PatchMapping("/forum/post/{postId}/comment/{commenter}")
    public PostDto addComment(@PathVariable String postId, @PathVariable String commenter, @RequestBody PostAddCommentDto comment) {
        return forumService.addComment(postId, commenter, comment);
    }

    @DeleteMapping("/forum/post/{postId}")
    public PostDto removePost(@PathVariable String postId) {
        return forumService.removePost(postId);
    }

    @GetMapping("/forum/posts/tags")
    public List<PostDto> findPostsByTags(@RequestParam Set<String> tags) {
        return forumService.findPostsByTags(tags);
    }

    @GetMapping("/forum/posts/period")
    public List<PostDto> findPostsByPeriod(@RequestParam String startDate, @RequestParam String endDate) {
        return forumService.findPostsByPeriod(startDate, endDate);
    }

    @GetMapping("/forum/post/{postId}/update")
    public PostDto updatePost(@PathVariable String postId, @RequestBody PostUpdateDto post) {
        return forumService.updatePost(postId, post);
    }
}

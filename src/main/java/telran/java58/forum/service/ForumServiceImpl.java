package telran.java58.forum.service;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import telran.java58.forum.dao.ForumRepository;
import telran.java58.forum.dto.ForumAddPostDto;
import telran.java58.forum.dto.PostAddCommentDto;
import telran.java58.forum.dto.PostDto;
import telran.java58.forum.dto.PostUpdateDto;
import telran.java58.forum.dto.exceptions.ConflictException;
import telran.java58.forum.dto.exceptions.NotFoundException;
import telran.java58.forum.model.Comment;
import telran.java58.forum.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService{
   private final ForumRepository forumRepository;
   private final ModelMapper modelMapper;


    @Override
    public PostDto addPost(ForumAddPostDto postDto, String user) {
        Post post = modelMapper.map(postDto, Post.class);
        post.setAuthor(user);
        post.setDateCreated(LocalDate.now());
        forumRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto findPostById(String postId) {
        Post post = forumRepository.findById(postId).orElseThrow(NotFoundException::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void addLike(String postId) {
        Post post = forumRepository.findById(postId).orElseThrow(NotFoundException::new);
        post.setLikes(post.getLikes() + 1);
        forumRepository.save(post);
    }

    @Override
    public List<PostDto> findPostsByAuthor(String user) {
        return forumRepository.findByAuthor(user)
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public PostDto addComment(String postId, String commenter, PostAddCommentDto commentDto) {
        Post post = forumRepository.findById(postId).orElseThrow(NotFoundException::new);
        post.addComment(new Comment(commenter, commentDto.getMessage(), LocalDateTime.now(), 0));
        forumRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto removePost(String postId) {
        Post post = forumRepository.findById(postId).orElseThrow(NotFoundException::new);
        forumRepository.deleteById(postId);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> findPostsByTags(Set<String> tags) {
        return forumRepository.findByTagsIn(tags)
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public List<PostDto> findPostsByPeriod(String startDate, String endDate) {
        return forumRepository.findByDateCreatedBetween(startDate, endDate)
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public PostDto updatePost(String postId, PostUpdateDto postUpdateDto) {
        Post post = forumRepository.findById(postId).orElseThrow(NotFoundException::new);
        if(post.getTitle() != null) {
            post.setTitle(postUpdateDto.getTitle());
        }
        if(post.getTags() != null) {
            post.setTags(postUpdateDto.getTags());
        }
        return modelMapper.map(post, PostDto.class);

    }
}

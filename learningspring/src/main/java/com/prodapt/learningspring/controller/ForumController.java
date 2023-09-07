package com.prodapt.learningspring.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prodapt.learningspring.business.LoggedInUser;
import com.prodapt.learningspring.business.NeedsAuth;
import com.prodapt.learningspring.controller.binding.AddPostForm;
import com.prodapt.learningspring.controller.exception.ResourceNotFoundException;
import com.prodapt.learningspring.entity.CommentRecord;
import com.prodapt.learningspring.entity.LikeId;
import com.prodapt.learningspring.entity.LikeRecord;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.CommentCRUDRepository;
import com.prodapt.learningspring.repository.LikeCRUDRepository;
import com.prodapt.learningspring.repository.PostRepository;
import com.prodapt.learningspring.service.DomainUserService;

import jakarta.servlet.ServletException;

@Controller
@RequestMapping("/forum")
public class ForumController {
  
  @Autowired
  private PostRepository postRepository;

  @Autowired
  private DomainUserService domainUserService;

  @Autowired
  private CommentCRUDRepository commentCRUDRepository;

  @Autowired
  private LikeCRUDRepository likeCRUDRepository;

  @GetMapping("/post/form")
  public String getPostForm(Model model) {
    AddPostForm postForm = new AddPostForm();
    model.addAttribute("postForm", postForm);
    return "forum/postForm";
  }
  

  @PostMapping("/post/add")
  public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult, RedirectAttributes attr,@AuthenticationPrincipal UserDetails userDetails) throws ServletException {
    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult.getFieldErrors());
      attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
      attr.addFlashAttribute("post", postForm);
      return "redirect:/forum/post/form";
    }
    var user = domainUserService.getByName(userDetails.getUsername()).get();
    if (user==null) {
        return "redirect:/login";
    }
    Post post = new Post();
    post.setAuthor(user);
    post.setContent(postForm.getContent());
    post.setTimeStamp(LocalDateTime.now());
    postRepository.save(post);
    return String.format("redirect:/forum/post/%d", post.getId());
  }
  
  @GetMapping("/post/{id}")
  public String postDetail(@PathVariable int id, Model model) throws ResourceNotFoundException {
    Optional<Post> post = postRepository.findById(id);
    if (post.isEmpty()) {
      throw new ResourceNotFoundException("No post with the requested ID");
    }
    model.addAttribute("post", post.get());
    int numLikes = likeCRUDRepository.countByLikeIdPost(post.get());
    List<LikeRecord> allLikers = likeCRUDRepository.findByLikeIdPost(post.get());
    List<CommentRecord> allCommentRecords = commentCRUDRepository.findAllByPost(post.get());
    List<String> likerNames = allLikers.stream().map(likeRecord -> likeRecord.getLikeId().getUser().getName()).collect(Collectors.toList());
    model.addAttribute("AllComments", allCommentRecords);
    model.addAttribute("likeCount", numLikes);
    model.addAttribute("allLikers", likerNames);
    return "forum/postDetail";
  }
  
  @PostMapping("/post/{id}/{user_id}/like")
  public String postLike(@PathVariable int id,String Comment, RedirectAttributes attr,@AuthenticationPrincipal UserDetails userDetails) {
    var post = postRepository.findById(id);
    var user = domainUserService.getByName(userDetails.getUsername()).get();
    if(user==null){
        return "redirect:/login";
    }
    CommentRecord commentRecord = new CommentRecord();
    if(!Comment.equals(null)&&!Comment.equals("")){
      commentRecord.setPost(post.get());
      commentRecord.setUser(user);
      commentRecord.setComment(Comment);
      commentRecord.setTimeStamp(LocalDateTime.now());
      commentCRUDRepository.save(commentRecord);
    }
    LikeId likeId = new LikeId();
    likeId.setUser(user);
    likeId.setPost(post.get());
    LikeRecord like = new LikeRecord();
    like.setLikeId(likeId);
    likeCRUDRepository.save(like);
    postRepository.save(post.get());
    return String.format("redirect:/forum/post/%d", id);
  }
  
}

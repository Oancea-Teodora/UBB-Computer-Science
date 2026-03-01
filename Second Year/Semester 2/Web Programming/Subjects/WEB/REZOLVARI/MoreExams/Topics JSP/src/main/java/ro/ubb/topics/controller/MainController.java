package ro.ubb.topics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ro.ubb.topics.model.Post;
import ro.ubb.topics.model.Topic;
import ro.ubb.topics.repository.PostRepository;
import ro.ubb.topics.repository.TopicRepository;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    PostRepository postRepository;

    int length;

    @RequestMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session) {

        session.setAttribute("username", username);
        length = postRepository.findAll().size();
        return "home";

    }

    @RequestMapping(value = "/Post", method = RequestMethod.GET)
    public ResponseEntity<List<Post>> getPosts(HttpSession session) {

        return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);

    }

    @RequestMapping(value = "/Post/{id}", method = RequestMethod.PUT)
    public ResponseEntity<List<Post>> updatePost(@PathVariable Long id,@RequestBody Post post , HttpSession session) {

        post.setUsername((String) session.getAttribute("username"));
        post.setDate( LocalDate.now().getDayOfMonth());
        post.setId(id);

        postRepository.save(post);


        return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/Post", method = RequestMethod.POST)
    public ResponseEntity<List<Post>> addPost(@RequestParam String topicName, @RequestParam String text,HttpSession session) {


        try {
            Long topicId = topicRepository.findByTopicname(topicName).getId();
        }
        catch (Exception e)
        {
            var newTopic = Topic.builder().topicname(topicName).build();
            topicRepository.save(newTopic);
        }
        finally {
           Long topicId = topicRepository.findByTopicname(topicName).getId();

            var newPost = Post.builder()
                    .username((String) session.getAttribute("username"))
                    .topicid(Math.toIntExact(topicId))
                    .text(text)
                    .date(LocalDate.now().getDayOfMonth())
                    .build();

            postRepository.save(newPost);


        }
        return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);

    }

    @GetMapping("/Post/New")
    public ResponseEntity<String> wasNewlyAdded(HttpSession session) {
        int localLength = postRepository.findAll().size();
        if(localLength > length) {
            length = localLength;
            String userName = (String) session.getAttribute("username");
            var post = postRepository.findAll()
                    .stream()
                    .sorted((post1, post2) -> post2.getId().compareTo(post1.getId()))
                    .findFirst()
                    .orElseThrow();
            System.out.println(post);
            if (!post.getUsername().equals(userName)) {
                return new ResponseEntity<>(post.getText(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("false", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("false", HttpStatus.OK);
    }


}

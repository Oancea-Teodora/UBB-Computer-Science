package ro.ubb.journals.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.ubb.journals.model.Article;
import ro.ubb.journals.model.Journal;
import ro.ubb.journals.repository.ArticleRepository;
import ro.ubb.journals.repository.JournalRepository;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    JournalRepository journalRepository;

    int length;

    @RequestMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session) {

        session.setAttribute("username", username);
        length = articleRepository.findAll().size();
        return "home";

    }

    @RequestMapping(value = "/journals/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Article>> getJournal(@PathVariable String name, HttpSession session) {
        String username =  (String) session.getAttribute("username");
        var journal = journalRepository.findByName(name);

        if (username != null) {
            return new ResponseEntity<>(articleRepository.findAllByUsernameAndJournalId(username, Math.toIntExact(journal.getId())), HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/addArticle")
    public ResponseEntity<List<Article>> addArticle(@RequestParam String journalName,@RequestParam String summary, HttpSession session) {
        String username =  (String) session.getAttribute("username");
        var journal = journalRepository.findByName(journalName);

        if(journal == null){
            journalRepository.save(Journal.builder().name(journalName).build());
            journal = journalRepository.findByName(journalName);
        }
        int journalId = Math.toIntExact(journal.getId());
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();

        Article article = Article.builder()
                .username(username)
                .journalId(journalId)
                .summary(summary)
                .date(currentDay)
                .build();

        articleRepository.save(article);

        return new ResponseEntity<>(articleRepository.findAllByUsernameAndJournalId(username, journalId), HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<Boolean> wasNewlyAdded(HttpSession session) {
        int localLength = articleRepository.findAll().size();
        if(localLength > length) {
            length = localLength;
            String userName = (String) session.getAttribute("username");
            var article = articleRepository.findAll()
                    .stream()
                    .sorted((art1, art2) -> art2.getId().compareTo(art1.getId()))
                    .findFirst()
                    .orElseThrow();
            System.out.println(article);
            if (!article.getUsername().equals(userName)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }


}

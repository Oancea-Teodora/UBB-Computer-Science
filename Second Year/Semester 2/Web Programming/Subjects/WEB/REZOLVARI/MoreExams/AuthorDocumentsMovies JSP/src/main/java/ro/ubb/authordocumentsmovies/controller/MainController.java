package ro.ubb.authordocumentsmovies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.ubb.authordocumentsmovies.model.Document;
import ro.ubb.authordocumentsmovies.model.Movie;
import ro.ubb.authordocumentsmovies.repository.AuthorRepository;
import ro.ubb.authordocumentsmovies.repository.DocumentRepository;
import ro.ubb.authordocumentsmovies.repository.MovieRepository;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    MovieRepository movieRepository;

    int counter = 0;

    String nameOfMostFrequent = "";
    int maximum = 0;


    @RequestMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session) {

        session.setAttribute("username", username);
        return "home";

    }

    @RequestMapping(value = "/document", method = RequestMethod.GET)
    public ResponseEntity<List<Document>> getDocument(HttpSession session) {
        return new ResponseEntity<>(documentRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/document")
    public ResponseEntity<List<Document>> addArticle(@RequestParam String name,@RequestParam String contents, HttpSession session) {
        var document = Document.builder().name(name).contents(contents).build();

        documentRepository.save(document);

        return new ResponseEntity<>(documentRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/interleave1", method = RequestMethod.GET)
    public ResponseEntity<List<Document>> getInterleave1(HttpSession session) {
        var username = (String)session.getAttribute("username");
        var author = authorRepository.findByName(username);
        var result = documentRepository.findAll().stream()
                .filter(document -> author.getDocumentList().contains(document.getName()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/interleave2", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getInterleave2(HttpSession session) {
        var username = (String)session.getAttribute("username");
        var author = authorRepository.findByName(username);
        var result = movieRepository.findAll().stream()
                .filter(movie -> author.getMovieList().contains(movie.getTitle()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/mostfrequent", method = RequestMethod.GET)
    public ResponseEntity<List<Document>> getMostFrequent(HttpSession session) {


        var authors = authorRepository.findAll();
        var documents = documentRepository.findAll();

        documents.forEach(document -> {
            counter = 0;

           authors.forEach(author ->{
              if (author.getDocumentList().contains(document.getName()))
              {
                  counter ++;
              }
           });

           if (counter > maximum){
               maximum = counter;
               nameOfMostFrequent = document.getName();
           }
        });

        var result = documentRepository.findByName(nameOfMostFrequent);
        var resultList = new ArrayList<Document>();
        resultList.add(result);
        return new ResponseEntity<>(resultList, HttpStatus.OK);

    }



}
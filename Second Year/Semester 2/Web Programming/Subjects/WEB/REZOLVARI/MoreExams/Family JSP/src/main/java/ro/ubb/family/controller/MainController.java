package ro.ubb.family.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.ubb.family.model.FamilyRelation;
import ro.ubb.family.repository.AppUserRepository;
import ro.ubb.family.repository.FamilyRelationRepository;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    FamilyRelationRepository familyRelationRepository;



    @RequestMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String parent, HttpSession session) {
        FamilyRelation user = familyRelationRepository.findByUsernameAndFatherOrMother(username, parent, parent);
        if (user != null) {
            session.setAttribute("username", username);
            return "home";
        }
        return "login";
    }


    @GetMapping("/familyRelations")
    public ResponseEntity<List<FamilyRelation>> getFamilyRelations(HttpSession session) {

        return new ResponseEntity<>(familyRelationRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/familyRelations", method = RequestMethod.POST)
    public ResponseEntity<List<FamilyRelation>> addFamilyRelation(@RequestParam String username,
                                                                  @RequestParam String mother,
                                                                  @RequestParam String father,

                                                                  HttpSession session) {

        var familyRelation = FamilyRelation.builder()
                .username(username)
                .mother(mother)
                .father(father)
                .build();
        familyRelationRepository.save(familyRelation);

        return new ResponseEntity<>(familyRelationRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/motherFamilyLine")
    public ResponseEntity<String> getMotherFamilyLine(HttpSession session) {

        var username = (String)session.getAttribute("username");

        StringBuilder motherFamilyLine = new StringBuilder();
        String motherName = "";
        while (username != null)
        {
            FamilyRelation familyRelation = familyRelationRepository.findByUsername(username);

            if(familyRelation != null) {
                motherName = familyRelation.getMother();
                motherFamilyLine.append(motherName).append(" <-- ");
                username = motherName;
            }
            else {
                username = null;
            }

        }

        return new ResponseEntity<>(motherFamilyLine.toString(), HttpStatus.OK);

    }

    @GetMapping("/fatherFamilyLine")
    public ResponseEntity<String> getFatherFamilyLine(HttpSession session) {
        var username = (String)session.getAttribute("username");

        StringBuilder fatherFamilyLine = new StringBuilder();
        String fatherName = "";
        while (username != null)
        {
            FamilyRelation familyRelation = familyRelationRepository.findByUsername(username);

            if(familyRelation != null) {
                fatherName = familyRelation.getFather();
                fatherFamilyLine.append(fatherName).append(" <-- ");
                username = fatherName;
            }
            else {
                username = null;
            }

        }

        return new ResponseEntity<>(fatherFamilyLine.toString(), HttpStatus.OK);
    }



}

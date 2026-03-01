package ro.ubb.assets_jsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.ubb.assets_jsp.model.Asset;
import ro.ubb.assets_jsp.model.AppUser;
import ro.ubb.assets_jsp.model.AssetDTO;
import ro.ubb.assets_jsp.repository.AssetsRepository;
import ro.ubb.assets_jsp.repository.UsersRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    UsersRepository userRepository;

    @Autowired
    AssetsRepository assetRepository;


    @RequestMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,HttpSession session) {
        AppUser user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            return "home";
        }
        return "login";
    }



    @GetMapping("/assets")
    public ResponseEntity<List<Asset>> getAssets(HttpSession session) {
        Long id = (Long) session.getAttribute("userId");
        var user = userRepository.findById(id).orElseThrow();
        System.out.println(user);
        if (user != null) {
            return new ResponseEntity<>(assetRepository.findAll(), HttpStatus.OK);
        }
        return null;
    }

    @RequestMapping(value = "/assets", method = RequestMethod.POST)
    public ResponseEntity<List<Asset>> insertAssets(@RequestBody List<AssetDTO> data, HttpSession session) {
        System.out.println(data);
        Long id = (Long) session.getAttribute("userId");
        AppUser user = userRepository.findById(id).orElseThrow();
        System.out.println(user);
        if (user != null) {
            for (AssetDTO assetDto : data) {
                System.out.println(assetDto);
                Asset asset = new Asset(
                        user.getId(),
                        assetDto.getName(),
                        assetDto.getDescription(),
                        assetDto.getValue()
                );
                System.out.println(asset);
                assetRepository.save(asset);
            }
        }
        return new ResponseEntity<>(assetRepository.findAll(), HttpStatus.OK);
    }





}
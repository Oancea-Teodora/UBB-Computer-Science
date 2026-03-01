package ro.ubb.products.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.ubb.products.model.OrderEntity;
import ro.ubb.products.model.Product;
import ro.ubb.products.repository.OrderEntityRepository;
import ro.ubb.products.repository.ProductRepository;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    OrderEntityRepository orderEntityRepository;

    @Autowired
    ProductRepository productRepository;

    List<String> orderList = new ArrayList<>();


    @RequestMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, HttpSession session) {

        session.setAttribute("username", username);
        return "home";

    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public String addProduct(@RequestParam String name,
                                                    @RequestParam String description,
                                                    HttpSession session) {

       var product = Product.builder()
               .name(name)
               .description(description)
               .build();
        productRepository.save(product);

        return "home";
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam String name)
    {
        List<Product> output = productRepository.findAll()
                .stream()
                .filter(product -> product.getName().startsWith(name))
                .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();

        for (Product product : output){
            builder.append("Product name: ")
                    .append(product.getName())
                    .append(" Description: ")
                    .append(product.getDescription())
                    .append("; ");

        }

        return new ResponseEntity<>(builder.toString(), HttpStatus.OK);

    }


    @GetMapping("/cancel")
    public String cancel(@RequestParam Object x, HttpSession httpSession) {
        httpSession.setAttribute("shopList", "");
        orderList.clear();
        return "home";
    }

    @GetMapping("/confirm")
    public String confirm(HttpSession httpSession) {

        String username = (String) httpSession.getAttribute("username");

        for (String product : orderList) {
            var split = product.split(" ");
            String name = split[0];
            int quantity = Integer.parseInt(split[1]);
            long productId = productRepository.findByName(name).getId();
            OrderEntity orders = new OrderEntity();
            orders.setQuantity(quantity);
            orders.setUsername(username);
            orders.setProductid((int) productId);
            orderEntityRepository.save(orders);
        }

        httpSession.setAttribute("shopList", "");
        orderList.clear();
        return "home";
    }

    @PostMapping("/addItem")
    public ResponseEntity<String> addItem(@RequestParam String productName, @RequestParam int quantity, HttpSession httpSession) {
        orderList.add(productName + " " + quantity);
        httpSession.setAttribute("shopList", orderList);
        return new ResponseEntity<>(orderList.toString(), HttpStatus.OK);
    }



}

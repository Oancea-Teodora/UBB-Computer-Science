package web.exam.jspexam.controller;

import org.json.JSONArray;
import web.exam.jspexam.model.Keyword;
import web.exam.jspexam.repository.DocumentsRepo;
import web.exam.jspexam.repository.KeywordsRepo;
import web.exam.jspexam.repository.TemplateRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet(name = "MainServlet", value = "/home")
public class MainController extends HttpServlet {
    private KeywordsRepo keywordsRepo;
    private DocumentsRepo documentsRepo;
    private TemplateRepo templateRepo;

    @Override
    public void init() throws ServletException {
        super.init();

        this.keywordsRepo = new KeywordsRepo();
        this.documentsRepo = new DocumentsRepo();
        this.templateRepo = new TemplateRepo();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var action = request.getParameter("action");

        switch (action) {
            case "search" -> handleSearchGet(request, response);
            case "render" -> handleRenderGet(request, response);
        }
    }

    private void handleRenderGet(HttpServletRequest request, HttpServletResponse response) {
        var id = request.getParameter("id");
        var opt = documentsRepo.findById(Integer.parseInt(id));
        if (opt.isEmpty()) {
            return;
        }

        var doc = opt.get();
        var templates = doc.getTemplates();
        var templatesArr = Arrays.stream(templates.split(",")).collect(Collectors.toList());

        String text = "";
        var templateObjs = templatesArr.stream().map(t -> {
            var temp = templateRepo.findByName(t);
            return temp.isEmpty() ? null : temp.get();
        }).collect(Collectors.toList());

        for (var t : templateObjs.stream().filter(Objects::nonNull).collect(Collectors.toList())) {
            text += t.getTextContent() + "\n";
        }

        var keywords = keywordsRepo.findAll();
        for (var keyword : keywords) {
            var key = keyword.getKey();
            var value = keyword.getValue();

            text = text.replaceAll(key, value);
        }

        PrintWriter responseOut = null;
        try {
            responseOut = new PrintWriter(response.getOutputStream());
            responseOut.println(text);
            responseOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSearchGet(HttpServletRequest request, HttpServletResponse response) {
        var title = request.getParameter("title");
        var docs = documentsRepo.findAllContainingTitle(title);

        JSONArray jsonObjs = new JSONArray();
        docs.forEach(doc -> jsonObjs.put(doc.convertToJson()));

        PrintWriter responseOut = null;
        try {
            responseOut = new PrintWriter(response.getOutputStream());
            responseOut.println(jsonObjs);
            responseOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        var action = request.getParameter("action");

        switch (action) {
            case "add" -> handleAddPost(request, response);
        }
    }

    private void handleAddPost(HttpServletRequest request, HttpServletResponse response) {
        var key = request.getParameter("key");
        var value = request.getParameter("value");

        keywordsRepo.save(new Keyword(-1, key, value));
    }
}

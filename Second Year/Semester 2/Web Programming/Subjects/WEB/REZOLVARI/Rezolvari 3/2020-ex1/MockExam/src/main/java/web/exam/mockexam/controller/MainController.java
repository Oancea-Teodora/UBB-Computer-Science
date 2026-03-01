package web.exam.mockexam.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import web.exam.mockexam.service.ArticleService;
import web.exam.mockexam.service.JournalService;
import web.exam.mockexam.service.MainService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MainServlet", value = "/home")
public class MainController extends HttpServlet {
    private ArticleService articleService;
    private JournalService journalService;
    private MainService mainService;

    @Override
    public void init() throws ServletException {
        super.init();

        articleService = new ArticleService();
        journalService = new JournalService();
        mainService = new MainService(articleService, journalService);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var action = request.getParameter("action");

        switch (action) {
            case "submitName" -> handleSubmitNameGet(request, response);
            case "getArticlesFromJournal" -> handleGetArticlesFromJournalGet(request, response);
            case "checkLastArticle" -> handleCheckLastArticleGet(request, response);
        }
    }

    private void handleCheckLastArticleGet(HttpServletRequest request, HttpServletResponse response) {
        var optional = articleService.getLastArticle();
        var article = optional.isEmpty() ? null : optional.get();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(article == null ? "" : article.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSubmitNameGet(HttpServletRequest request, HttpServletResponse response) {
        var name = request.getParameter("name");
        HttpSession session = request.getSession();
        session.setAttribute("user", name);
    }

    private void handleGetArticlesFromJournalGet(HttpServletRequest request, HttpServletResponse response) {
        var user = request.getSession().getAttribute("user");
        var journal = request.getParameter("journal");

        if (!(user instanceof String) || ((String) user).equals("")) {
            return;
        }

        var articles = mainService.getAllUserArticleFromJournal((String)user, journal);

        JSONArray jsonObjs = new JSONArray();
        for (var article : articles) {
            JSONObject jsonObj = new JSONObject();

            jsonObj.put("id", article.getId());
            jsonObj.put("user", article.getUser());
            jsonObj.put("journalid", article.getJournalId());
            jsonObj.put("summary", article.getSummary());
            jsonObj.put("date", article.getDate());

            jsonObjs.put(jsonObj);
        }

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
            case "addArticle" -> handleAddArticlePost(request, response);
        }
    }

    private void handleAddArticlePost(HttpServletRequest request, HttpServletResponse response) {
        var user = request.getSession().getAttribute("user");
        var journal = request.getParameter("journal");
        var summary = request.getParameter("summary");

        mainService.addNewArticleToJournal((String) user, summary, journal);
    }
}

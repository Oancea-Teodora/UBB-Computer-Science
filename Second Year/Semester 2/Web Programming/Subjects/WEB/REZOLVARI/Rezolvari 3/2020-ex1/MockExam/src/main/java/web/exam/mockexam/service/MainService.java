package web.exam.mockexam.service;

import web.exam.mockexam.model.Article;
import web.exam.mockexam.model.Journal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainService {
    private final ArticleService articleService;
    private final JournalService journalService;

    public MainService(ArticleService articleService, JournalService journalService) {
        this.articleService = articleService;
        this.journalService = journalService;
    }

    public List<Article> getAllUserArticleFromJournal(String user, String journalName) {
        var userArticles = articleService.findAllByUser(user);
        var journal = journalService.findByName(journalName);
        if (journal.isEmpty() || userArticles.isEmpty()) {
            return new ArrayList<>();
        }

        return userArticles.stream().filter(article -> article.getJournalId() == journal.get().getId()).collect(Collectors.toList());
    }

    public void addNewArticleToJournal(String user, String articleSummary, String journalName) {
        journalService.save(new Journal(-1, journalName));
        var journalOptional = journalService.findByName(journalName);
        if (journalOptional.isEmpty()) {
            return;
        }

        var journal = journalOptional.get();
        var article = new Article(-1, user, journal.getId(), articleSummary, new Timestamp(System.currentTimeMillis()));
        articleService.save(article);
    }
}

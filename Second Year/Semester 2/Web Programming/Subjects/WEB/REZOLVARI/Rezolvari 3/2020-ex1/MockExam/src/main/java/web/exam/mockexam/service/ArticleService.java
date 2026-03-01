package web.exam.mockexam.service;

import web.exam.mockexam.model.Article;
import web.exam.mockexam.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService() {
        this.articleRepository = new ArticleRepository();
    }

    List<Article> findAll() {
        return articleRepository.findAll();
    }

    List<Article> findAllByUser(String user) {
        return articleRepository.findAllByUser(user);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public Optional<Article> getLastArticle() {
        return articleRepository.getLastArticle();
    }
}

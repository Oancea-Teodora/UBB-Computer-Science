package ro.ubb.journals.repository;

import ro.ubb.journals.model.Article;
import ro.ubb.journals.model.Journal;

import java.util.List;

public interface ArticleRepository extends MainRepository<Article, Long>{
    List<Article> findAllByUsernameAndJournalId(String username, int journalId);
}

package web.exam.mockexam.repository;

import web.exam.mockexam.model.Article;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleRepository {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/web-2020-ex1";
    private static final String databaseUsername = "postgres";
    private static final String databasePassword = "postgres";

    public ArticleRepository() {
        try {
            Class.forName("org.postgresql.Driver");  // load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save(Article article) {
        if (article == null) {
            return;
        }

        String insertSqlQuery = "INSERT INTO public.\"Articles\" (\"user\", journalid, summary, date) VALUES(?, ?, ?, ?);";
        try(var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            var statement = connection.prepareStatement(insertSqlQuery);
        ) {
            statement.setString(1, article.getUser());
            statement.setInt(2, article.getJournalId());
            statement.setString(3, article.getSummary());
            statement.setTimestamp(4, article.getDate());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Article> findAll() {
        var query = "SELECT * FROM public.\"Articles\"";
        return findAllWithQuery(query);
    }

    public List<Article> findAllByUser(String user) {
        var query = String.format("SELECT * FROM public.\"Articles\" a WHERE a.user = '%s'", user);
        return findAllWithQuery(query);
    }

    public Optional<Article> getLastArticle() {
        var query = "SELECT * FROM public.\"Articles\" ORDER BY id DESC LIMIT 1";
        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query)
        ) {
            try (
                var resultSet = statement.executeQuery()
            ) {
                return getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Article> findAllWithQuery(String query) {
        List<Article> articles = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query);
                var resultSet = statement.executeQuery()
        ) {
            Optional<Article> optional = getEntityFromResultSet(resultSet);
            while(optional.isPresent()){
                articles.add(optional.get());
                optional = getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    private Optional<Article> getEntityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String user = resultSet.getString("user");
                int journalId = resultSet.getInt("journalid");
                String summary = resultSet.getString("summary");
                Timestamp date = resultSet.getTimestamp("date");

                return Optional.of(new Article(id, user, journalId, summary, date));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}

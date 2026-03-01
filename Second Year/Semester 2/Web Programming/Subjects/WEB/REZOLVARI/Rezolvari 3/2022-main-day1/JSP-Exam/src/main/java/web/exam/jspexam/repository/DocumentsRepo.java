package web.exam.jspexam.repository;

import web.exam.jspexam.model.Document;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DocumentsRepo {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/web-main1";
    private static final String databaseUsername = "postgres";
    private static final String databasePassword = "postgres";

    public DocumentsRepo() {
        try {
            Class.forName("org.postgresql.Driver");  // load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Document> findAllContainingTitle(String title) {
        return findAll().stream().filter(doc -> doc.getTitle().startsWith(title)).collect(Collectors.toList());
    }

    public Optional<Document> findById(Integer id) {
        return findAll().stream().filter(doc -> doc.getId().equals(id)).findFirst();
    }

    public List<Document> findAll() {
        var query = "SELECT * FROM public.\"Documents\"";
        return findAllWithQuery(query);
    }

    public List<Document> findAllWithQuery(String query) {
        List<Document> documents = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query);
                var resultSet = statement.executeQuery()
        ) {
            Optional<Document> optional = getEntityFromResultSet(resultSet);
            while(optional.isPresent()){
                documents.add(optional.get());
                optional = getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    private Optional<Document> getEntityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String templates = resultSet.getString("templates");

                return Optional.of(new Document(id, title, templates));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}

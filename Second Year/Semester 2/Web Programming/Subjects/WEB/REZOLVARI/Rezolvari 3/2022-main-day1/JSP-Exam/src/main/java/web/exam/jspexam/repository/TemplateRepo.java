package web.exam.jspexam.repository;

import web.exam.jspexam.model.Template;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemplateRepo {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/web-main1";
    private static final String databaseUsername = "postgres";
    private static final String databasePassword = "postgres";

    public TemplateRepo() {
        try {
            Class.forName("org.postgresql.Driver");  // load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Optional<Template> findByName(String name) {
        return findAll().stream().filter(doc -> doc.getName().equals(name)).findFirst();
    }

    public List<Template> findAll() {
        var query = "SELECT * FROM public.\"Templates\"";
        return findAllWithQuery(query);
    }

    public List<Template> findAllWithQuery(String query) {
        List<Template> templates = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query);
                var resultSet = statement.executeQuery()
        ) {
            Optional<Template> optional = getEntityFromResultSet(resultSet);
            while(optional.isPresent()){
                templates.add(optional.get());
                optional = getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return templates;
    }

    private Optional<Template> getEntityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String textContent = resultSet.getString("textContent");

                return Optional.of(new Template(id, name, textContent));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}

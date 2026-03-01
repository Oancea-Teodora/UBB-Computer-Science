package web.exam.jspexam.repository;

import web.exam.jspexam.model.Keyword;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KeywordsRepo {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/web-main1";
    private static final String databaseUsername = "postgres";
    private static final String databasePassword = "postgres";

    public KeywordsRepo() {
        try {
            Class.forName("org.postgresql.Driver");  // load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save(Keyword keyword) {
        if (keyword == null) {
            return;
        }

        String insertSqlQuery = "INSERT INTO public.\"Keywords\" (\"key\", \"value\") VALUES(?, ?);";
        try(var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            var statement = connection.prepareStatement(insertSqlQuery);
        ) {
            statement.setString(1, keyword.getKey());
            statement.setString(2, keyword.getValue());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Keyword> findAll() {
        var query = "SELECT * FROM public.\"Keywords\"";
        return findAllWithQuery(query);
    }

    public List<Keyword> findAllWithQuery(String query) {
        List<Keyword> keywords = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query);
                var resultSet = statement.executeQuery()
        ) {
            Optional<Keyword> optional = getEntityFromResultSet(resultSet);
            while(optional.isPresent()){
                keywords.add(optional.get());
                optional = getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keywords;
    }

    private Optional<Keyword> getEntityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String key = resultSet.getString("key");
                String value = resultSet.getString("value");

                return Optional.of(new Keyword(id, key, value));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}

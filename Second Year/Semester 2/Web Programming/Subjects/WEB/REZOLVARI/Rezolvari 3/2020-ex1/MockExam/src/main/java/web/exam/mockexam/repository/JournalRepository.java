package web.exam.mockexam.repository;

import web.exam.mockexam.model.Journal;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JournalRepository {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/web-2020-ex1";
    private static final String databaseUsername = "postgres";
    private static final String databasePassword = "postgres";

    public JournalRepository() {
        try {
            Class.forName("org.postgresql.Driver");  // load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save(Journal journal) {
        String insertSqlQuery = "INSERT INTO public.\"Journals\"(name) VALUES(?) ON CONFLICT(name) DO UPDATE SET name = excluded.name";
        try(var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            var statement = connection.prepareStatement(insertSqlQuery);
        ) {
            statement.setString(1, journal.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Journal> findById(Integer id) {
        String query = "SELECT * FROM public.\"Journals\" WHERE id = ?;";

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, id.toString());

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

    public Optional<Journal> findByName(String name) {
        String query = "SELECT * FROM public.\"Journals\" WHERE name = ?;";

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, name);

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

    public List<Journal> findAll() {
        var query = "SELECT * FROM public.\"Journals\"";
        return findAllWithQuery(query);
    }

    private List<Journal> findAllWithQuery(String query) {
        List<Journal> journals = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query);
                var resultSet = statement.executeQuery()
        ) {
            Optional<Journal> optional = getEntityFromResultSet(resultSet);
            while(optional.isPresent()){
                journals.add(optional.get());
                optional = getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return journals;
    }

    private Optional<Journal> getEntityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                return Optional.of(new Journal(id, name));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}

package web.exam.exam.repository;

import web.exam.exam.model.Developer;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeveloperRepo {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/web-2022-sec";
    private static final String databaseUsername = "postgres";
    private static final String databasePassword = "postgres";

    public DeveloperRepo() {
        try {
            Class.forName("org.postgresql.Driver");  // load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Developer> findAllWithSkill(String skill) {
        var query = "SELECT * FROM public.\"Developers\"";
        return findAllWithQuery(query).stream().filter(d -> d.hasSkill(skill)).collect(Collectors.toList());
    }

    public List<Developer> findAll() {
        var query = "SELECT * FROM public.\"Developers\"";
        return findAllWithQuery(query);
    }

    private List<Developer> findAllWithQuery(String query) {
        List<Developer> developers = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query);
                var resultSet = statement.executeQuery()
        ) {
            Optional<Developer> optional = getEntityFromResultSet(resultSet);
            while(optional.isPresent()){
                developers.add(optional.get());
                optional = getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return developers;
    }

    private Optional<Developer> getEntityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String skills = resultSet.getString("skills");

                return Optional.of(new Developer(id, name, age, skills));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}

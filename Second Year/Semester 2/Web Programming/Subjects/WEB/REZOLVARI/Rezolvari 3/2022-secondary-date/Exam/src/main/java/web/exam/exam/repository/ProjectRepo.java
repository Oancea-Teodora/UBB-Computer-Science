package web.exam.exam.repository;

import web.exam.exam.model.Project;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectRepo {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/web-2022-sec";
    private static final String databaseUsername = "postgres";
    private static final String databasePassword = "postgres";

    public ProjectRepo() {
        try {
            Class.forName("org.postgresql.Driver");  // load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveProjectWithNameOnly(Project project) {
        String insertSqlQuery = "INSERT INTO public.\"Projects\"(name) VALUES(?);";
        try(var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            var statement = connection.prepareStatement(insertSqlQuery);
        ) {
            statement.setString(1, project.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDevMemberToProject(Project project, String dev) {
        if (project.isMemberOf(dev)) {
            return;
        }

        String query = "UPDATE public.\"Projects\" SET members = ? WHERE id = ?;";
        try(var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            var statement = connection.prepareStatement(query);
        ) {
            String newMembers = project.getMembers() + dev + ",";

            statement.setString(1, newMembers);
            statement.setInt(2, project.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Project> findAllMemberOf(String user) {
        var query = "SELECT * FROM public.\"Projects\"";
        return findAllWithQuery(query).stream().filter(p -> p.isMemberOf(user)).collect(Collectors.toList());
    }

    public List<Project> findAll() {
        var query = "SELECT * FROM public.\"Projects\"";
        return findAllWithQuery(query);
    }

    private List<Project> findAllWithQuery(String query) {
        List<Project> projects = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
                var statement = connection.prepareStatement(query);
                var resultSet = statement.executeQuery()
        ) {
            Optional<Project> optional = getEntityFromResultSet(resultSet);
            while(optional.isPresent()){
                projects.add(optional.get());
                optional = getEntityFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    private Optional<Project> getEntityFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                int managerId = resultSet.getInt("managerId");
                String name = resultSet.getString("name");
                String desc = resultSet.getString("description");
                String members = resultSet.getString("members");

                return Optional.of(new Project(id, managerId, name, desc, members));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}

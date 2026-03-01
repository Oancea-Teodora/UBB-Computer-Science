package web.exam.exam.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import web.exam.exam.model.Developer;
import web.exam.exam.model.Project;
import web.exam.exam.repository.DeveloperRepo;
import web.exam.exam.repository.ProjectRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "MainServlet", value = "/home")
public class MainController extends HttpServlet {
    private DeveloperRepo developerRepo;
    private ProjectRepo projectRepo;

    private String user = "";

    @Override
    public void init() throws ServletException {
        super.init();

        developerRepo = new DeveloperRepo();
        projectRepo = new ProjectRepo();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        var action = request.getParameter("action");

        switch (action) {
            case "allProjects" -> handleAllProjectsGet(request, response);
            case "memberProjects" -> handlerMemberProjectsGet(request, response);
            case "allDevs" -> handleAllDevsGet(request, response);
            case "user" -> handleUserGet(request, response);
        }
    }

    private void handleUserGet(HttpServletRequest request, HttpServletResponse response) {
        var user = request.getParameter("user");
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        this.user = user;
    }

    private void handleAllProjectsGet(HttpServletRequest request, HttpServletResponse response) {
        var projects = projectRepo.findAll();
        displayAllProjectsJson(projects, response, false);
    }

    private void handlerMemberProjectsGet(HttpServletRequest request, HttpServletResponse response) {
//        var user = (String) request.getSession().getAttribute("user");
//        System.out.println(user);

        var projects = projectRepo.findAllMemberOf(this.user);
        displayAllProjectsJson(projects, response, true);
    }

    private void handleAllDevsGet(HttpServletRequest request, HttpServletResponse response) {
        var devs = developerRepo.findAll();
        displayAllDevsJson(devs, response);
    }

    private void displayAllProjectsJson(List<Project> projects, HttpServletResponse response, boolean onlyName) {
        JSONArray jsonObjs = new JSONArray();
        for (var project : projects) {
            JSONObject jsonObj = new JSONObject();

            if (!onlyName) {
                jsonObj.put("id", project.getId());
                jsonObj.put("managerId", project.getManagerId());
                jsonObj.put("desc", project.getDesc());
                jsonObj.put("members", project.getMembers());
            }

            jsonObj.put("name", project.getName());

            jsonObjs.put(jsonObj);
        }

        PrintWriter responseOut = null;
        try {
            responseOut = new PrintWriter(response.getOutputStream());
            responseOut.println(jsonObjs);
            responseOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayAllDevsJson(List<Developer> devs, HttpServletResponse response) {
        JSONArray jsonObjs = new JSONArray();
        for (var dev : devs) {
            JSONObject jsonObj = new JSONObject();

            jsonObj.put("id", dev.getId());
            jsonObj.put("name", dev.getName());
            jsonObj.put("age", dev.getAge());
            jsonObj.put("skills", dev.getSkills());

            jsonObjs.put(jsonObj);
        }

        PrintWriter responseOut = null;
        try {
            responseOut = new PrintWriter(response.getOutputStream());
            responseOut.println(jsonObjs);
            responseOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setContentType("application/json");

        var action = request.getParameter("action");

        switch (action) {
            case "assign" -> handleAssignPost(request, response);
        }
    }

    private void handleAssignPost(HttpServletRequest request, HttpServletResponse response) {
        var devName = request.getParameter("devName");
        var projects = request.getParameter("projects");
        var optional = developerRepo.findAll().stream().filter(d -> d.getName().equals(devName)).findFirst();

        if (optional.isEmpty()) {
            return;
        }

        var dev = optional.get();
        var projectNames = projects.split(",");
        Arrays.stream(projectNames).forEach(p -> System.out.print(p + " "));

        Arrays.stream(projectNames).forEach(prName -> {
            var o = projectRepo.findAll().stream().filter(p -> p.getName().equals(prName)).findFirst();
            if (o.isEmpty()) {
                projectRepo.saveProjectWithNameOnly(new Project(-1, prName));
                o = projectRepo.findAll().stream().filter(p -> p.getName().equals(prName)).findFirst();
            }

            projectRepo.addDevMemberToProject(o.get(), devName);
        });
    }
}

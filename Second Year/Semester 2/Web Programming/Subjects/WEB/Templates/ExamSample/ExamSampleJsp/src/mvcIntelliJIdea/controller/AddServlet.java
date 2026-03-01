package mvcIntelliJIdea.controller;

import mvcIntelliJIdea.dbHelper.AddQuery;
import mvcIntelliJIdea.model.Asset;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddServlet extends HttpServlet {
    public AddServlet() { super(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get the data
        String json = req.getParameter("json");
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(json);
            JSONArray array = (JSONArray)obj;

            for (Object o : array) {
                Object obj2 = parser.parse((String) o);
                JSONObject jsonObject = (JSONObject) obj2;

                //Set up the asset object
                Asset asset = new Asset();
                int id = Integer.parseInt(jsonObject.get("id").toString());
                String name = jsonObject.get("name").toString();
                String description = jsonObject.get("description").toString();
                int value = Integer.parseInt(jsonObject.get("value").toString());

                asset.setUserId(id);
                asset.setName(name);
                asset.setDescription(description);
                asset.setValue(value);

                //Set up an addQuery object
                AddQuery aq = new AddQuery();

                //Pass the asset to addQuery to add to the database
                aq.doAdd(asset);
            }
        } catch(ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}

package web.exam.jspexam.model;

import org.json.JSONObject;

public class Document {
    private Integer id;
    private String title;
    private String templates;

    public Document(Integer id, String title, String templates) {
        this.id = id;
        this.title = title;
        this.templates = templates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplates() {
        return templates;
    }

    public void setTemplates(String templates) {
        this.templates = templates;
    }

    public JSONObject convertToJson() {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("id", id);
        jsonObj.put("title", title);
        jsonObj.put("templates", templates);

        return jsonObj;
    }
}

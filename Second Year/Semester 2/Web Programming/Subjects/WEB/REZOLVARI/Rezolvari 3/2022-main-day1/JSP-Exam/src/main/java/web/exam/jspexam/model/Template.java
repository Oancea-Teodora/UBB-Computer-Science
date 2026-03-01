package web.exam.jspexam.model;

public class Template {
    private Integer id;
    private String name;
    private String textContent;

    public Template(Integer id, String name, String textContent) {
        this.id = id;
        this.name = name;
        this.textContent = textContent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}

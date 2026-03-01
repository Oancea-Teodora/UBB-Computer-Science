package mvcIntelliJIdea.model;

public class Asset {

    private int id;
    private int userId;
    private String name;
    private String description;
    private int value;

    public Asset() {
        this.id = 0;
        this.userId = 0;
        this.name = "";
        this.description = "";
        this.value = 0;
    }

    public Asset(int id, int userId, String name, String description, int value) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}

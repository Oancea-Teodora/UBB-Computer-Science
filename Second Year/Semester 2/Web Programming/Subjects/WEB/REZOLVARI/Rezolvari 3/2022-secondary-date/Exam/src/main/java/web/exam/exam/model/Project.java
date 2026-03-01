package web.exam.exam.model;

import java.util.Arrays;

public class Project {
    private Integer id;
    private Integer managerId;
    private String name;
    private String desc;
    private String members;

    public Project(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project(Integer id, Integer managerId, String name, String desc, String members) {
        this.id = id;
        this.managerId = managerId;
        this.name = name;
        this.desc = desc;
        this.members = members;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public boolean isMemberOf(String user) {
        if (members == null) {
            return false;
        }

        var parts = members.split(",");
        return Arrays.asList(parts).contains(user);
    }
}

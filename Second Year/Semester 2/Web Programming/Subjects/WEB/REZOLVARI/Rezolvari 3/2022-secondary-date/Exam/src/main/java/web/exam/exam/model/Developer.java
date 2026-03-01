package web.exam.exam.model;

import java.util.Arrays;

public class Developer {
    private Integer id;
    private String name;
    private Integer age;
    private String skills;

    public Developer(Integer id, String name, Integer age, String skills) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.skills = skills;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public boolean hasSkill(String skill) {
        var parts = skills.split(",");
        return Arrays.asList(parts).contains(skill);
    }
}

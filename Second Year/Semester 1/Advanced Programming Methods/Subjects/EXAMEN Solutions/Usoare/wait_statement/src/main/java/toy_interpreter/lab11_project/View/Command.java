package toy_interpreter.lab11_project.View;

public abstract class Command {
    private String key;
    private String description;

    public Command(String k, String desc) {
        this.key = k;
        this.description = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDescription() {
        return this.description;
    }

    public abstract void execute();
}

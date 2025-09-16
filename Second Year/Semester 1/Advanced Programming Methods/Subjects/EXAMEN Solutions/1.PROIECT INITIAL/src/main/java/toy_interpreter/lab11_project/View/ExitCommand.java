package toy_interpreter.lab11_project.View;

public class ExitCommand extends Command{
    public ExitCommand(String k, String desc) {
        super(k, desc);
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}

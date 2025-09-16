package toy_interpreter.lab11_project.View;

import java.util.*;

public class TextMenu {
    private Map<String, Command> commands;

    public TextMenu() {
        this.commands = new HashMap<String, Command>();
    }

    public void addCommand(Command c) {
        this.commands.put(c.getKey(), c);
    }

    private void printMenu() {
        System.out.println("Available commands:");
        List<String> list = new ArrayList<String>(commands.keySet());
        list
                .stream()
                .sorted(Comparator.comparingInt(Integer::valueOf))
                .forEach(key -> {
                    String line = String.format("%4s: %s", key, this.commands.get(key).getDescription());
                    System.out.println(line);
                    }
                );
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            this.printMenu();
            System.out.print("Give a command: ");
            String key = scanner.nextLine();
            Command currentCommand = this.commands.get(key);
            if (currentCommand == null) {
                System.out.println("Invalid option.");
                continue;
            }
            currentCommand.execute();
        }
    }
}

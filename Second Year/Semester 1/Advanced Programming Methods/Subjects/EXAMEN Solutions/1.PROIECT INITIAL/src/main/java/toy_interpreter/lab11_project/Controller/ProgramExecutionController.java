package toy_interpreter.lab11_project.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import toy_interpreter.lab11_project.Model.ADT.*;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Statement.IStatement;
import toy_interpreter.lab11_project.Repository.IRepository;
import toy_interpreter.lab11_project.Repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.stream.Collectors;

public class ProgramExecutionController {

    @FXML
    private ListView<String> executionStackList;

    @FXML
    private ListView<Pair<String, String>> fileTableList;

    @FXML
    private TableView<Pair<Integer, String>> heapTable;

    @FXML
    private TableColumn<Pair<Integer, String>, Integer> heapTableAddressColumn;

    @FXML
    private TableColumn<Pair<Integer, String>, String> heapTableValueColumn;

    @FXML
    private TextField nrPrgsStatesTextField;

    @FXML
    private ListView<String> outList;

    @FXML
    private ListView<Integer> prgStateIdentifiers;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TableView<Pair<String, String>> symbolTable;

    @FXML
    private TableColumn<Pair<String, String>, String> symbolTableValueColumn;

    @FXML
    private TableColumn<Pair<String, String>, String> symbolTableVarNameColumn;

    private ProgramState currentProgramState;
    private IRepository repository;
    private Controller controller;

    @FXML
    void initialize() {
        this.heapTableAddressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        this.heapTableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        this.symbolTableVarNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        this.symbolTableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        this.prgStateIdentifiers.getSelectionModel().selectedItemProperty().addListener((observableValue, integer, t1) -> {
            if (t1 == null) {
                return;
            }
            try {
                this.currentProgramState = this.repository.getProgramStateById(t1);
            } catch (MyException e) {
                this.raiseAlert(e.getMessage());
            }
            // Repopulate the nodes which do not share data: the execution stack and the symbol table
            // Everything else (heap table, file table, output list, program state ids) is shared between program
            // states, so we don't need to repopulate them
            this.populateExecutionStack();
            this.populateSymbolTable();
        });
    }

    public void createProgramState(IStatement program, String repositoryFilePath) {
        ProgramState initialProgramState = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), program);
        this.currentProgramState = initialProgramState;
        this.repository = new Repository(initialProgramState, repositoryFilePath);
        this.controller = new Controller(this.repository);
        this.controller.createExecutor();
        this.populateEverything();
    }

    private boolean checkForUnfinishedProgramStates() {
        return this.repository.getPrgList().size() > 0;
    }

    @FXML
    void runOneStep(ActionEvent event) {
        if (!this.checkForUnfinishedProgramStates()) {
            this.raiseAlert("There are no unfinished program states.");
            return;
        }
        try {
            List<ProgramState> programStateList = this.controller.removeCompletedPrg(this.repository.getPrgList());
            this.controller.conservativeGarbageCollector(programStateList);
            this.controller.oneStepForAllPrg(programStateList);
            programStateList = this.controller.removeCompletedPrg(this.repository.getPrgList());
            if (programStateList.size() == 0) {
                this.controller.destroyExecutor();
                this.repository.setPrgList(programStateList);
            }
        } catch (InterruptedException e) {
            this.raiseAlert(e.getMessage());
        }
        this.populateEverything();
    }

    private void populateEverything() {
        this.populateProgramStateIdentifiersList();
        this.populateHeapTable();
        this.populateSymbolTable();
        this.populateFileTable();
        this.populateExecutionStack();
        this.populateOutList();
    }

    private void populateProgramStateIdentifiersList() {
        List<Integer> programStatesIds = this.repository.getPrgList().stream().map(ProgramState::getId).collect(Collectors.toList());
        this.prgStateIdentifiers.setItems(FXCollections.observableList(programStatesIds));
        this.updateNrPrgsStatesTextField();
    }

    private void updateNrPrgsStatesTextField() {
        this.nrPrgsStatesTextField.setText(String.valueOf(this.repository.getPrgList().size()));
    }

    private void populateHeapTable() {
        ArrayList<Pair<Integer, String>> heapElems = new ArrayList<>();
        this.currentProgramState.getHeapTable().getContent().forEach((key, value) -> heapElems.add(new Pair<>(key, value.toString())));
        this.heapTable.setItems(FXCollections.observableList(heapElems));
    }

    private void populateSymbolTable() {
        ArrayList<Pair<String, String>> symbolTableElems = new ArrayList<>();
        this.currentProgramState.getSymbolTable().getContent().forEach((key, value) -> symbolTableElems.add(new Pair<>(key, value.toString())));
        this.symbolTable.setItems(FXCollections.observableList(symbolTableElems));
    }

    private void populateFileTable() {
        ArrayList<Pair<String, String>> files = new ArrayList<>();
        this.currentProgramState.getFileTable().getContent().forEach((key, value) -> files.add(new Pair<>(key.getValue(), value.toString())));
        this.fileTableList.setItems(FXCollections.observableList(files));
    }

    private void populateExecutionStack() {
        Stack<IStatement> stack = this.currentProgramState.getExecutionStack().getContent();
        ArrayList<String> stackElems = new ArrayList<>();
        ListIterator<IStatement> stackIterator = stack.listIterator(stack.size());
        while (stackIterator.hasPrevious()) {
            stackElems.add(stackIterator.previous().toString());
        }
        this.executionStackList.setItems(FXCollections.observableList(stackElems));
    }

    private void populateOutList() {
        ArrayList<String> output = new ArrayList<>();
        this.currentProgramState.getOutput().getElems().forEach(e -> output.add(e.toString()));
        this.outList.setItems(FXCollections.observableList(output));
    }

    private void raiseAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }
}

package toy_interpreter.lab11_project.Model.ADT;

import javafx.util.Pair;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;

import java.util.List;

public interface ISemaphoreTable {
    int addNewSemaphoreEntry( Pair<List<Integer>, Integer> value);
    public boolean contains(Integer key);
    public  Pair<List<Integer>, Integer> getValue(Integer key);
}

package toy_interpreter.lab11_project.Model.ADT;

import javafx.util.Pair;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MySemaphoreTable implements ISemaphoreTable {

    protected Map<Integer,  Pair<List<Integer>, Integer>> semTable;
    private int currentPos;

    public MySemaphoreTable()
    {
        this.semTable = new ConcurrentHashMap<>();
        this.currentPos = 0;
    }

    private synchronized int getCurrentPos()
    {
        this.currentPos++;
        return currentPos;
    }

    //@Override
    public int addNewSemaphoreEntry( Pair<List<Integer>, Integer> value) {
        int pos = getCurrentPos();
        this.semTable.put(pos, value);
        return pos;
    }

    public boolean contains(Integer key)
    {
        return this.semTable.containsKey(key);
    }

    public  Pair<List<Integer>, Integer> getValue(Integer key)
    {
        return this.semTable.get(key);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Integer key : this.semTable.keySet()) {
            str.append(key).append(" -> ").append("(").append(this.semTable.get(key).getKey()).append(",").append(this.semTable.get(key).getValue()).append(")\n");
        }
        return str.toString();
    }
}

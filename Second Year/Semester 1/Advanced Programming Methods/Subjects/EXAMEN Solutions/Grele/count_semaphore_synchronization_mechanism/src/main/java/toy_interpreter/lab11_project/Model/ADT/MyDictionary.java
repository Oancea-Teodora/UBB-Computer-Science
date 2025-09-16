package toy_interpreter.lab11_project.Model.ADT;

import toy_interpreter.lab11_project.Model.Exceptions.MyException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MyDictionary<K, V> implements IDictionary<K, V>{
    protected HashMap<K, V> elems;

    public MyDictionary() {
        this.elems = new HashMap<K, V>();
    }

    @Override
    public void addKeyValuePair(K newKey, V newValue) {
        this.elems.put(newKey, newValue);   // returns the previous V value if K was used, null otherwise
    }

    @Override
    public V lookUp(K key) throws MyException {
        if (!this.elems.containsKey(key)) {
            throw new MyException("Failed to get value: the given key is not in the dictionary.");
        }
        return this.elems.get(key);
    }

    @Override
    public V removeByKey(K key) throws MyException {
        V value = this.elems.remove(key);
        if (value != null) {
            return value;
        }
        else {
            throw new MyException("Failed to remove key-value pair: The given key is not in the dictionary");
        }
    }

    @Override
    public boolean isDefined(K key) {
        return this.elems.containsKey(key);
    }

    @Override
    public Map<K, V> getContent() {
        return this.elems;
    }

    @Override
    public void setContent(Map<K, V> newContent) {
        this.elems.clear();
        this.elems.putAll(newContent);
    }

    @Override
    public IDictionary<K, V> shallowCopy() {
        Map<K, V> dictionaryContent = this.getContent();
        MyDictionary<K, V> newDictionary = new MyDictionary<>();
        newDictionary.setContent(dictionaryContent.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        return newDictionary;
    }

    @Override
    public String toString() {
        StringBuilder elemsInString = new StringBuilder();
        int i = 0;
        for (Map.Entry<K, V> entry: this.elems.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            elemsInString.append(key.toString());
            elemsInString.append(" --> ");
            elemsInString.append(value.toString());
            if (i < this.elems.size() - 1) {
                elemsInString.append("\n");
            }
            i += 1;
        }
        return elemsInString.toString();
    }
}

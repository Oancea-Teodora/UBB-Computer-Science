package com.example.repeat_until.Model.ADT;

import com.example.repeat_until.Model.Exceptions.MyException;

import java.util.Map;

public interface IDictionary<K, V> {
    void addKeyValuePair(K newKey, V newValue);
    V lookUp(K key) throws MyException;
    V removeByKey(K key) throws MyException;
    boolean isDefined(K key);
    Map<K, V> getContent();
    void setContent(Map<K, V> newContent);
    IDictionary<K, V> shallowCopy();
}

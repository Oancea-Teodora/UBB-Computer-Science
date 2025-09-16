package com.example.switch_statement.Model.Value;

import com.example.switch_statement.Model.Type.IType;
import com.example.switch_statement.Model.Type.StringType;

import java.util.Objects;

public class StringValue implements IValue{
    private String value;

    public StringValue(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringValue that = (StringValue) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

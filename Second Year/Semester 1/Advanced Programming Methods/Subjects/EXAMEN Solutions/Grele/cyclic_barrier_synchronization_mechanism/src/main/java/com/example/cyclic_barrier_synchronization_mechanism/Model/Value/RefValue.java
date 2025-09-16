package com.example.cyclic_barrier_synchronization_mechanism.Model.Value;

import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IType;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.RefType;

public class RefValue implements IValue{
    int address;
    IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return this.address;
    }

    public IType getLocationType() {
        return this.locationType;
    }

    @Override
    public IType getType() {
        return new RefType(this.locationType);
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(this.address, this.locationType);
    }

    @Override
    public String toString() { return "(" + this.address + ", " + this.locationType.toString() + ")"; }
}

package com.eres.telegram.fsm;

public interface IStates {
    IStates getPrevious();
    IStates getNext();
    StatesGroups getStateGroup();
}

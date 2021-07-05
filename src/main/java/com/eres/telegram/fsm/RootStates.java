package com.eres.telegram.fsm;

public enum RootStates implements IStates {
    INIT {
        public IStates getPrevious() {
            return INIT;
        }
        public IStates getNext() {
            return INIT; }
    };
    public StatesGroups getStateGroup(){
        return StatesGroups.SETTINGS;
    }
}

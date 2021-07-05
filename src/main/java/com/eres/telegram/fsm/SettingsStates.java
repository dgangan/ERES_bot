package com.eres.telegram.fsm;

public enum SettingsStates implements IStates{

    SETTINGS_MAIN{
        public IStates getPrevious(){
            return RootStates.INIT;
        }
        public IStates getNext(){
            return SETTINGS_REGION;
        }
    },
    SETTINGS_REGION{
        public IStates getPrevious(){
            return SETTINGS_MAIN;
        }
        public IStates getNext(){
            return SETTINGS_CITY;
        }
    },
    SETTINGS_CITY{
        public IStates getPrevious(){
            return SETTINGS_MAIN;
        }
        public IStates getNext(){
            return SETTINGS_STREET;
        }
    },
    SETTINGS_STREET{
        public IStates getPrevious(){
            return SETTINGS_MAIN;
        }
        public IStates getNext(){
            return RootStates.INIT;
        }
    };

    public StatesGroups getStateGroup(){
        return StatesGroups.SETTINGS;
    }
}

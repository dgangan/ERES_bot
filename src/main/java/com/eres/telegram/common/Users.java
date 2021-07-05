package com.eres.telegram.common;

import com.eres.telegram.fsm.IStates;
import com.eres.telegram.fsm.RootStates;
import com.eres.telegram.fsm.SettingsStates;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private static final Map<Long, IStates> userMap= new HashMap<>();

    public static IStates getState(Long userId){
        return userMap.get(userId);
    }

    public static IStates setPreviousState(Long userId) {
        IStates previousState = userMap.get(userId).getPrevious();
        userMap.put(userId, previousState);
        return previousState;
    }

    public static IStates setNextState(Long userId) {
        IStates previousState = userMap.get(userId).getNext();
        userMap.put(userId, previousState);
        return previousState;
    }

    public static IStates setState(Long userId, IStates state) {
        userMap.put(userId, state);
        return state;
    }

    public static IStates resetState(Long userId) {
        IStates rootState = RootStates.INIT;
        userMap.put(userId, rootState);
        return rootState;
    }
}

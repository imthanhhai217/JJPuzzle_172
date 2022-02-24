package com.pachia.comon.object;

public class EventObj {
    String message;
    EventAction action;

    public enum EventAction {
        UNDEFINDE,
        FORCE_LOGOUT,
        SHOW_TOAST
    }

    public String getMessage() {
        if (message != null)
            return message;
        else return "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventAction getAction() {
        if (action == null)
            return EventAction.UNDEFINDE;
        else return action;
    }

    public void setAction(EventAction action) {
        this.action = action;
    }
}

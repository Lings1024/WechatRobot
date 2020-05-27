package com.k.wechat.robot.action;


public class ActionAssertEntity {
    private int eventType;
    private String className;
    private String[] classNames;
    private Action action;

    public ActionAssertEntity className(String className) {
        this.className = className;
        return this;
    }

    public int getEventType() {
        return eventType;
    }

    public ActionAssertEntity classNames(String[] classNames) {
        this.classNames = classNames;
        return this;
    }

    public ActionAssertEntity action(Action action) {
        this.action = action;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public String[] getClassNames() {
        return classNames;
    }

    public Action getAction() {
        return action;
    }

    public ActionAssertEntity eventType(int eventType) {
        this.eventType = eventType;
        return this;
    }
}

package com.harshvardhanbajpai;

public class GetSetStateList {
    public GetSetStateList(String id, String name) {
        this.id = id;
        Name = name;
    }

    String id , Name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

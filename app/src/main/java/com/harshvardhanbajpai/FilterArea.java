package com.harshvardhanbajpai;

public class FilterArea {
    String id,year;


    public FilterArea( String year) {


        this.year = year;
    }


    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    private boolean is_select;

    public boolean isIs_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

}

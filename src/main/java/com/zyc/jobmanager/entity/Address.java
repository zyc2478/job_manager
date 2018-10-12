package com.zyc.jobmanager.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Address {
    private String lines;
    private String city;
    private String state;
    private Integer postal;

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPostal() {
        return postal;
    }

    public void setPostal(Integer postal) {
        this.postal = postal;
    }
}
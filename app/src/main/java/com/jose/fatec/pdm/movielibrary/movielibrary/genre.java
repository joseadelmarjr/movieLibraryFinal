package com.jose.fatec.pdm.movielibrary.movielibrary;

import java.io.Serializable;

public class genre implements Serializable {
    int id;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}

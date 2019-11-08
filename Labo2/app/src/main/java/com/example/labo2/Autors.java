package com.example.labo2;

public class Autors {
    private String firstname;
    private String lastname;
    private int id;

    public Autors(String firstname, String lastname, int id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
}

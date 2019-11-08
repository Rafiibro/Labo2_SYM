package com.example.labo2;

/* Definition d'une personne, utilisé dans GraphQL*/
public class Personne {
    private String name = "";
    private String firstname = "";
    private String middlename = "";
    private String gender = "";
    private String phone = "";
    private String phoneType = "";

    public Personne(String name, String firstname, String middlename, String gender, String phone, String phoneType) {
        this.name = name;
        this.firstname = firstname;
        this.middlename = middlename;
        this.gender = gender;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public Personne(String name, String firstname, String gender, String phone, String phoneType) {
        this.name = name;
        this.firstname = firstname;
        this.gender = gender;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneType() {
        return phoneType;
    }


}

package com.keyur.raftis;

public class UserHelperClass {
    String email,name,number;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserHelperClass() {
    }

    public UserHelperClass(String email, String name, String number) {
        this.email = email;
        this.name = name;
        this.number = number;
    }
}

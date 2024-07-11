package com.application.tourlica.data;

import com.application.tourlica.common.GenderType;
import com.application.tourlica.common.UserType;

import java.util.Date;

public class UserData {

    private static UserData userData;

    public static UserData getInstance() {
        if (userData == null) {
            userData = new UserData();
        }

        return userData;
    }

    private UserType type;

    private String password;

    private String name;

    private String email;

    private Date birthday;

    private GenderType gender;

    public void setType(UserType type) {
        this.type = type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public UserType getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public GenderType getGender() {
        return gender;
    }
}

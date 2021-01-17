package com.eventor.payload.response;

import java.util.Date;
import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String profileImg;
    private Date birthday;
    private String gender;
    private String phone;
    private String nationality;
    private List<String> roles;


    public JwtResponse(String accessToken, int id, String firstName, String lastName, String username,
                       String email, String profileImg, Date birthday, String gender, String phone,
                       String nationality, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.profileImg = profileImg;
        this.birthday = birthday;
        this.gender = gender;
        this.phone = phone;
        this.nationality = nationality;
        this.roles = roles;
    }


    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_img() {
        return profileImg;
    }

    public void setProfile_img(String profileImg) {
        this.profileImg = profileImg;
    }

    public Date getBirthday() { return birthday; }

    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getNationality() { return nationality; }

    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}

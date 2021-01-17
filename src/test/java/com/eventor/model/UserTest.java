package com.eventor.model;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    String firstName = "Gray";
    String lastName = "Bill";
    String email = "grayb@gmail.com";
    String username = "grayb";
    String password = "a12347";
    String gender = "Male";
    String phone = "7778 88 456";
    String nationality = "American";
    String profileImg = "default.png";

    String day = "1989-11-14";
    Date birthday = new SimpleDateFormat("YYYY-MM-dd").parse(day);

//    Set<Role> roles = new HashSet<>();
//    Role roleUser = new Role("ROLE_USER");
//    roles.add(roleUser);

    User emptyUser = new User();
    User registeredUser = new User(email, username, password);

    public UserTest() throws ParseException {
    }

    @Test
    void newUser_EmptyConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void newUser_ParametersInConstructor() {
        User user = new User(email, username, password);
        assertEquals(email, user.getEmail());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
    }

    @Test
    void setFirstName()
    {
        emptyUser.setFirstName(firstName);
        assertEquals(firstName, emptyUser.getFirstName());
    }

    @Test
    void getFirstName()
    {
        emptyUser.setFirstName(firstName);
        assertEquals(firstName, emptyUser.getFirstName());
    }

    @Test
    void setLastName()
    {
        emptyUser.setLastName(lastName);
        assertEquals(lastName, emptyUser.getLastName());
    }

    @Test
    void getLastName()
    {
        emptyUser.setLastName(lastName);
        assertEquals(lastName, emptyUser.getLastName());
    }

    @Test
    void setEmail()
    {
        emptyUser.setEmail(email);
        assertEquals(email, emptyUser.getEmail());
    }

    @Test
    void getEmail()
    {
        assertEquals(email, registeredUser.getEmail());
    }

    @Test
    void setUsername()
    {
        emptyUser.setUsername(username);
        assertEquals(username, emptyUser.getUsername());
    }

    @Test
    void getUsername()
    {
        assertEquals(username, registeredUser.getUsername());
    }

    @Test
    void setPassword()
    {
        emptyUser.setPassword(password);
        assertEquals(password, emptyUser.getPassword());
    }

    @Test
    void getPassword()
    {
        assertEquals(password, registeredUser.getPassword());
    }

    @Test
    void setGender()
    {
        emptyUser.setGender(gender);
        assertEquals(gender, emptyUser.getGender());
    }

    @Test
    void getGender()
    {
        emptyUser.setGender(gender);
        assertEquals(gender, emptyUser.getGender());
    }

    @Test
    void setPhone()
    {
        emptyUser.setPhone(phone);
        assertEquals(phone, emptyUser.getPhone());
    }

    @Test
    void getPhone()
    {
        emptyUser.setPhone(phone);
        assertEquals(phone, emptyUser.getPhone());
    }

    @Test
    void setNationality()
    {
        emptyUser.setNationality(nationality);
        assertEquals(nationality, emptyUser.getNationality());
    }

    @Test
    void getNationality()
    {
        emptyUser.setNationality(nationality);
        assertEquals(nationality, emptyUser.getNationality());
    }

    @Test
    void setBirthday()
    {
        emptyUser.setBirthday(birthday);
        assertEquals(birthday, emptyUser.getBirthday());
    }

    @Test
    void getBirthday()
    {
        emptyUser.setBirthday(birthday);
        assertEquals(birthday, emptyUser.getBirthday());
    }

    @Test
    void setProfileImg()
    {
        emptyUser.setProfile_img(profileImg);
        assertEquals(profileImg, emptyUser.getProfileImg());

    }

    @Test
    void getProfileImg()
    {
        emptyUser.setProfile_img(profileImg);
        String img = emptyUser.getProfileImg();
        assertEquals(profileImg, img);
    }

    @Test
    void testToString() {
        assertTrue(registeredUser.toString().contains(username));
        assertTrue(registeredUser.toString().contains(email));
    }
}

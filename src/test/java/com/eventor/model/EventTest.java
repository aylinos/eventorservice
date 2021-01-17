package com.eventor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    User creator = new User("gray@gmail.com", "grayb", "a12347");
    String title = "Home workout";
    String description = "Let's sweat this weekend!";
    Boolean isPublished = false;
    Boolean isPrivate = false;

    @Test
    void newEvent_EmptyConstructor() {
        Event event = new Event();
        assertNotNull(event);
    }

    @Test
    void newEvent_ParametersInConstructor() {
        Event event = new Event(creator, title, description, isPublished, isPrivate);
        assertEquals(creator, event.getCreator());
        assertEquals(title, event.getTitle());
        assertEquals(description, event.getInformation());
        assertFalse(event.isPublished());
        assertFalse(event.isPrivate());
    }

    @Test
    void setTitle()
    {
        Event event = new Event();
        String setTitle = "Unit testing workshop";
        event.setTitle(setTitle);
        assertEquals(setTitle, event.getTitle());
    }

    @Test
    void getTitle()
    {
        Event event = new Event(creator, title, description, isPublished, isPrivate);
        assertEquals(title, event.getTitle());
    }

    @Test
    void setInformation() {
        Event event = new Event();
        String information = "Learn unit testing in our 1-hour-long workshop";
        event.setInformation(information);
        assertEquals(information, event.getInformation());
    }

    @Test
    void getInformation() {
        Event event = new Event(creator, title, description, isPublished, isPrivate);
        assertEquals(description, event.getInformation());
    }

    @Test
    void setPublished() {
        Event event = new Event();
        event.setPublished(true);
        assertTrue(event.isPublished());
    }

    @Test
    void isPublished() {
        Event event = new Event(creator, title, description, isPublished, isPrivate);
        assertFalse(event.isPublished());
    }

    @Test
    void setPrivate() {
        Event event = new Event();
        event.setPrivate(true);
        assertTrue(event.isPrivate());
    }

    @Test
    void isPrivate() {
        Event event = new Event(creator, title, description, isPublished, isPrivate);
        assertFalse(event.isPrivate());
    }

    @Test
    void testToString() {
        Event event = new Event(title, description, isPublished);
        assertTrue(event.toString().contains(title));
        assertTrue(event.toString().contains(description));
        assertTrue(event.toString().contains(isPublished.toString()));
        assertTrue(event.toString().contains(isPrivate.toString()));
    }
}
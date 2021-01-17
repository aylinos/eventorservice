package com.eventor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileInfoTest {

    String name = "default_avatar.png";
    String url = "http://localhost:8080/eventor/files/file/default_avatar.png";

    FileInfo fileInfo = new FileInfo(name, url);
    @Test
    void newFileInfo_constructor()
    {
        assertNotNull(fileInfo);
    }

    @Test
    void setName()
    {
        fileInfo.setName("default.png");

        assertEquals("default.png", fileInfo.getName());
    }

    @Test
    void getName()
    {
        assertEquals(name, fileInfo.getName());
    }

    @Test
    void setUrl()
    {
        fileInfo.setUrl("http://localhost:8080/eventor/files/file/default.png");

        assertEquals("http://localhost:8080/eventor/files/file/default.png", fileInfo.getUrl());
    }

    @Test
    void getUrl()
    {
        assertEquals(url, fileInfo.getUrl());
    }
}

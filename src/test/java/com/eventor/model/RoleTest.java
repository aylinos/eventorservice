package com.eventor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoleTest {

    int id = 1;
    String name = "USER";

    Role emptyRole = new Role();
    Role newRole = new Role(name);

    @Test
    void newRole_emptyConstructor()
    {
        assertNotNull(emptyRole);
    }

    @Test
    void newRole_ParameterInConstructor()
    {
        assertNotNull(newRole);
    }

    @Test
    void setId()
    {
        emptyRole.setId(id);
        assertEquals(id, emptyRole.getId());
    }

    @Test
    void getId()
    {
        assertEquals(null, emptyRole.getId());
    }

    @Test
    void setName()
    {
        emptyRole.setName(name);
        assertEquals(name, emptyRole.getName());
    }

    @Test
    void getName()
    {
        assertEquals(null, emptyRole.getName());
    }
}

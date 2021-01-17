package com.eventor.repository;

import com.eventor.model.Event;
import com.eventor.model.Event;
import com.eventor.repository.EventRepository;
import com.eventor.security.services.FilesStorageService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventRepositoryTests {

    @Autowired
    private EventRepository eventRepository;

    private static long unpublishedEvent_id;
    private static long publishedEvent_id;

    @Test
    @Rollback(value = false)
    @Order(1)
    void testCreateEvent()
    {
        Event unpublishedEvent = new Event("Java workshop", "Suitable for beginners", false);
        Event firstAdded = eventRepository.save(unpublishedEvent);
        unpublishedEvent_id = firstAdded.getId();

        Event publishedEvent = new Event("10km cycling", "Join our cycling marathon! Bring your own bike, family, friends and have fun!", true);
        Event secondAdded = eventRepository.save(publishedEvent);
        publishedEvent_id = secondAdded.getId();

        assertNotNull(firstAdded);
        assertNotNull(secondAdded);
    }

    @Test
    @Order(2)
    void testFindByPublishedExists()
    {
        List<Event> events = eventRepository.findByPublished(true);

        Event foundEvent = null;
        for (Event event:events
             ) {if(event.getId() == publishedEvent_id){
                 foundEvent = event;
                 break;
        }

        }
        assertNotNull(foundEvent);
    }
//
    @Test
    @Order(3)
    void testFindByPublishedNotExists()
    {
        List<Event> events = eventRepository.findByPublished(true);

        Event foundEvent = null;
        for (Event event:events
        ) {if(event.getId() == unpublishedEvent_id){
            foundEvent = event;
            break;
        }

        }
        assertNull(foundEvent);
    }
//
    @Test
    @Order(4)
    void testFindByTitleExists()
    {
        String title = "Java workshop";
        List<Event> events = eventRepository.findByTitleContaining(title);

        Event foundEvent = null;
        for (Event event:events
        ) {
            if (event.getId() == unpublishedEvent_id) {
                foundEvent = event;
                break;
            }
        }
        assertNotNull(foundEvent);

        assertEquals(title, foundEvent.getTitle());
    }

    @Test
    @Order(5)
    void testFindByTitlePartial() {
        String title = "workshop";
        List<Event> events = eventRepository.findByTitleContaining(title);

        Event foundEvent = null;
        for (Event event : events
        ) {
            if (event.getId() == unpublishedEvent_id) {
                foundEvent = event;
                break;
            }
        }
        assertNotNull(foundEvent);
    }

    @Test
    @Order(6)
    void testFindByTitleNotExists()
    {
        String title = "Java workshopp";
        List<Event> events = eventRepository.findByTitleContaining(title);

        assertEquals(0, events.size());
    }

    @Test
    @Order(7)
    void testFindByIdExists()
    {
        Optional<Event> foundEvent = eventRepository.findById(publishedEvent_id);
        assertFalse(foundEvent.isEmpty());
        assertEquals("10km cycling", foundEvent.get().getTitle());
    }

    @Test
    @Order(8)
    void testFindByIdNotExists()
    {
        Optional<Event> foundEvent = eventRepository.findById(publishedEvent_id+1);
        assertTrue(foundEvent.isEmpty());
    }

    @Test
    @Rollback(value = false)
    @Order(9)
    void testUpdateEvent()
    {
        Optional<Event> eventData = eventRepository.findById(publishedEvent_id);
        Event eventToUpdate = eventData.get();
        eventToUpdate.setTitle("Update test");
        eventToUpdate.setInformation("This is a test!");
        eventToUpdate.setPublished(false);
        eventRepository.save(eventToUpdate);

        Event updatedEvent = eventRepository.findById(publishedEvent_id).get();
        assertEquals("Update test", updatedEvent.getTitle());
    }

    @Test
    @Order(10)
    void testListEvents()
    {
        List<Event> events = eventRepository.findAll();

//        for(Event e:events)
//        {
//            System.out.println(e.toString());
//        }

        assertTrue(events.size() > 1); //at least 2 as newly added in this test class
    }

    @Test
    @Rollback(value = false)
    @Order(11)
    void testDeleteEvent()
    {
        boolean existsBeforeDelete = eventRepository.findById(publishedEvent_id).isPresent();
        eventRepository.deleteById(publishedEvent_id);
        boolean notExistsAfterDelete = eventRepository.findById(publishedEvent_id).isPresent();

        assertTrue(existsBeforeDelete);
        assertFalse(notExistsAfterDelete);
    }
}
package com.eventor.controller;

import com.eventor.model.Event;
import com.eventor.model.User;
import com.eventor.payload.response.ResponseWithError;
import com.eventor.payload.response.MessageResponse;
import com.eventor.repository.EventRepository;
import com.eventor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/eventor")
public class EventController {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(required = false) String title) {
        try {
            List<Event> events = new ArrayList<>();

            if (title == null)
                eventRepository.findAll().forEach(events::add);
            else
                eventRepository.findByTitleContaining(title).forEach(events::add);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/events/my")
    public ResponseEntity<ResponseWithError<List<Event>>> getMyEvents(@RequestParam("creator") int creatorID) {
        Optional<User> creatorOpt;
        try {
            creatorOpt = userRepository.findById(creatorID);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "User repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (!creatorOpt.isPresent()) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "User not found"), HttpStatus.NOT_FOUND);
        }

        User creator = creatorOpt.get();
        List<Event> events = new ArrayList<>();

        try {
            eventRepository.findByCreator(creator).forEach(events::add);
        } catch (DataAccessException e)
        {
            return new ResponseEntity<>(new ResponseWithError<>(null, "Event repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (events.isEmpty()) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "No events found"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseWithError<>(events, null), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id) {
        Optional<Event> eventData = eventRepository.findById(id);

        if (eventData.isPresent()) {
            return new ResponseEntity<>(eventData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            if(event.getTitle().equals("")) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Please, fill in a title for your event!"));
            }
            Event newEvent = eventRepository
                    .save(new Event(event.getCreator(), event.getTitle(), event.getInformation(), false, event.isPrivate()));
            return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable("id") long id, @RequestBody Event event) {
        try {
            if (event.getTitle().equals("")) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Event does not have a title!"));
            }

            Optional<Event> eventData = eventRepository.findById(id);

            if (eventData.isPresent()) {
                Event newEvent = eventData.get();
                newEvent.setTitle(event.getTitle());
                newEvent.setStartDate(event.getStartDate());
                newEvent.setInformation(event.getInformation());
                if(!newEvent.isPublished() && event.isPublished())
                {
                    newEvent.setPublishDate(event.getPublishDate());
                }
                else if(newEvent.isPublished() && !event.isPublished())
                {
                    newEvent.setPublishDate(null);
                }
                newEvent.setPublished(event.isPublished());
                newEvent.setPrivate(event.isPrivate());
                return new ResponseEntity<>(eventRepository.save(newEvent), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping("/events/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id) {
        try {
            eventRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping("/events")
    public ResponseEntity<?> deleteAllEvents() {
        try {
            eventRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/events/published")
    public ResponseEntity<List<Event>> findByPublished() {
        try {
            List<Event> events = eventRepository.findByPublished(true);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.eventor.controller;

import com.eventor.exception.ResourceNotFound;
import com.eventor.model.Event;
import com.eventor.model.Subscription;
import com.eventor.model.User;
import com.eventor.payload.response.DateWithSubscriptions;
import com.eventor.payload.response.MessageResponse;
import com.eventor.repository.EventRepository;
import com.eventor.repository.SubscriptionRepository;
import com.eventor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/eventor/subscriptions")
public class SubscriptionController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    public static final String USER_NOT_FOUND_EXCEPTION_CONTENT = "User not found:";

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/subscribe/{userId}")
    public ResponseEntity<MessageResponse> subscribeToEvent(@PathVariable int userId, @RequestBody Event event) {
        String message = "";
        try {
            Optional<User> userData = userRepository.findById(userId);

            if (userData.isPresent()) {
                User currentUser = userData.get();
                Optional<Event> eventData = eventRepository.findById(event.getId());

                if(eventData.isPresent())
                {
                    Event eventToSubscribe = eventData.get();
                    eventToSubscribe.setLastSubscription(event.getLastSubscription());
                    eventRepository.save(eventToSubscribe);

                    Set<Subscription> eventSubscriptions = eventToSubscribe.getSubscriptions();
                    Subscription subscription = null;
                    for(Subscription s : eventSubscriptions)
                    {
                        if(s.getUser().getId() == currentUser.getId())
                        {
                            subscription = s;
                            break;
                        }
                    }
                    if (subscription != null) {
                        subscriptionRepository.delete(subscription);
                    }

                    Subscription newSubscription = new Subscription(eventToSubscribe, currentUser, event.getLastSubscription());
                    subscriptionRepository.save(newSubscription);

                    message = "You subscribed successfully!";
                    return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
                } else {
                    message = "Event not found!";
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
                }
            } else {
                message = "User not found!";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
            }
        } catch (Exception e) {
            message = "Something went wrong!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(message));
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Set<Event>> getSubscriptionsForUser(@PathVariable int userId) {
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND_EXCEPTION_CONTENT + userId));


            Set<Subscription> subscriptions = currentUser.getSubscriptions();
            if(subscriptions.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Set<Event> events = new HashSet<>();
            for(Subscription s : subscriptions)
            {
                if(!s.getUnsubscribed())
                    events.add(s.getEvent());
            }
            return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/unsubscribe/{userId}")
    public ResponseEntity<MessageResponse> unsubscribeToEvent(@PathVariable int userId, @RequestBody Event event) {
        String message = "";
        try {
            Optional<User> userData = userRepository.findById(userId);

            if (userData.isPresent()) {
                User currentUser = userData.get();
                Optional<Event> eventData = eventRepository.findById(event.getId());

                if(eventData.isPresent()) {
                    Event eventToUnsubscribe = eventData.get();
                    Set<Subscription> userSubscriptions = currentUser.getSubscriptions();
                    Subscription subscription = null;
                    for(Subscription s : userSubscriptions)
                    {
                        if(s.getEvent().getId() == eventToUnsubscribe.getId())
                        {
                            subscription = s;
                            break;
                        }
                    }
                    if (subscription != null) {
                        subscription.setUnsubscribed(true);
                        subscription.setUnsubscriptionDate(new Date(System.currentTimeMillis()));
                        subscriptionRepository.save(subscription);
//                        subscriptionRepository.delete(subscription);
                        message = "You unsubscribed successfully!";
                        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
                    }
                    else {
                        message = "You are not following this event!";
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
                    }
                }else {
                    message = "Event not found!";
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
                }
            } else {
                message = "User not found!";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
            }
        } catch (Exception e) {
            message = "Something went wrong!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(message));
        }
    }

//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    @GetMapping("/date/{eventId}")
//    public ResponseEntity<Integer> getSubscriptionsForDate(@PathVariable long eventId) {
//        Optional<Event> event = eventRepository.findById(eventId);
//        if(event.isPresent()) {
//            Event currentEvent = event.get();
//
//            Integer nr = 0;
//            Set<Subscription> subscriptions = currentEvent.getSubscriptions();
//            if (subscriptions.size() > 0) {
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//                for (Subscription s : subscriptions) {
//                    if (df.format(s.getSubscriptionDate()).compareTo(df.format(currentEvent.getLastSubscription())) == 0) {
//                        nr++;
//                    }
//                }
//            }
//
//            return new ResponseEntity<>(nr, HttpStatus.OK);
//        }return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/datee/{eventId}")
    public ResponseEntity<Integer> getNewSubscriptionsByDate(@PathVariable("eventId") long eventId, @RequestParam("date") String chosenDate) throws ParseException {
        Optional<Event> event = eventRepository.findById(eventId);
        if(event.isPresent()) {
            Event currentEvent = event.get();

            Integer nr = 0;
            Set<Subscription> subscriptions = currentEvent.getSubscriptions();
            if (subscriptions.size() > 0) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                for (Subscription s : subscriptions) {
                    if (df.format(s.getSubscriptionDate()).compareTo(df.format(df.parse(chosenDate))) == 0) {
                        if(!s.getUnsubscribed() || df.format(s.getUnsubscriptionDate()).compareTo(df.format(df.parse(chosenDate))) > 0)
                            nr++;
                    }
                }
            }
            return new ResponseEntity<>(nr, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/total/{eventId}")
    public ResponseEntity<Integer> getTotalSubscriptions(@PathVariable("eventId") long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if(event.isPresent()) {
            Event currentEvent = event.get();

            Set<Subscription> subscriptions = currentEvent.getSubscriptions();
            Integer nr = 0;
            for(Subscription s : subscriptions)
            {
                if(!s.getUnsubscribed())
                    nr++;
            }

            return new ResponseEntity<>(nr, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/statistics/{eventId}")
    public ResponseEntity<List<DateWithSubscriptions>> getStatistics(@PathVariable("eventId") long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if(event.isPresent()) {
            Event currentEvent = event.get();

            Set<Subscription> subscriptions = currentEvent.getSubscriptions();
            List<DateWithSubscriptions> result = new ArrayList<>();

            if (subscriptions.size() > 0) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date(System.currentTimeMillis());

                List<Date> datesSincePublishDate = getDatesBetween(currentEvent.getPublishDate(), currentDate);
                Integer nr = 0;

                for (Date d : datesSincePublishDate) {
                    for (Subscription s : subscriptions) {
                        if (df.format(s.getSubscriptionDate()).compareTo(df.format(d)) == 0 ||
                            df.format(s.getSubscriptionDate()).compareTo(df.format(d)) < 0) {
                            if(!s.getUnsubscribed() || (
                                    df.format(s.getUnsubscriptionDate()).compareTo(df.format(d)) > 0
                                    ))
                                nr++;
                        }
                    }
                    result.add(new DateWithSubscriptions(df.format(d), nr));
                    nr=0;
                }
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public static List<Date> getDatesBetween( Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        datesInRange.add(endDate);

        return datesInRange;
    }
}

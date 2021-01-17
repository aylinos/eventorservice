package com.eventor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Entity
@JsonIgnoreProperties("subscriptions")
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonIgnoreProperties("subscribedEvents")
    private User creator;

    @Valid
    @NotBlank
    @Column(name = "title")
    private String title;

    private Date startDate;

    @Column(name = "information")
    private String information;

    @Column(name = "published")
    private boolean published;

    private Date publishDate;

    @Column(name = "private", nullable = false, columnDefinition = "boolean default true")
    private boolean isPrivate;

    public Date getLastSubscription() {
        return lastSubscription;
    }

    public void setLastSubscription(Date lastSubscription) {
        this.lastSubscription = lastSubscription;
    }

    @Column(name = "last_subscription")
    private Date lastSubscription;

    @OneToMany(mappedBy = "event")
    Set<Subscription> subscriptions;

    public Event() {

    }

//    Old one without adding user - used in the implemented tests
    public Event(String title, String information, boolean published) {
        this.title = title;
        this.information = information;
        this.published = published;
    }

    //Newest
    public Event(User user, String title, String information, boolean published, boolean isPrivate) {
        this.creator = user;
        this.title = title;
        this.information = information;
        this.published = published;
        this.isPrivate = isPrivate;
    }

    //Needed for tests:
    public Event(long id, User user, String title, String information, boolean published, boolean isPrivate) {
        this.id = id;
        this.creator = user;
        this.title = title;
        this.information = information;
        this.published = published;
        this.isPrivate = isPrivate;
    }

    //Needed for tests:
    public Event(int id, String title, String information, boolean published) {
        this.id = id;
        this.title = title;
        this.information = information;
        this.published = published;
    }

    public long getId() {
        return this.id;
    }

    public User getCreator() { return creator; }

    public void setCreator(User creator) { this.creator = creator; }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public String getInformation() {
        return this.information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isPublished() {
        return this.published;
    }

    public void setPublished(boolean isPublished) {
        this.published = isPublished;
    }

    public boolean isPrivate() { return this.isPrivate; }

    public void setPrivate(boolean aPrivate) { this.isPrivate = aPrivate; }

    public Date getPublishDate() { return publishDate; }

    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        return "Event [id= " + this.id +
                ", title= " + this.title +
                ", info= " + this.information +
                ", published= " + this.published +
                ", private= " + this.isPrivate + "]";
    }
}

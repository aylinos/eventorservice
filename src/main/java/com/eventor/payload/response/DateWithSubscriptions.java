package com.eventor.payload.response;

import java.util.Date;

public class DateWithSubscriptions {
    String date;
    Integer totalSubscriptions;

    public DateWithSubscriptions(String date, Integer totalSubscriptions)
    {
        this.date = date;
        this.totalSubscriptions = totalSubscriptions;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public Integer getTotalSubscriptions() { return totalSubscriptions; }

    public void setTotalSubscriptions(Integer totalSubscriptions) {
        this.totalSubscriptions = totalSubscriptions;
    }
}

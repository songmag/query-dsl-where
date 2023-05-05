package com.mark.filter;

import java.time.LocalDateTime;

public class DateTimeFilter {
    private LocalDateTime from;
    private LocalDateTime end;
    public DateTimeFilter() {
        this.from = null;
        this.end = null;
    }

    public DateTimeFilter(LocalDateTime from, LocalDateTime end) {
        this.from = from;
        this.end = end;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}

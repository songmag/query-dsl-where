package com.mark.filter;


import java.time.LocalDate;

public class DateFilter {
    private LocalDate from;
    private LocalDate end;

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public DateFilter(LocalDate from, LocalDate end) {
        this.from = from;
        this.end = end;
    }
}

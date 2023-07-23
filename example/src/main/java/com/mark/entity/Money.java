package com.mark.entity;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Money {
    private Long amount;
    private Long unit;
    @Enumerated(EnumType.STRING)
    private Currency currency;

}

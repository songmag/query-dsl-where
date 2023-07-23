package com.mark.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class TestEntity {
    @Id
    @GeneratedValue()
    private Long id;
    private String name;
    private LocalDateTime time;

    @Embedded
    private Money money;

    public TestEntity() {
    }
}

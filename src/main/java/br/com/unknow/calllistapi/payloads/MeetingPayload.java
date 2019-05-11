package br.com.unknow.calllistapi.payloads;

import br.com.unknow.calllistapi.models.Meeting;

import java.time.LocalDateTime;

public class MeetingPayload {
    private Long id;
    private String name;
    private LocalDateTime date;

    public MeetingPayload() {
        super();
    }

    public MeetingPayload(Meeting entity) {
        this();

        this.id = entity.getId();
        this.name = entity.getName();
        this.date = entity.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

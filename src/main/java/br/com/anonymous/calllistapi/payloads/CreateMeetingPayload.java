package br.com.anonymous.calllistapi.payloads;

import java.time.LocalDateTime;

public class CreateMeetingPayload {
    private String name;
    private LocalDateTime date;

    public CreateMeetingPayload() {
        super();
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

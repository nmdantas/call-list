package br.com.alice.calllistapi.payloads;

import br.com.alice.calllistapi.models.Meeting;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDateTime;

public class MeetingPayload extends ResourceSupport {
    private Long meetingId;
    private String name;
    private LocalDateTime date;
    private Integer accessCode;

    public MeetingPayload() {
        super();
    }

    public MeetingPayload(Meeting entity) {
        this();

        this.meetingId = entity.getId();
        this.name = entity.getName();
        this.date = entity.getDate();

        if (!entity.getAccessCodes().isEmpty()) {
            this.accessCode = entity.getAccessCodes().get(0).getCode();
        }
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
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

    public Integer getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(Integer accessCode) {
        this.accessCode = accessCode;
    }
}

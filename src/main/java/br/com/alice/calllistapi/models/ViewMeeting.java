package br.com.alice.calllistapi.models;

import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Immutable
public class ViewMeeting {
    @Id
    @Column(name="MEETING_ID")
    private Long meetingId;

    @Column(name="MEETING_NAME")
    private String meetingName;

    @Column(name="MEETING_DATE")
    private LocalDateTime meetingDate;

    @Column(name="PROFILE_ID")
    private Long profileId;

    @Column(name="COMPANY")
    private String company;

    public ViewMeeting() {
        super();
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public LocalDateTime getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDateTime meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}

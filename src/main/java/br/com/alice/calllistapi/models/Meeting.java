package br.com.alice.calllistapi.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="MEETING")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="profile_id")
    private Long profileId;

    @Column(name="name")
    private String name;

    @Column(name="date")
    private LocalDateTime date;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="profile_id", insertable=false, updatable=false)
    private Profile profile;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="meeting", cascade=CascadeType.ALL)
    private List<AccessCode> accessCodes;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="meeting", cascade=CascadeType.ALL)
    private List<Participant> participants;

    public Meeting() {
        super();
    }

    public Meeting(Long id) {
        this();

        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<AccessCode> getAccessCodes() {
        if (accessCodes == null) {
            accessCodes = new ArrayList<>();
        }

        return accessCodes;
    }

    public void setAccessCodes(List<AccessCode> accessCodes) {
        accessCodes = accessCodes;
    }

    public void addAccessCode(AccessCode accessCode) {
        accessCode.setMeeting(this);

        getAccessCodes().add(accessCode);
    }

    public List<Participant> getParticipants() {
        if (participants == null) {
            participants = new ArrayList<>();
        }

        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant) {
        participant.setMeeting(this);

        getParticipants().add(participant);
    }
}

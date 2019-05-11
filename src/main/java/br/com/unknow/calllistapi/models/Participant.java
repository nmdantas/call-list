package br.com.unknow.calllistapi.models;

import javax.persistence.*;

@Entity
@Table(name="PARTICIPANT")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="profile_id")
    private Long profileId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="profile_id", insertable=false, updatable=false)
    private Profile profile;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="meeting_id")
    private Meeting meeting;

    public Participant() {
        super();
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}

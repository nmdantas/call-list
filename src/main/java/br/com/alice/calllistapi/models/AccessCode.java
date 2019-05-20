package br.com.alice.calllistapi.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="ACCESS_CODE")
public class AccessCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="code")
    private Integer code;

    @Column(name="create_date")
    private LocalDateTime createDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="meeting_id")
    private Meeting meeting;

    public AccessCode() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}

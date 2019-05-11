package br.com.unknow.calllistapi.payloads;

import br.com.unknow.calllistapi.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class UserPayload {

    private Long id;

    private String name;

    private String username;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    public UserPayload() {
        super();
    }

    public UserPayload(User entity) {
        this();

        this.id = entity.getId();
        this.name = entity.getName();
        this.username = entity.getUsername();
        this.createDate = entity.getCreateDate();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}

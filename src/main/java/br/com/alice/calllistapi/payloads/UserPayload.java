package br.com.alice.calllistapi.payloads;

import br.com.alice.calllistapi.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDateTime;

public class UserPayload extends ResourceSupport {

    private Long userId;

    private String name;

    private String username;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    public UserPayload() {
        super();
    }

    public UserPayload(User entity) {
        this();

        this.userId = entity.getId();
        this.name = entity.getName();
        this.username = entity.getUsername();
        this.createDate = entity.getCreateDate();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

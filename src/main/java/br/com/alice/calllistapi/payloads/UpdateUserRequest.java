package br.com.alice.calllistapi.payloads;

public class UpdateUserRequest {
    private String name;
    private String username;

    public UpdateUserRequest() {
        super();
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
}

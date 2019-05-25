package br.com.alice.calllistapi.payloads;

public class RecoveryPasswordRequest {
    private String email;

    public RecoveryPasswordRequest() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

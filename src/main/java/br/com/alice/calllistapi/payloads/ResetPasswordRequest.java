package br.com.alice.calllistapi.payloads;

public class ResetPasswordRequest {
    private String email;

    public ResetPasswordRequest() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

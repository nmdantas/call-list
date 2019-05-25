package br.com.alice.calllistapi.payloads;

public class ResetPasswordRequest {
    private String password;

    public ResetPasswordRequest() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

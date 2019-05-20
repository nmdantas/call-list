package br.com.alice.calllistapi.payloads;

public class UpdateProfileRequest {
    private boolean main;

    private String email;

    private String phone;

    private String company;

    private String role;

    public UpdateProfileRequest() {
        super();
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

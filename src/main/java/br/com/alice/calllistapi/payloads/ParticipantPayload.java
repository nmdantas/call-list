package br.com.alice.calllistapi.payloads;

import br.com.alice.calllistapi.models.Profile;

public class ParticipantPayload {
    private String name;

    private String email;

    private String phone;

    private String company;

    private String role;

    public ParticipantPayload() {
        super();
    }

    public ParticipantPayload(Profile entity) {
        this();

        this.name = entity.getUser().getName();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.company = entity.getCompany();
        this.role = entity.getRole();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

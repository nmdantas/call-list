package br.com.alice.calllistapi.payloads;

import br.com.alice.calllistapi.models.ViewContact;

public class ContactDetailsPayload {
    private String company;

    private String role;

    private String email;

    private String phone;

    public ContactDetailsPayload() {
        super();
    }

    public ContactDetailsPayload(String company, String role, String email, String phone) {
        this();

        this.company = company;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    public ContactDetailsPayload(ViewContact entity) {
        this(entity.getCompany(), entity.getRole(), entity.getEmail(), entity.getPhone());
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
}

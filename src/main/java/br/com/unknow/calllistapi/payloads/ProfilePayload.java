package br.com.unknow.calllistapi.payloads;

import br.com.unknow.calllistapi.models.Profile;

public class ProfilePayload {
    private Long id;

    private Integer type;

    private String email;

    private String phone;

    private String company;

    private String role;

    private boolean active;

    public ProfilePayload() {
        super();
    }

    public ProfilePayload(Profile entity) {
        this();

        this.id = entity.getId();
        this.type = entity.getType();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.company = entity.getCompany();
        this.role = entity.getRole();
        this.active = entity.isActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

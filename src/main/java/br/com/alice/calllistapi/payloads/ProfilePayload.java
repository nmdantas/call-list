package br.com.alice.calllistapi.payloads;

import br.com.alice.calllistapi.models.Profile;
import org.springframework.hateoas.ResourceSupport;

public class ProfilePayload extends ResourceSupport {
    private Long profileId;

    private String email;

    private String phone;

    private String company;

    private String role;

    private boolean active;

    private boolean main;

    public ProfilePayload() {
        super();
    }

    public ProfilePayload(Profile entity) {
        this();

        this.profileId = entity.getId();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.company = entity.getCompany();
        this.role = entity.getRole();
        this.active = entity.isActive();
        this.main = entity.isMain();
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
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

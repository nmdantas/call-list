package br.com.alice.calllistapi.models;

import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Immutable
public class ViewContact {
    @Id
    @Column(name="USER_ID")
    private Long userId;

    @Column(name="USER_NAME")
    private String userName;

    @Column(name="COMPANY")
    private String company;

    @Column(name="ROLE")
    private String role;

    @Column(name="EMAIL")
    private String email;

    @Column(name="PHONE")
    private String phone;

    public ViewContact() {
        super();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

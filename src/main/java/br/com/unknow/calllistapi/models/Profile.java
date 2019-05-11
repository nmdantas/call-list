package br.com.unknow.calllistapi.models;

import javax.persistence.*;

@Entity
@Table(name="PROFILE")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name="type")
    private Integer type;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="company")
    private String company;

    @Column(name="role")
    private String role;

    @Column(name="active")
    private boolean active;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", insertable=false, updatable=false)
    private User user;

    public Profile() {
        super();
    }

    public Profile(Long id) {
        this();

        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

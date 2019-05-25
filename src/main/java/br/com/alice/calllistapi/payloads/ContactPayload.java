package br.com.alice.calllistapi.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactPayload {
    @JsonIgnore
    private long id;

    private String name;

    private List<ContactDetailsPayload> details;

    public ContactPayload() {
        super();

        this.details = new ArrayList<>();
    }

    public ContactPayload(long id, String name) {
        this();

        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContactDetailsPayload> getDetails() {
        return details;
    }

    public void setDetails(List<ContactDetailsPayload> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ContactPayload that = (ContactPayload) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

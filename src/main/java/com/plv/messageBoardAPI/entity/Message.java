package com.plv.messageBoardAPI.entity;

import javax.persistence.*;

@Entity
@Table(name="MESSAGE")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="MESSAGE_OWNER")
    private String owner;

    @Column(name="MESSAGE_CONTENT")
    private String message;

    public Message() { }
    public Message(Long id, String owner, String message) {
        this.id = id;
        this.owner = owner;
        this.message = message;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOwner() {
        return this.owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}

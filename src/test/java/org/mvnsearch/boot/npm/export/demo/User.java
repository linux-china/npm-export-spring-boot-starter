package org.mvnsearch.boot.npm.export.demo;

import java.util.Date;

/**
 * User object
 *
 * @author linux_china
 */
public class User {
    private Integer id;
    private String nick;
    private Date birthDate;

    public User() {

    }

    public User(Integer id, String nick, Date birthDate) {
        this.id = id;
        this.nick = nick;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}

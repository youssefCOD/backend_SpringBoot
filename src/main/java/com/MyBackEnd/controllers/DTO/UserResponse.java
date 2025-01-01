package com.MyBackEnd.controllers.DTO;

import java.io.Serializable;
import com.MyBackEnd.models.User;

public class UserResponse implements Serializable {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int color;

    public UserResponse(User user) {
        this.id = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.color = user.getColor();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "UserResponse [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + "]";
    }
}

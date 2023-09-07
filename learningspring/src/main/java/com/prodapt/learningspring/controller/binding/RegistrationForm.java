package com.prodapt.learningspring.controller.binding;

import lombok.Data;

@Data
public class RegistrationForm {
    private String name;
    private String password;
    private String repeatPassword;

    public boolean isValid() {
        return password.equals(repeatPassword);
    }
}

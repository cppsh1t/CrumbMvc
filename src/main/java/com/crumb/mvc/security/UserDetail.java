package com.crumb.mvc.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetail {
    private String username;
    private String password;
    private String role;

}

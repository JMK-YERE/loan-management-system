package com.loanapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordActionRequest {
 @NotBlank private String token;
 @NotBlank @Size(min=8,max=128) private String password;
 public PasswordActionRequest(){}
 public String getToken(){return token;} public void setToken(String v){token=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
}

package com.loanapp.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
 @NotBlank(message = "Barua pepe au namba ya simu inahitajika") private String username;
 @NotBlank(message = "Password inahitajika") private String password;
 public LoginRequest(){} public LoginRequest(String username,String password){this.username=username;this.password=password;}
 public static Builder builder(){return new Builder();} public static class Builder{private final LoginRequest x=new LoginRequest(); public Builder username(String v){x.username=v;return this;} public Builder password(String v){x.password=v;return this;} public LoginRequest build(){return x;}}
 public String getUsername(){return username;} public void setUsername(String v){username=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;}
}
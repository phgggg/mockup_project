package com.authen.domain.request;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullName;
    private String facebookUrl;
    private String address;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username,
                        @JsonProperty("password") String password,
                        @JsonProperty("email") String email,
                        @JsonProperty("fullName") String fullName,
                        @JsonProperty("phone") String phone,
                        @JsonProperty("facebookUrl") String facebookUrl,
                        @JsonProperty("address") String address
    ) {
        this.username = username;
        this.password = password;
        this.email=email;
        this.fullName=fullName;
        this.phone=phone;
        this.facebookUrl=facebookUrl;
        this.address=address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", facebookUrl='" + facebookUrl + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

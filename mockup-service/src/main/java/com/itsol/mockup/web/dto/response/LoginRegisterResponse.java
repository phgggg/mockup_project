package com.itsol.mockup.web.dto.response;


/**
 * @author Rin-DTS
 */
public class LoginRegisterResponse extends BaseResultDTO{
    private String token;

    public String getToken() {
        return token;
    }

    public LoginRegisterResponse(String token) {
        setSuccess();
        this.token = token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public LoginRegisterResponse(String code, String message) {
        super(code, message);
        this.token = "@@@";
    }
}

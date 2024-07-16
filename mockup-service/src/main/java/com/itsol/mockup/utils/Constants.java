package com.itsol.mockup.utils;

import com.itsol.mockup.web.dto.response.ResultDTO;
import org.springframework.data.domain.Page;

public interface Constants {
//    public interface ApiErrorCode {
//        String ERROR = "01";
//        String SUCCESS = "00";
//        String DELETE_ERROR = "02";
//    }
//    public interface ApiErrorDesc {
//        String ERROR = "ERROR";
//        String SUCCESS = "SUCCESS";
//        String DELETE_ERROR = "DELETE_ERROR";
//    }
    //auth
    long TIME_TOKEN_EXPIRE = 3600000000L;
    String SECRET = "secretKey";
    String STATUS_CODE_USERNAME_OR_PASSWORD_INVALID = "30";
    String ENPOINT_LOGIN = "/auth/login";
//    String ENPOINT_LOGIN = "/auth";
    String ENPOINT_REGISTER = "/api/register";
    String ENPOINT_MATCH_API = "/**";
//    String ENPOINT_MATCH_API = "/api/**";
    String ENPOINT_MATCH_AUTH_API = "/auth/api/**";
    String ENPOINT_MATCH_AUTH_API_ACTIVE_USER = "/api/updateActiveUser";


    int MIN_LEHGTH_PASSWORD = 6;


    String UNKNOWN = "-10";

    String ERROR = "01";
    String SUCCESS = "00";
    String DELETE_ERROR = "02";
    String ERR_CODE_ITEM_NOT_FOUND = "03";
    String ERROR_401 = "401";
//    public ResultDTO pageToObj (Page<Object> page) {
//        ResultDTO resultDTO = new ResultDTO();
//        if(page != null) {
//            resultDTO.set
//        }
//    }
}

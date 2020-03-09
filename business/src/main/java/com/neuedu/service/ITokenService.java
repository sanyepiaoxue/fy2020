package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * token服务
 */

public interface ITokenService {
    //生成token
    ServerResponse generateToken();

    //校验token
    ServerResponse checkToken(HttpServletRequest request);
}

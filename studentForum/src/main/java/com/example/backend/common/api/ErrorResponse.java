package com.example.backend.common.api;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ErrorResponse {

    private static final Gson gson = new Gson();

    /**
     * 响应数据给前端
     * @param code
     */
    public static void sendJsonMessage(IErrorCode code) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        Map<String,Object> errorMap = new HashMap<>();
        errorMap.put("code",code.getCode());
        errorMap.put("message",code.getMessage());
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(gson.toJson(errorMap));
        writer.close();
        response.flushBuffer();
    }
}

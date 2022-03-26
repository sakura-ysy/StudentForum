package com.example.backend.module.question.controller;

import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.module.question.dto.AnswerDTO;
import com.example.backend.module.question.entity.Question;
import com.example.backend.module.question.service.IAnswerService;
import com.example.backend.module.question.vo.AnswerVO;
import com.example.backend.module.question.vo.QuestionVO;
import com.example.backend.module.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@Api(tags = "回答相关接口")
@RestController
@RequestMapping("/api/answer")
public class AnswerController {
    @Resource
    private IAnswerService iAnswerService;

    @ApiOperation("回答问题, 一级回答把replyToId置为null")
    @LoginRequired(allowAll = true)
    @PostMapping("/create")
    public ApiResult<AnswerVO> createAnswer(@RequestBody AnswerDTO dto) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        AnswerVO answerVO = iAnswerService.createAnswer(dto,user);
        return ApiResult.success(answerVO);
    }

    @ApiOperation("撤销回答")
    @LoginRequired(allowAll = true)
    @DeleteMapping("/cancel")
    public ApiResult<Object> cancelAnswer(@RequestParam("id") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        Boolean res = iAnswerService.cancelAnswer(id,user);
        if(res) return ApiResult.success("删除成功");
        else return ApiResult.failed("删除失败");
    }
}

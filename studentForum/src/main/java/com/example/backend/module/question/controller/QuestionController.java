package com.example.backend.module.question.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.module.question.dto.QuestionDTO;
import com.example.backend.module.question.entity.Question;
import com.example.backend.module.question.service.IQuestionService;
import com.example.backend.module.question.vo.AnswerVO;
import com.example.backend.module.question.vo.QuestionVO;
import com.example.backend.module.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spi.service.contexts.ApiSelector;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Api(tags = "问答相关接口")
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Resource
    private IQuestionService iQuestionService;

    @ApiOperation("分页获取问题列表")
    @GetMapping("/list")
    public ApiResult<Page<QuestionVO>> getQuestionPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                                                       @RequestParam(value = "tab", defaultValue = "latest") String tab) throws IOException {
        Page<Question> page = new Page<>(pageNo,pageSize);
        Page<QuestionVO> res = iQuestionService.getQuestionPage(page,tab);
        return ApiResult.success(res);
    }

    @ApiOperation("问题详情")
    @GetMapping("/info/{id}")
    public ApiResult<QuestionVO> getInfo(@PathVariable("id") String id) throws IOException {
        QuestionVO questionVO = iQuestionService.getInfo(id);
        return ApiResult.success(questionVO);
    }

    @ApiOperation("发布问题")
    @LoginRequired(allowAll = true)
    @PostMapping("/create")
    public ApiResult<QuestionVO> createQuestion(@RequestBody QuestionDTO dto) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        QuestionVO res = iQuestionService.createQuestion(dto,user);
        return ApiResult.success(res);
    }

    @ApiOperation("发布者撤销问题，逻辑删")
    @LoginRequired(allowAll = true)
    @DeleteMapping("/cancel")
    public ApiResult<Object> cancelQuestion(@RequestParam("id") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        Boolean res = iQuestionService.cancelQuestion(id,user);
        if(!res)
            return ApiResult.failed("撤销失败");
        return ApiResult.success("撤销成功");
    }

    @ApiOperation("发布者恢复曾经撤销的问题")
    @LoginRequired(allowAll = true)
    @PutMapping("/recover")
    public ApiResult<QuestionVO> recoverQuestion(@RequestParam("id") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        QuestionVO questionVO = iQuestionService.recoverQuestion(id,user);
        return ApiResult.success(questionVO);
    }

    @ApiOperation("获取某一个问题的全部回答")
    @GetMapping("/get/answer/list/{id}")
    public ApiResult<List<AnswerVO>> getAnswerList(@PathVariable("id") String id) throws IOException {
        return ApiResult.success(iQuestionService.getAnswerList(id));
    }

    @ApiOperation("采纳回答并给予赏金")
    @LoginRequired(allowAll = true)
    @PutMapping("/adopt")
    public ApiResult<AnswerVO> adoptAnswer(@RequestParam("answerId") String answerId,
                                           @RequestParam("questionId") String questionId) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        AnswerVO answerVO = iQuestionService.adoptAnswer(answerId,questionId,user);
        return ApiResult.success(answerVO);
    }

    @ApiOperation("获取全部已撤回问题, 只有管理员可用")
    @LoginRequired(allowAdmin = true)
    @GetMapping("/cancel/list")
    public ApiResult<List<QuestionVO>> getCanceledQuestionList() throws IOException {
        List<QuestionVO> res = iQuestionService.getCanceledQuestionList();
        return ApiResult.success(res);
    }
}

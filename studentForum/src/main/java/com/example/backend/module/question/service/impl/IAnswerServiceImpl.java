package com.example.backend.module.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ApiErrorCode;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.module.question.dto.AnswerDTO;
import com.example.backend.module.question.entity.Answer;
import com.example.backend.module.question.entity.Question;
import com.example.backend.module.question.mapper.AnswerMapper;
import com.example.backend.module.question.service.IAnswerService;
import com.example.backend.module.question.service.IQuestionService;
import com.example.backend.module.question.vo.AnswerVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IAnswerServiceImpl extends ServiceImpl<AnswerMapper,Answer> implements IAnswerService {

    @Resource
    private IUserService iUserService;

    @Resource
    private IQuestionService iQuestionService;

    /**
     * 回答
     * @param dto
     * @return
     */
    @Override
    public AnswerVO createAnswer(AnswerDTO dto, User user) throws IOException {
        Answer answer = new Answer();
        BeanUtils.copyProperties(dto,answer);
        answer.setUserId(user.getId());
        answer.setCreateTime(new Date());
        answer.setModifyTime(new Date());
        this.getBaseMapper().insert(answer);
        // 问题回答数++
        if(dto.getReplyToId() == null){
            Question question = iQuestionService.getById(dto.getQuestionId());
            if(question == null){
                ErrorResponse.sendJsonMessage(ApiErrorCode.QUESTION_NOT_EXIST);
                return null;
            }
            question.setAnsNum(question.getAnsNum()+1);
            iQuestionService.updateById(question);
        }
        return changeAnswerToAnswerVO(answer);
    }

    /**
     * 撤销回答
     * @param id
     * @return
     */
    @Override
    public Boolean cancelAnswer(String id, User user) throws IOException {
        Answer answer = this.getById(id);
        if(answer == null){
            ErrorResponse.sendJsonMessage(ApiErrorCode.ANSWER_NOT_EXIST);
            return false;
        }
        if(!answer.getUserId().equals(user.getId())){
            ErrorResponse.sendJsonMessage(ApiErrorCode.AUTHOR_ERROR);
            return false;
        }
        this.getBaseMapper().deleteById(id);
        return true;
    }

    /**
     * 转vo
     * @param answer
     * @return
     */
    @Override
    public AnswerVO changeAnswerToAnswerVO(Answer answer) throws IOException {
        AnswerVO answerVO = new AnswerVO();
        BeanUtils.copyProperties(answer,answerVO);
        String userId = answer.getUserId();
        User user = iUserService.getById(userId);
        if(user == null){
            ErrorResponse.sendJsonMessage(ApiErrorCode.USER_NOT_EXISTS);
            return null;
        }
        answerVO.setUsername(user.getUsername());
        // 一级
        if(answer.getReplyToId() == null){
            List<Answer> list = this.getBaseMapper().selectList(new LambdaQueryWrapper<Answer>().eq(Answer::getReplyToId,answer.getId()).eq(Answer::getQuestionId,answer.getQuestionId()));
            List<AnswerVO> voList = new ArrayList<>();
            for (Answer childAnswer : list) {
                AnswerVO childAnswerVO = new AnswerVO();
                BeanUtils.copyProperties(childAnswer,childAnswerVO);
                User childUser = iUserService.getById(childAnswer.getUserId());
                childAnswerVO.setUsername(childUser.getUsername());
                childAnswerVO.setUserId(childUser.getId());
                voList.add(childAnswerVO);
            }
            answerVO.setChildAnswer(voList);
        }
        // 二级
        else {
            answerVO.setChildAnswer(null);
        }
        return answerVO;
    }
}

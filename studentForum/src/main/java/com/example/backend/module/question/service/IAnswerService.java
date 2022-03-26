package com.example.backend.module.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.question.dto.AnswerDTO;
import com.example.backend.module.question.entity.Answer;
import com.example.backend.module.question.vo.AnswerVO;
import com.example.backend.module.user.entity.User;

import java.io.IOException;


public interface IAnswerService extends IService<Answer> {
    /**
     * 回答
     * @param dto
     * @return
     */
    AnswerVO createAnswer(AnswerDTO dto, User user) throws IOException;

    /**
     * 撤销回答, 实际删
     * @param id
     * @return
     */
    Boolean cancelAnswer(String id, User user) throws IOException;


    /**
     * 转vo
     */
    AnswerVO changeAnswerToAnswerVO(Answer answer) throws IOException;
}

package com.example.backend.module.question.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.question.dto.QuestionDTO;
import com.example.backend.module.question.entity.Question;
import com.example.backend.module.question.vo.AnswerVO;
import com.example.backend.module.question.vo.QuestionVO;
import com.example.backend.module.user.entity.User;

import java.io.IOException;
import java.util.List;

public interface IQuestionService extends IService<Question> {

    /**
     * 获取问题列表
     * @param page
     * @param tab
     * @return
     */
    Page<QuestionVO> getQuestionPage(Page<Question> page, String tab) throws IOException;

    /**
     * 问题详情
     * @param id
     * @return
     */
    QuestionVO getInfo(String id) throws IOException;

    /**
     * 发布问题
     * @param dto
     * @param user
     * @return
     */
    QuestionVO createQuestion(QuestionDTO dto, User user) throws IOException;

    /**
     * 撤销问题
     * @param id
     * @param user
     * @return
     */
    Boolean cancelQuestion(String id, User user) throws IOException;

    /**
     * 恢复问题
     * @param id
     * @param user
     * @return
     */
    QuestionVO recoverQuestion(String id, User user) throws IOException;

    /**
     * 获取所有撤销问题
     * @return
     */
    List<QuestionVO> getCanceledQuestionList() throws IOException;

    /**
     * 获取问题的全部回答
     * @return
     */
    List<AnswerVO> getAnswerList(String questionId) throws IOException;

    /**
     * 采纳回答
     * @param answerId
     * @return
     */
    AnswerVO adoptAnswer(String answerId, String questionId, User user) throws IOException;


    /**
     * 转换VO
     * @param question
     * @return
     */
    QuestionVO changeQuestionToQuestionVO(Question question) throws IOException;
}

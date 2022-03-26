package com.example.backend.module.question.service.impl;

import cn.hutool.core.net.url.UrlQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ApiResult;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.question.dto.QuestionDTO;
import com.example.backend.module.question.entity.Answer;
import com.example.backend.module.question.entity.Question;
import com.example.backend.module.question.mapper.QuestionMapper;
import com.example.backend.module.question.service.IAnswerService;
import com.example.backend.module.question.service.IQuestionService;
import com.example.backend.module.question.vo.AnswerVO;
import com.example.backend.module.question.vo.QuestionVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.example.backend.common.api.ApiErrorCode;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IQuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>  implements IQuestionService {

    @Resource
    private IUserService iUserService;

    @Resource
    private IAnswerService iAnswerService;

    /**
     * 获取问题列表
     *
     * @param page
     * @param tab
     * @return
     */
    @Override
    public Page<QuestionVO> getQuestionPage(Page<Question> page, String tab) throws IOException {
        // 查询话题
        Page<Question> iPage = this.baseMapper.selectQuestionPage(page, tab);
        Page<QuestionVO> resPage = new Page<>(page.getCurrent(), page.getSize());
        List<QuestionVO> questionVOList = new ArrayList<>();
        for (Question record : iPage.getRecords()) {
            questionVOList.add(changeQuestionToQuestionVO(record));
        }
        resPage.setRecords(questionVOList);
        resPage.setTotal(page.getTotal());
        return resPage;
    }

    @Override
    public QuestionVO getInfo(String id) throws IOException {
        Question question = this.getById(id);
        if(question == null)
            return null;
        // 浏览量++
        question.setView(question.getView()+1);
        this.updateById(question);
        return changeQuestionToQuestionVO(question);
    }

    /**
     * 发布问题
     * @param dto
     * @param user
     * @return
     */
    @Override
    public QuestionVO createQuestion(QuestionDTO dto, User user) throws IOException {
        if(user.getScore() < dto.getReward()){
            ErrorResponse.sendJsonMessage(ApiErrorCode.USER_SCORE_NOT_ENOUGH);
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(dto,question);
        question.setUserId(user.getId());
        question.setCreateTime(new Date());
        question.setModifyTime(new Date());
        // todo tag相关

        // 插入
        this.getBaseMapper().insert(question);
        // 更新用户积分
        user.setScore(user.getScore() - dto.getReward());
        iUserService.updateById(user);
        return this.changeQuestionToQuestionVO(question);
    }

    /**
     * 撤销问题, 逻辑删
     * @param id
     * @param user
     * @return
     */
    @Override
    public Boolean cancelQuestion(String id, User user) throws IOException {
        Question question = this.getById(id);
        if(question == null){
            ErrorResponse.sendJsonMessage(ApiErrorCode.QUESTION_NOT_EXIST);
            return false;
        }
        User author = iUserService.getById(question.getUserId());
        if(!author.getId().equals(user.getId())){
            ErrorResponse.sendJsonMessage(ApiErrorCode.AUTHOR_ERROR);
            return false;
        }
        if(question.getIsCanceled()){
            ErrorResponse.sendJsonMessage(ApiErrorCode.QUESTION_ALREADY_CANCELED);
            return false;
        }
        question.setIsCanceled(true);
        this.getBaseMapper().updateById(question);
        // 赏金收回
        if(!question.getIsSolved()){
            user.setScore(user.getScore()+question.getReward());
            iUserService.updateById(user);
        }
        return true;
    }

    /**
     * 恢复问题
     * @param id
     * @param user
     * @return
     */
    @Override
    public QuestionVO recoverQuestion(String id, User user) throws IOException {
        Question question = this.getById(id);
        if(question == null){
            ErrorResponse.sendJsonMessage(ApiErrorCode.QUESTION_NOT_EXIST);
            return null;
        }
        if(!question.getUserId().equals(user.getId())){
            ErrorResponse.sendJsonMessage(ApiErrorCode.AUTHOR_ERROR);
            return null;
        }
        if(!question.getIsCanceled()){
            ErrorResponse.sendJsonMessage(ApiErrorCode.QUESTION_NOT_CANCELED);
            return null;
        }
        // 再悬赏
        if(!question.getIsSolved()){
            user.setScore(user.getScore()-question.getReward());
            iUserService.updateById(user);
        }
        question.setIsCanceled(false);
        this.updateById(question);
        return changeQuestionToQuestionVO(question);
    }

    /**
     * 获取所有撤销帖子
     * @return
     */
    @Override
    public List<QuestionVO> getCanceledQuestionList() throws IOException {
        List<Question> questions = this.baseMapper.selectList(new LambdaQueryWrapper<Question>().eq(Question::getIsCanceled,true));
        List<QuestionVO> questionVOList = new ArrayList<>();
        for (Question question : questions) {
            questionVOList.add(changeQuestionToQuestionVO(question));
        }
        return questionVOList;
    }

    /**
     * 获取问题全部回答
     * @return
     */
    @Override
    public List<AnswerVO> getAnswerList(String questionId) throws IOException {
        List<Answer> answerList = iAnswerService.getBaseMapper().selectList(new LambdaQueryWrapper<Answer>().eq(Answer::getQuestionId,questionId).isNull(Answer::getReplyToId));
        List<AnswerVO> answerVOList = new ArrayList<>();
        for (Answer answer : answerList) {
            answerVOList.add(iAnswerService.changeAnswerToAnswerVO(answer));
        }
        return answerVOList;
    }

    /**
     * 采纳回答并交付赏金
     * @param answerId
     * @param questionId
     * @param user
     * @return
     */
    @Override
    public AnswerVO adoptAnswer(String answerId, String questionId, User user) throws IOException {
        Question question = this.getById(questionId);
        if(question == null){
            ErrorResponse.sendJsonMessage(ApiErrorCode.QUESTION_NOT_EXIST);
            return null;
        }
        if(question.getIsCanceled()){
            ErrorResponse.sendJsonMessage(ApiErrorCode.QUESTION_ALREADY_CANCELED);
            return null;
        }
        if(!question.getUserId().equals(user.getId())){
            ErrorResponse.sendJsonMessage(ApiErrorCode.AUTHOR_ERROR);
            return null;
        }
        Answer answer = iAnswerService.getById(answerId);
        if(answer == null){
            ErrorResponse.sendJsonMessage(ApiErrorCode.ANSWER_NOT_EXIST);
            return null;
        }
        if(!answer.getQuestionId().equals(questionId)){
            ErrorResponse.sendJsonMessage(ApiErrorCode.ANSWER_NOT_BELONG_THIS_QUESTION);
            return null;
        }
        answer.setIsAdopted(true);
        iAnswerService.updateById(answer);
        User answerUser = iUserService.getById(answer.getUserId());
        answerUser.setScore(answerUser.getScore() + question.getReward());
        iUserService.updateById(answerUser);
        return iAnswerService.changeAnswerToAnswerVO(answer);
    }


    /**
     * 转换VO
     * @param question
     * @return
     * @throws IOException
     */
    @Override
    public QuestionVO changeQuestionToQuestionVO(Question question) throws IOException {
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question,questionVO);
        // 发布者相关
        String userId = question.getUserId();
        if(userId != null){
            User user = iUserService.getById(userId);
            if(user == null) {
                ErrorResponse.sendJsonMessage(ApiErrorCode.USER_NOT_EXISTS);
                return null;
            }
            questionVO.setAvatar(user.getAvatar());
            questionVO.setUsername(user.getUsername());
        }

        // todo tag相关

        return questionVO;
    }


}

package com.example.backend.module.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.module.question.entity.Question;
import org.apache.ibatis.annotations.Param;

public interface QuestionMapper extends BaseMapper<Question> {
    /**
     * 分页查询首页话题列表
     * <p>
     *
     * @param page
     * @param tab
     * @return
     */
    Page<Question> selectQuestionPage(@Param("page") Page<Question> page, @Param("tab") String tab);
}

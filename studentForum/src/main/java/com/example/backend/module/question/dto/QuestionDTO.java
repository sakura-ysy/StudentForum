package com.example.backend.module.question.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class QuestionDTO implements Serializable {
    private static final long serialVersionUID = -4674204869581624048L;

    /**
     * 标题
     */
    private String title;

    /**
     * markdown
     */
    private String content;

    /**
     * tag
     */
    private List<String> tags;

    /**
     * 赏金
     */
    private Integer reward;
}

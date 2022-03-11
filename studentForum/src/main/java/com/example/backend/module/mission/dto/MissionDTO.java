package com.example.backend.module.mission.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class MissionDTO {
    private static final long serialVersionUID = 4922663593069261192L;

    /**
     * 内容
     */
    @NotBlank(message = "任务内容不能为空")
    private String content;

    /**
     * 标题
     */
    @NotBlank(message = "任务标题不能为空")
    private String title;

    /**
     * 任务附件
     */
    private List<String> files;

    /**
     * 奖励金额
     */
    @NotBlank(message = "奖励金额不能为空")
    private String reward;


    /**
     * 截止时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date deadTime;

    /**
     * 限制人数
     */
    private Integer sumLimit;

}

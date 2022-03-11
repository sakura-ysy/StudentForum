package com.example.backend.module.surface.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

// 公告栏的类（表单）

@Data  // 棒类自动生成 setter 和 getter
@Builder
@Accessors(chain = true)   // Builder和Accessors让类的对象支持链式操作
@TableName("bms_billboard")
@NoArgsConstructor   // 生成空参构造器
@AllArgsConstructor   // 生成全参构造器
public class Billboard implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)  // AUTO 指自增长
    private Integer id;

    /**
     * 公告牌
     */
    @TableField("content")  // content 列
    private String content;

    /**
     * 公告时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)   // create_time 列，创建时会自动获取值
    private Date createTime;

    /**
     * 1：展示中，0：过期
     */
    @Builder.Default
    @TableField("`show`")  // show 列，设定：1指显示，0指不显示
    private boolean show = false;
}

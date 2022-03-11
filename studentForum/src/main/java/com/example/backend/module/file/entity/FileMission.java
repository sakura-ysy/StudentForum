package com.example.backend.module.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@TableName("file_mission")
@AllArgsConstructor
@NoArgsConstructor
public class FileMission implements Serializable {
    private static final long serialVersionUID = 5785202187947224689L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件url
     */
    @TableField("file_url")
    private String url;

    /**
     * 任务id
     */
    @TableField("mission_id")
    private String missionId;
}

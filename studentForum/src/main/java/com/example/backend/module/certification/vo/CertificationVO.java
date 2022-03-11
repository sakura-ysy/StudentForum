package com.example.backend.module.certification.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class CertificationVO {
    private String id;
    private String userId;
    private String achievement;
    private String keyWord;
    private String content;
    @JsonIgnore
    private String tags;

    @JsonIgnore
    private String files;

    private Integer agree;
    private Integer disagree;
    private Integer numLimit;
    private Integer numSum;
    private Date createTime;
    private Date modifyTime;
    private String username;
    private String alias;
    private String avatar;

    private Object fileList;
    private Object tagList;


}

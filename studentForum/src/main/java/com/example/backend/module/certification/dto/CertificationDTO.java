package com.example.backend.module.certification.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CertificationDTO implements Serializable {
    private static final long serialVersionUID = -1L;

    /*
    * 成就
    * */
    private String achievement;

    /*
    * 关键词
    * */
    private String keyWord;

    /*
    * 标签
    * */
    private List<String> tags;

    /*
    * 概述
    * */
    private String content;


    /**
     * 上限
     */
    private Integer numLimit;

}

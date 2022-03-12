package com.example.backend.module.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户VO")
@Data
public class ProfileVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户id")
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 别称
     */
    @ApiModelProperty("昵称")
    private String alias;

    /**
     * 头像
     */
    @ApiModelProperty("头像url")
    private String avatar;

    /**
     * 关注数
     */
    @ApiModelProperty("关注数")
    private Integer followCount;

    /**
     * 关注者数
     */
    @ApiModelProperty("关注者数")
    private Integer followerCount;

    /**
     * 文章数
     */
    @ApiModelProperty("帖子数")
    private Integer topicCount;

    /**
     * 专栏数
     */
    @ApiModelProperty("专栏数")
    private Integer columns;

    /**
     * 评论数
     */
    @ApiModelProperty("评论数")
    private Integer commentCount;

}

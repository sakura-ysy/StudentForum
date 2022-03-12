package com.example.backend.module.post.vo;

import com.example.backend.module.post.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel("帖子VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVO implements Serializable {
    private static final long serialVersionUID = -261082150965211545L;

    /**
     * 文章ID
     */
    @ApiModelProperty("帖子id")
    private String id;
    /**
     * 用户ID
     */
    @ApiModelProperty("作者用户id")
    private String userId;
    /**
     * 头像
     */
    @ApiModelProperty("作者头像")
    private String avatar;
    /**
     * 用户昵称
     */
    @ApiModelProperty("作者昵称")
    private String alias;
    /**
     * 作者用户名
     */
    @ApiModelProperty("作者用户名")
    private String username;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    /**
     * 评论统计
     */
    @ApiModelProperty("评论次数")
    private Integer comments;
    /**
     * 点赞统计
     */
    @ApiModelProperty("点赞次数")
    private Integer praises;
    /**
     * 收藏次數
     */
    @ApiModelProperty("收藏次数")
    private Integer collects;
    /**
     * 置顶
     */
    @ApiModelProperty("是否置顶(暂时没用)")
    private Boolean top;
    /**
     * 加精
     */
    @ApiModelProperty("是否加精(暂时没用)")
    private Boolean essence;
    /**
     * 话题关联标签
     */
    @ApiModelProperty("帖子的标签")
    private List<Tag> tags;
    /**
     * 浏览量
     */
    @ApiModelProperty("浏览量")
    private Integer view;
    /**
     * 创建时间
     */
    @ApiModelProperty("发布时间")
    private Date createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty("最近一次修改时间")
    private Date modifyTime;

    /**
     * 是否审核通过
     */
    @ApiModelProperty("是否通过审核")
    private Boolean isPass;
}

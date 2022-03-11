package com.example.backend.module.notify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.module.notify.entity.Notify;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyMapper extends BaseMapper<Notify> {

    /**
     * 根据某一用户的id获取其相应通知
     * @param userId 用户id
     * @return 通知
     */
    List<Notify> getNotifyByUserId(@Param("userId") String userId);

    List<Notify> getPraiseNotifyByUserId(@Param("userId")String userId);

    List<Notify> getCommentNotifyByUserId(@Param("userId")String userId);

    List<Notify> getCollectNotifyByUserId(@Param("userId")String userId);

    List<Notify> getReplyNotifyByUserId(@Param("userId")String userId);

    List<Notify> getFollowNotifyByUserId(@Param("userId")String userId);

}

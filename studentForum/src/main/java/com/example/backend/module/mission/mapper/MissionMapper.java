package com.example.backend.module.mission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.module.mission.entity.Mission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionMapper extends BaseMapper<Mission> {
    /**
     * 任务查询首页话题列表
     * <p>
     *
     * @param page
     * @param tab
     * @return
     */
    Page<Mission> selectListAndPage(@Param("page") Page<Mission> page, @Param("tab") String tab);

}

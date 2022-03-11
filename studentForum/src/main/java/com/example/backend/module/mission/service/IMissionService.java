package com.example.backend.module.mission.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.mission.dto.MissionDTO;
import com.example.backend.module.mission.entity.Mission;

import java.util.List;
import java.util.Map;

public interface IMissionService extends IService<Mission> {

    /**
     * 任务发布
     * @param dto
     * @param userName
     * @return
     */
    Mission executeRelease(MissionDTO dto,String userName);

    /**
     * 获取全部任务
     * @return
     */
    Page<Mission> getAllMissions(Page<Mission> page, String tab);

    /**
     * 接受任务
     * @param missionId
     * @param userName
     * @return
     */
    Mission executeReceive(String missionId, String userName);

    /**
     * 返回用户接收的所有任务以及完成状态
     * @param userName
     * @return
     */
    List<Map<String,Object>> getReceiveList(String userName);

    /**
     * 放弃任务
     * @param missionId
     * @param userName
     */
    void executeAbandon(String missionId, String userName);
}

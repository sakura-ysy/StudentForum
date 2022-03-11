package com.example.backend.module.mission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.file.entity.FileMission;
import com.example.backend.module.file.mapper.FileMissionMapper;
import com.example.backend.module.mission.dto.MissionDTO;
import com.example.backend.module.mission.entity.Mission;
import com.example.backend.module.mission.entity.MissionReceiver;
import com.example.backend.module.mission.mapper.MissionMapper;
import com.example.backend.module.mission.mapper.MissionReceiverMapper;
import com.example.backend.module.mission.service.IMissionService;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class IMissionServiceImpl extends ServiceImpl<MissionMapper, Mission> implements IMissionService {
    @Resource
    private IUserService iUserService;
    @Resource
    private FileMissionMapper fileMissionMapper;
    @Resource
    private MissionReceiverMapper missionReceiverMapper;
    /**
     * 任务发布
     * @param dto
     * @param userName
     * @return
     */
    @Override
    public Mission executeRelease(MissionDTO dto, String userName) {
        User user = iUserService.getUserByUsername(userName);
        Mission addMission = Mission.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .title(dto.getTitle())
                .createTime(new Date())
                .deadTime(dto.getDeadTime())
                .sumLimit(dto.getSumLimit())
                .reward(dto.getReward())
                .build();
        baseMapper.insert(addMission);

        // 新增 任务-附件关联
        for (String file : dto.getFiles()) {
            FileMission fileMission = FileMission.builder()
                    .missionId(addMission.getId())
                    .url(file)
                    .build();
            fileMissionMapper.insert(fileMission);
        }

        addMission.setFiles(dto.getFiles());
        return addMission;
    }

    /**
     * 获取全部任务
     * @param page
     * @param tab
     * @return
     */
    @Override
    public Page<Mission> getAllMissions(Page<Mission> page, String tab) {
        Page<Mission> iPage = this.baseMapper.selectListAndPage(page, tab);
        // 添加附件url
        iPage.getRecords().forEach(mission-> {
            List<String> files = new ArrayList<>();
            List<FileMission> fileMissions = fileMissionMapper.selectList(new LambdaQueryWrapper<FileMission>().eq(FileMission::getMissionId,mission.getId()));
            for (FileMission fileMission : fileMissions) {
                files.add(fileMission.getUrl());
            }
            mission.setFiles(files);
        });
        return iPage;
    }

    /**
     * 接收任务
     * @param missionId
     * @param userName
     * @return
     */
    @Override
    public Mission executeReceive(String missionId, String userName) {
        User user = iUserService.getUserByUsername(userName);
        MissionReceiver addRelation = MissionReceiver.builder()
                .missionId(missionId)
                .receiverId(user.getId())
                .receiverName(userName)
                .build();
        missionReceiverMapper.insert(addRelation);
        Mission mission = baseMapper.selectById(missionId);
        mission.setNowSum(mission.getNowSum() + 1);
        if (mission.getNowSum() >= mission.getSumLimit()){
            mission.setIsFull(true);
        }
        baseMapper.updateById(mission);
        return mission;
    }

    /**
     * 返回用户接收的所有任务以及完成状态
     * @param userName
     * @return
     */
    @Override
    public List<Map<String,Object>> getReceiveList(String userName) {
        List<MissionReceiver> mrList =
                missionReceiverMapper.selectList(new LambdaQueryWrapper<MissionReceiver>().eq(MissionReceiver::getReceiverName,userName));
        List<Map<String,Object>> list = new ArrayList<>();
        for (MissionReceiver missionReceiver : mrList) {
            Map<String,Object> map = new HashMap<>();
            Mission mission = baseMapper.selectById(missionReceiver.getMissionId());
            map.put("mission", mission);
            map.put("isFinish",missionReceiver.getIsFinish());
            list.add(map);
        }
        return list;
    }

    /**
     * 放弃任务
     * @param missionId
     * @param userName
     */
    @Override
    public void executeAbandon(String missionId, String userName) {
        missionReceiverMapper.delete(new LambdaQueryWrapper<MissionReceiver>().eq(MissionReceiver::getMissionId,missionId)
        .eq(MissionReceiver::getReceiverName,userName));
        Mission mission = baseMapper.selectById(missionId);
        mission.setNowSum(mission.getNowSum() - 1);
        if (mission.getNowSum() < mission.getSumLimit()){
            mission.setIsFull(false);
        }
    }
}

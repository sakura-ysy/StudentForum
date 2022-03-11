package com.example.backend.module.mission.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.api.ApiResult;
import com.example.backend.module.mission.dto.MissionDTO;
import com.example.backend.module.mission.entity.Mission;
import com.example.backend.module.mission.entity.MissionReceiver;
import com.example.backend.module.mission.mapper.MissionReceiverMapper;
import com.example.backend.module.mission.service.IMissionService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mission")
public class MissionController {

    @Resource
    private IMissionService iMissionService;
    @Resource
    private MissionReceiverMapper missionReceiverMapper;

    /**
     * 发布任务
     * @return
     */
    @RequestMapping("/release")
    @SaCheckPermission("missionRelease")
    public ApiResult<Mission> releaseMission(@RequestBody MissionDTO dto){
        String userName = (String) StpUtil.getLoginId();
        Mission mission = iMissionService.executeRelease(dto,userName);
        return ApiResult.success(mission);
    }

    /**
     * 获取全部任务
     */
    @RequestMapping("/list")
    public ApiResult<Page<Mission>> getAllMissions(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                                  @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<Mission> missionPage = iMissionService.getAllMissions(new Page<>(pageNo, pageSize), tab);
        return ApiResult.success(missionPage);
    }

    /**
     * 接收任务
     * @param missionId
     * @return
     */
    @SaCheckPermission("missionReceive")
    @RequestMapping("/receive")
    public ApiResult<Mission> receiveMission(@RequestParam("missionId") String missionId){
        String userName = (String) StpUtil.getLoginId();
        Mission mission = iMissionService.getBaseMapper().selectOne(new LambdaQueryWrapper<Mission>().eq(Mission::getId, missionId));
        if (ObjectUtils.isEmpty(mission)){
            return ApiResult.failed("任务不存在");
        }
        else if (!(mission.getNowSum() < mission.getSumLimit())){
            return ApiResult.failed("任务接收者已满额");
        }
        else if (mission.getIsOver()){
            return ApiResult.failed("任务已结束");
        }
        MissionReceiver missionReceiver =
                missionReceiverMapper.selectOne(new LambdaQueryWrapper<MissionReceiver>().eq(MissionReceiver::getMissionId,missionId)
                .eq(MissionReceiver::getReceiverName,userName));
        if (!ObjectUtils.isEmpty(missionReceiver)){
            return ApiResult.failed("已接收该任务，不能再次接收");
        }
        Mission recMission =  iMissionService.executeReceive(missionId,userName);
        return ApiResult.success(recMission);
    }

    /**
     * 获取全部接收任务
     * @return
     */
    @SaCheckPermission("missionReceive")
    @RequestMapping("/receive/list")
    public ApiResult<List<Map<String,Object>>> getReceiveList(){
        String userName = (String) StpUtil.getLoginId();
        List<Map<String,Object>> resList = iMissionService.getReceiveList(userName);
        return ApiResult.success(resList);
    }


    /**
     * 放弃任务
     * @param missionId
     * @return
     */
    @SaCheckPermission("missionReceive")
    @RequestMapping("/abandon")
    public ApiResult<String> abandonMission(@RequestParam("missionId") String missionId){
        String userName = (String) StpUtil.getLoginId();
        MissionReceiver missionReceiver =
                missionReceiverMapper.selectOne(new LambdaQueryWrapper<MissionReceiver>().eq(MissionReceiver::getMissionId,missionId)
                        .eq(MissionReceiver::getReceiverName,userName));
        if (ObjectUtils.isEmpty(missionReceiver)){
            return ApiResult.failed("还未接收");
        }
       iMissionService.executeAbandon(missionId,userName);
        return ApiResult.success(null,"取消成功");
    }
}

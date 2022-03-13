package com.example.backend.module.mission.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.module.mission.dto.MissionDTO;
import com.example.backend.module.mission.entity.Mission;
import com.example.backend.module.mission.entity.MissionReceiver;
import com.example.backend.module.mission.mapper.MissionReceiverMapper;
import com.example.backend.module.mission.service.IMissionService;
import com.example.backend.module.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.*;
import java.util.List;
import java.util.Map;

@Api(tags = "任务相关接口")
@RestController
@RequestMapping("/api/mission")
public class MissionController {

    @Resource
    private IMissionService iMissionService;
    @Resource
    private MissionReceiverMapper missionReceiverMapper;

    /**
     * 发布任务
     * @return
     */
    @ApiOperation("发布任务")
    @RequestMapping(value = "/release",method = RequestMethod.POST)
    public ApiResult<Mission> releaseMission(@RequestBody MissionDTO dto){
        User user = AuthInterceptor.getCurrentUser();
        Mission mission = iMissionService.executeRelease(dto,user.getUsername());
        return ApiResult.success(mission);
    }

    /**
     * 获取全部任务
     */
    @ApiOperation("获取全部任务")
    @GetMapping(value = "/list")
    public ApiResult<Page<Mission>> getAllMissions(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<Mission> missionPage = iMissionService.getAllMissions(new Page<>(pageNo, pageSize), "latest");
        return ApiResult.success(missionPage);
    }

    /**
     * 接收任务
     * @param map
     * @return
     */
    @ApiOperation("接收任务")
    @RequestMapping(value = "/receive",method = RequestMethod.POST)
    public ApiResult<Mission> receiveMission(
            @ApiParam("任务id,仅需传入missionId字段") @RequestBody Map<String,String> map){
        String missionId = map.get("missionId");
        User user = AuthInterceptor.getCurrentUser();
        String userName = user.getUsername();
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
    @ApiOperation("获取全部已接收任务")
    @GetMapping(value = "/receive/list")
    public ApiResult<List<Map<String,Object>>> getReceiveList(){
        User user = AuthInterceptor.getCurrentUser();
        String userName = user.getUsername();
        List<Map<String,Object>> resList = iMissionService.getReceiveList(userName);
        return ApiResult.success(resList);
    }


    /**
     * 放弃任务
     * @param map
     * @return
     */
    @ApiOperation("放弃任务")
    @RequestMapping(value = "/abandon",method = RequestMethod.POST)
    public ApiResult<String> abandonMission(
            @ApiParam("任务id, 仅需传入missionId")@RequestBody Map<String,String> map){
        String missionId = map.get("missionId");
        User user = AuthInterceptor.getCurrentUser();
        String userName = user.getUsername();
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

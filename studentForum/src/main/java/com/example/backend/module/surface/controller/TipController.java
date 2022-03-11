package com.example.backend.module.surface.controller;

import com.example.backend.common.api.ApiResult;
import com.example.backend.module.surface.entity.Tip;
import com.example.backend.module.surface.service.ITipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

// Url 为 Localhost:8000/tip/today 时将随机返回个bms_tip对象（每日一句）
@RestController
@RequestMapping("/tip")
public class TipController {
    @Resource
    private ITipService bmsTipService; // 由于@MapperScan的存在，IBmsBillboardService 就是对应接口的实现类

    /**
     * 返回每日十句
     * @return
     */
    @GetMapping("/today")
    public ApiResult<Tip> getRandomTip(){
        Tip tip =bmsTipService.getRandomTip();
        return ApiResult.success(tip);
    }
}

package com.example.backend.module.surface.controller;

import com.example.backend.common.api.ApiResult;
import com.example.backend.module.surface.entity.Promotion;
import com.example.backend.module.surface.service.IPromotionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

// Url 为 Localhost:8000/promotion/all 时将返回最后一个bms_promotion对象（推广信息）
@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Resource
    private IPromotionService bmsPromotionService;

    /**
     * 返回所有推广
     * @return
     */
    @GetMapping("/all")
    public ApiResult<List<Promotion>> list() {
        List<Promotion> list = bmsPromotionService.list();
        return ApiResult.success(list);
    }

}


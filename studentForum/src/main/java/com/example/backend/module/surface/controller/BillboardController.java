package com.example.backend.module.surface.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.api.ApiResult;
import com.example.backend.module.surface.entity.Billboard;
import com.example.backend.module.surface.service.IBillboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

// Url 为 Localhost:8000/billboard/show 时将返回最后一个show值为true的BmsBillboard对象
@RestController
@RequestMapping("/billboard")
public class BillboardController {
    @Resource
    private IBillboardService iBillboardService;

    /**
     * 返回公告栏最新数据
     * @return
     */
    @GetMapping("/show")
    public ApiResult<Billboard> getNotices(){
        List<Billboard> list = iBillboardService.list(new
                LambdaQueryWrapper<Billboard>().eq(Billboard::isShow,true)) ;
        return ApiResult.success(list.get(list.size()-1));
        // 返回操作的Code、Message 以及 对象的Data，list.size()-1指集合中最后一个对象
    }
}

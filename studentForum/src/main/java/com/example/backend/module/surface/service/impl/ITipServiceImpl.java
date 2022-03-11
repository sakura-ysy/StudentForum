package com.example.backend.module.surface.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.surface.entity.Tip;
import com.example.backend.module.surface.mapper.TipMapper;
import com.example.backend.module.surface.service.ITipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j  // 日志
@Service  // 标记当前类是一个service类，加上该注解会将当前类自动注入到spring容器中，不需要再在applicationContext.xml文件定义bean了
public class ITipServiceImpl extends ServiceImpl<TipMapper, Tip> implements ITipService {
    @Override
    public Tip getRandomTip() {
        Tip todayTip = null;
        try {
            todayTip = this.baseMapper.getRandomTip();
        } catch (Exception e) {
            log.info("tip转化失败");
        }
        return todayTip;
    }
}

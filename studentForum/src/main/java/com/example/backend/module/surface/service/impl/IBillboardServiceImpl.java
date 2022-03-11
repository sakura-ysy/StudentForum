package com.example.backend.module.surface.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.surface.entity.Billboard;
import com.example.backend.module.surface.mapper.BillboardMapper;
import com.example.backend.module.surface.service.IBillboardService;
import org.springframework.stereotype.Service;

@Service  // 标记当前类是一个service类，加上该注解会将当前类自动注入到spring容器中，不需要再在applicationContext.xml文件定义bean了
public class IBillboardServiceImpl extends ServiceImpl<BillboardMapper, Billboard> implements IBillboardService {

}


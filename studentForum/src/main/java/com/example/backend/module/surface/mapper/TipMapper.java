package com.example.backend.module.surface.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.module.surface.entity.Tip;
import org.springframework.stereotype.Repository;

// 在 TipMapper.xml 中定义
@Repository  // 表明类具有对对象进行CRUD（增删改查）的功能,(@MapperScan会自动生成实现类)
public interface TipMapper extends BaseMapper<Tip> {
    Tip getRandomTip();
}

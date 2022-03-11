package com.example.backend.module.surface.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.surface.entity.Tip;

public interface ITipService extends IService<Tip> {
    Tip getRandomTip();
}

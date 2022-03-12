package com.example.backend.module.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.company.entity.Company;
import com.example.backend.module.company.mapper.CompanyMapper;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.impl.IUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "只允许企业能访问的接口")
@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private IUserServiceImpl iUserService;

    @ApiOperation("获取该企业信息")
    @LoginRequired(allowAll = true)
    @GetMapping(value = "/info")
    public ApiResult<Company> getCompanyInfo(){
        User user = AuthInterceptor.getCurrentUser();
        Company company = companyMapper.selectOne(new LambdaQueryWrapper<Company>().eq(Company::getUserName, user.getUsername()));
        if (ObjectUtils.isEmpty(company)){
            return ApiResult.failed("不是属于企业");
        }
        return ApiResult.success(company);
    }

    @ApiOperation("更新企业信息")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<Company> updateCompanyInfo(@RequestBody Company company){
        User user = AuthInterceptor.getCurrentUser();
        Company oldInfo = companyMapper.selectOne(new LambdaQueryWrapper<Company>().eq(Company::getUserName, user.getUsername()));
        if (ObjectUtils.isEmpty(oldInfo)){
            return ApiResult.failed("不是属于企业");
        }
        company.setId(oldInfo.getId());
        company.setUserId(oldInfo.getUserId());
        company.setUserName(oldInfo.getUserName());
        companyMapper.updateById(company);
        return ApiResult.success(company);
    }
}

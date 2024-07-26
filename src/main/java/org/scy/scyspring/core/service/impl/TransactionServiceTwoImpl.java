package org.scy.scyspring.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.UserInfoService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceTwoImpl {

    @Resource
    private UserInfoService userInfoService;

    public void compile(UserInfo userInfo) {


        if (userInfo.getUsername().contains("J")) {
            LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserInfo::getAge, 88);
            updateWrapper.eq(UserInfo::getUuid, userInfo.getUuid());
            userInfoService.update(updateWrapper);
            throw new RuntimeException("my asyncAloneTransaction Error");
        } else {
            LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserInfo::getAge, 101);
            updateWrapper.eq(UserInfo::getUuid, userInfo.getUuid());
            userInfoService.update(updateWrapper);
        }
    }
}

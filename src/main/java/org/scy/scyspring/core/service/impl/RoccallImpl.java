package org.scy.scyspring.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.mapper.UserInfoMapper;
import org.scy.scyspring.core.service.Rocall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RoccallImpl implements Rocall {

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public void rocall(String key) {
        LambdaUpdateWrapper<UserInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.like(UserInfo::getUsername, "");
        List<UserInfo> userInfos = userInfoMapper.selectList(lambdaUpdateWrapper);
        for (UserInfo userInfo : userInfos) {
            try {
                if (userInfo.getUsername().equals(key)) {
                    userInfoMapper.deleteById(userInfo.getUuid());
                }
            } catch (Throwable t) {
                log.error("rocall error", t);
            }
        }
    }
}

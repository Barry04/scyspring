package org.scy.scyspring.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.mapper.UserInfoMapper;
import org.scy.scyspring.core.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author suncy40801
 * &#064;description  针对表【user_info】的数据库操作Service实现
 * &#064;createDate  2024-04-17 15:46:19
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Override
    public UserInfo getUserInfoByUserName(String userName) {
        log.info("getUserInfoByUserName:{}", userName);
        LambdaUpdateWrapper<UserInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserInfo::getUsername, userName);
        return getOne(lambdaUpdateWrapper);
    }
}





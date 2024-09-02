package org.scy.scyspring.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceTwoImpl {

    @Resource
    private UserInfoService userInfoService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void compile(UserInfo userInfo) {
        // 定义事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 设置事务传播行为

        // 开始事务
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
            if (userInfo.getUsername().contains("J")) {
                updateWrapper.set(UserInfo::getAge, 88);
                updateWrapper.eq(UserInfo::getUuid, userInfo.getUuid());
                userInfoService.update(updateWrapper);
                throw new RuntimeException("my asyncAloneTransaction Error");
            } else {
                updateWrapper.set(UserInfo::getAge, 99);
                updateWrapper.eq(UserInfo::getUuid, userInfo.getUuid());
                userInfoService.update(updateWrapper);
            }
        } catch (Exception e) {
            // 事务回滚
            transactionManager.rollback(status);
        }
    }
}

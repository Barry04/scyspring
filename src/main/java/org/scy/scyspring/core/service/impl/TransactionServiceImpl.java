package org.scy.scyspring.core.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl {

    @Resource
    private UserInfoService userInfoService;

    @Autowired
    private AsyncService asyncService;

    @Resource
    private TransactionServiceTwoImpl transactionServiceTwo;

    @Resource
    private PlatformTransactionManager transactionManager;


    /**
     * 编译方法，用于处理与用户信息相关的业务逻辑
     * 该方法通过事务处理来确保每个用户的业务操作的原子性
     */
    public void compile() {
        // 定义事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 设置事务传播行为，表示总是新建事务执行代码块，如果有异常则回滚事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        // 获取所有用户信息列表
        List<UserInfo> list = userInfoService.list();
        // 遍历用户信息列表
        for (UserInfo userInfo : list) {
            // 开始事务
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                // 执行具体的业务逻辑
                extracted(userInfo);
                // 提交事务，表示操作成功
                transactionManager.commit(status);
            } catch (Exception e) {
                // 记录错误日志
                log.error("error msg : {}", e.getMessage(), e);
                // 回滚事务，表示操作失败
                transactionManager.rollback(status);
            }
        }
    }

    /**
     * 具体的业务处理方法
     * 该方法用于处理单个用户的信息
     *
     * @param userInfo 用户信息对象，包含用户的具体数据
     */
    public void extracted(UserInfo userInfo) {
        transactionServiceTwo.compile(userInfo);
    }


    List<UserInfo> getDescMatch(String key) {
        return userInfoService.selectByDescribesMatchQuery(key);
    }
}

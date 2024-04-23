package org.scy.scyspring.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.UserInfoService;
import org.scy.scyspring.utils.ExecutorsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

@Slf4j
@Component
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void asyncMethod(Long time) {

        try {
            Thread.sleep(time);
            System.out.println("异步方法开始执行 time: " + time);
            UserInfo admin = userInfoService.getUserInfoByUserName("Miu Wai Lam");
            System.out.println("admin.getUsername() = " + admin.getUsername() + " time: " + time);
        } catch (InterruptedException e) {
            log.error("异步方法执行异常", e);
        }

    }


    /**
     * 异步执行一个任务，可以选择是否在一个独立的事务中执行。
     *
     * @param runnable         要执行的任务，实现了Runnable接口的对象。
     * @param aloneTransaction 指示是否要在独立的事务中执行任务的布尔值。如果为true，则在独立事务中执行；如果为false或null，则直接执行，不涉及事务。
     */
    @Override
    public void asyncAloneTransaction(Runnable runnable, Boolean aloneTransaction) {
        // 判断是否指定在独立事务中执行且为true
        if (Objects.nonNull(aloneTransaction) && aloneTransaction) {
            // 使用事务模板执行任务，不返回任何结果
            transactionTemplate.executeWithoutResult(status -> ExecutorsUtils.getFixedThreadPoolExecutor().execute(runnable));
        } else {
            // 直接执行任务，不涉及事务
            ExecutorsUtils.getScheduledThreadPoolExecutor().execute(runnable);
        }
    }


}

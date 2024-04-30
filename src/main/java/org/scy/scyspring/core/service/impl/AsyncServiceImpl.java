package org.scy.scyspring.core.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.Log;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.LogService;
import org.scy.scyspring.core.service.UserInfoService;
import org.scy.scyspring.utils.ExecutorsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;


    @Autowired
    private LogService logService;

    @Override
    public void asyncMethod(Long time) {

        try {
            Log log = new Log();
            log.setUuid(UUID.randomUUID().toString().substring(0, 8));
            log.setMsg("my asyncAloneTransactionMethod Error");
            log.setStatus("done");
            logService.insertLog(log);
            Thread.sleep(time);
            System.out.println("异步方法开始执行 time: " + time);
        } catch (InterruptedException e) {
            log.error("异步方法执行异常", e);
        }

    }


    /**
     * 异步执行一个任务，当选中在独立的事务中执行任务时，会使用事务管理器手动管理这个任务的事务，
     * 当任务抛出异常时，事务会被回滚。
     *
     * @param runnable         要执行的任务，实现了Runnable接口的对象。
     * @param aloneTransaction 指示是否在独立的事务中执行任务的标志。
     *                         如果为 true，则使用事务管理器在独立事务中执行；
     *                         如果为 false 或者 null，则不使用事务管理器进行事务管理，直接执行任务。
     */
    @Async
    @Override
    public void asyncAloneTransaction(Runnable runnable, Boolean aloneTransaction) {
        if (Objects.nonNull(aloneTransaction) && aloneTransaction) {
            // 创建一个新的事务定义
            TransactionDefinition def = new DefaultTransactionDefinition();
            // 获取当前事务的状态，如果当前没有事务，就新建一个事务
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                // 运行任务
                runnable.run();
                // 任务完成后，提交事务
                transactionManager.commit(status);
            } catch (Exception e) {
                // 如果任务抛出异常，回滚事务
                transactionManager.rollback(status);
                // 重新抛出异常
                throw e;
            }
        } else {
            // 不需要事务的执行直接在线程池中执行
            ExecutorsUtils.getScheduledThreadPoolExecutor().execute(runnable);
        }
    }


    @Override
    @Async
    public void asyncAloneTransactionMethod(Runnable runnable, Boolean aloneTransaction) {
        if (Objects.nonNull(aloneTransaction) && aloneTransaction) {
            // 使用事务模板执行任务，不返回任何结果
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                    runnable.run();
                }
            });
        } else {
            // 不需要事务的执行直接在线程池中执行
            runnable.run();
        }
    }


}

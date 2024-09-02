package org.scy.scyspring.core.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.Log;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.LogService;
import org.scy.scyspring.core.service.UserInfoService;
import org.scy.scyspring.utils.ExecutorsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
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
     * 异步执行任务，根据是否需要独立事务来决定事务的处理方式
     *
     * @param runnable        需要执行的任务
     * @param aloneTransaction 是否需要独立事务
     */
    public void asyncAloneTransaction(Runnable runnable, Boolean aloneTransaction) {
        if (Boolean.TRUE.equals(aloneTransaction)) {
            // 独立事务逻辑
            executeInTransaction(runnable, false);
        } else {
            // 嵌套事务逻辑
            executeInTransaction(() -> {
                executeInTransaction(() -> {
                    try {
                        ExecutorsUtils.getScheduledThreadPoolExecutor().execute(runnable);
                    } catch (Exception ex) {
                        // 异常处理逻辑：如果在执行期间发生异常，回滚事务并重新抛出异常
                        throw new RuntimeException("Error in async task execution", ex);
                    }
                }, true);  // 新事务中执行
            }, true);  // 新事务中执行
        }
    }

    private void executeInTransaction(Runnable runnable, boolean requiresNew) {
        // 创建事务定义对象并根据需要设置传播行为
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        if (requiresNew) {
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        }

        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            runnable.run();  // 执行传入的任务
            transactionManager.commit(status);  // 提交事务
        } catch (Exception ex) {
            transactionManager.rollback(status);  // 发生异常时回滚事务
            throw ex;  // 重新抛出异常，以便上层处理
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

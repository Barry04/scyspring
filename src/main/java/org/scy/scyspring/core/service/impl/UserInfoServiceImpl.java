package org.scy.scyspring.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.mapper.UserInfoMapper;
import org.scy.scyspring.core.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<String, CompletableFuture<UserInfo>> asyncResultCache = new ConcurrentHashMap<>();

    @Override
    public UserInfo getUserInfoByUserName(String userName) {
        log.info("getUserInfoByUserName:{}", userName);
        LambdaUpdateWrapper<UserInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserInfo::getUsername, userName);
        return getOne(lambdaUpdateWrapper);
    }

    @Override
    public String getUserInfoByFuture(String userName) {
        String key = String.valueOf(new Random().nextInt());
        log.debug("getUserInfoByFuture : {}", userName);
        CompletableFuture<UserInfo> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LambdaUpdateWrapper<UserInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(UserInfo::getUsername, userName);
            future.complete(getOne(lambdaUpdateWrapper));
        });
        asyncResultCache.put(key, future);
        return key;
    }

    @Override
    public CompletableFuture<UserInfo> getUserByFuture(String key) {
        CompletableFuture<UserInfo> asyncResult = asyncResultCache.get(key);
        if (asyncResult != null) {
            asyncResult.thenAccept(result -> {
                // 在方法B中使用异步结果
                System.out.println("Method B received data: " + result);
            }).exceptionally(ex -> {
                System.err.println("Error in Method B: " + ex.getMessage());
                return null;
            });
            return asyncResult;
        } else {
            System.out.println("Method A with key '" + key + "' has not been called yet.");
            CompletableFuture<UserInfo> future = new CompletableFuture<>();
            asyncResultCache.put(key, future);
            return future;
        }

    }
}




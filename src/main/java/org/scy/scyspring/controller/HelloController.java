package org.scy.scyspring.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.LogService;
import org.scy.scyspring.core.service.UserInfoService;
import org.scy.scyspring.core.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private LogService logService;

    @Resource
    private TransactionServiceImpl transactionService;

    @GetMapping(value = "hellos")
    public String hello() {
        log.debug("hello world");
        log.info("hellos ");
        log.error("errors");
        log.warn("warn");
        UserInfo miuWaiLam = userInfoService.getById("10");
        return JSON.toJSONString(miuWaiLam);
    }

    @GetMapping(value = "asyncAloneTransaction")
    public String asyncAloneTransaction() {
        Runnable runnable = () -> {
            List<UserInfo> list = userInfoService.list();
            for (UserInfo userInfo : list) {
                try {
                    if (userInfo.getUsername().contains("u")) {
                        userInfoService.removeById(userInfo.getUuid());
                        asyncService.asyncMethod(5000L);
                        throw new RuntimeException("my asyncAloneTransaction Error");
                    } else {
                        userInfo.setAge(110);
                        userInfoService.updateById(userInfo);
                    }
                } catch (Exception e) {
                    log.error("error msg : {}", e.getMessage(), e);
                }
            }

        };
        // 调用异步方法，不使用独立事务。
        asyncService.asyncAloneTransaction(runnable, false);
        UserInfo byId = userInfoService.getById("10");
        return byId.getUsername();
    }

    @GetMapping(value = "transactionService")
    public String transactionService() {
        Runnable runnable = () -> transactionService.compile();
        // 调用异步方法，不使用独立事务。
        asyncService.asyncAloneTransaction(runnable, false);
        return "ss";
    }


    @GetMapping(value = "asyncAloneTransactionMethod")
    public String asyncAloneTransactionMethod() {

        Runnable runnable = () -> {

            userInfoService.removeById("3");
            asyncService.asyncMethod(5000L);

        };
        // 调用异步方法，不使用独立事务。
        asyncService.asyncAloneTransactionMethod(runnable, true);
        UserInfo byId = userInfoService.getById("2");
        return byId.getUsername();
    }


    @GetMapping(value = "getUserInfoByFuture")
    public String getUserInfoByFuture(@RequestParam String userName) {
        return JSON.toJSONString(userInfoService.getUserInfoByFuture(userName));
    }

    @PostMapping(value = "getUserInfoByUserName")
    public String getUserInfoByUserName(@RequestBody String userName) {
        JSONObject object = JSON.parseObject(userName);
        UserInfo name = userInfoService.getUserInfoByUserName(object.getString("userName"));
        String temp = "{\"age\":66,\"birthday\":1679328000000,\"user_name\":\"Ray Rose\"}";
        return temp;
    }

    @GetMapping(value = "getUserInfoByUserName")
    public String getUserInfoByUserNames(@RequestBody String userName) {
        JSONObject object = JSON.parseObject(userName);
        String temp = "{\"age\":66,\"birthday\":1679328000000,\"user_name\":\"Ray Rose\"}";
        return temp;
    }

    @GetMapping(value = "getUserByFuture")
    public String getUserByFuture(@RequestParam String key) {
        return userInfoService.getUserByFuture(key).toString();
    }

    @GetMapping(value = "getDescMatch")
    public String getDescMatch(@RequestParam String key) {
        return String.valueOf(userInfoService.selectByDescribesMatchQuery(key).size());
    }
}

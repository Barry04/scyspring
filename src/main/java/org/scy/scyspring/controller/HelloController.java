package org.scy.scyspring.controller;


import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.LogService;
import org.scy.scyspring.core.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private LogService logService;

    @GetMapping(value = "hellos")
    public String hello() {
        UserInfo miuWaiLam = userInfoService.getById("10");
        return miuWaiLam.toString();
    }

    @GetMapping(value = "asyncAloneTransaction")
    public String asyncAloneTransaction() {
        Runnable runnable = () -> {
            userInfoService.removeById("10");
            asyncService.asyncMethod(5000L);
            throw new RuntimeException("my asyncAloneTransaction Error");
        };
        // 调用异步方法，不使用独立事务。
        asyncService.asyncAloneTransaction(runnable, true);
        UserInfo byId = userInfoService.getById("10");
        return byId.getUsername();
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
        return userInfoService.getUserInfoByFuture(userName);
    }

    @GetMapping(value = "getUserByFuture")
    public String getUserByFuture(@RequestParam String key) {
        return userInfoService.getUserByFuture(key).toString();
    }
}

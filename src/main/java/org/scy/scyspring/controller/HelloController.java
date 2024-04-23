package org.scy.scyspring.controller;


import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.UserInfoService;
import org.scy.scyspring.utils.ExecutorsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AsyncService asyncService;

    @GetMapping(value = "hellos")
    public String hello() {
        UserInfo miuWaiLam = userInfoService.getUserInfoByUserName("Miu Wai Lam");
        return miuWaiLam.toString();
    }

    @GetMapping(value = "hello")
    public String hello2() {

        Runnable runnable1 = () -> asyncService.asyncMethod(1000L);
        Runnable runnable2 = () -> asyncService.asyncMethod(100L);
        Runnable runnable3 = () -> asyncService.asyncMethod(200L);

        ExecutorsUtils.getFixedThreadPoolExecutor().execute(runnable1);
        ExecutorsUtils.getFixedThreadPoolExecutor().execute(runnable2);
        ExecutorsUtils.getFixedThreadPoolExecutor().execute(runnable3);

        Runnable runnable4 = () -> {
            userInfoService.removeById("1");
            asyncService.asyncMethod(2000L);
            throw new RuntimeException("my Error");
        };
        // 调用异步方法，不使用独立事务。
        asyncService.asyncAloneTransaction(runnable4, false);
        UserInfo byId = userInfoService.getById("1");
        return byId.getUsername();
    }
}

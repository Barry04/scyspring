package org.scy.scyspring.Controller;


import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private UserInfoService userInfoService;
    @GetMapping(value = "hellos")
    public String hello() {
        UserInfo miuWaiLam = userInfoService.getUserInfoByUserName("Miu Wai Lam");
        return miuWaiLam.toString();
    }
}

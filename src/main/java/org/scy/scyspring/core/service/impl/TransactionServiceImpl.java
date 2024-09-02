package org.scy.scyspring.core.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.scy.scyspring.core.domain.UserInfo;
import org.scy.scyspring.core.service.AsyncService;
import org.scy.scyspring.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    public void compile() {
        List<UserInfo> list = userInfoService.list();
        for (UserInfo userInfo : list) {
            try {
                extracted(userInfo);
            } catch (Exception e) {
                log.error("error msg : {}", e.getMessage(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void extracted(UserInfo userInfo) {
        transactionServiceTwo.compile(userInfo);
    }


    List<UserInfo> getDescMatch(String key) {
        return userInfoService.selectByDescribesMatchQuery(key);
    }
}

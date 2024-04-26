package org.scy.scyspring.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.scy.scyspring.core.domain.UserInfo;

import java.util.concurrent.CompletableFuture;

/**
* @author suncy40801
 * &#064;description  针对表【user_info】的数据库操作Service
 * &#064;createDate  2024-04-17 15:46:19
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getUserInfoByUserName(String userName);


    String getUserInfoByFuture(String userName);


    CompletableFuture<UserInfo> getUserByFuture(String key);
}

package org.scy.scyspring.core.service;

import org.scy.scyspring.core.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author suncy40801
* @description 针对表【user_info】的数据库操作Service
* @createDate 2024-04-17 15:46:19
*/
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getUserInfoByUserName(String userName);
}

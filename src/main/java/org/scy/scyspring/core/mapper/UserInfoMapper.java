package org.scy.scyspring.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.scy.scyspring.core.domain.UserInfo;

import java.util.List;

/**
 * @author suncy40801
 * @description 针对表【user_info】的数据库操作Mapper
 * @createDate 2024-04-17 15:46:19
 * @Entity generator.domain.UserInfo
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {


    /**
     * 根据描述模糊匹配查询
     *
     * @param matchDescText
     * @return
     */
    List<UserInfo> selectByDescribesMatchQuery(String matchDescText);
}





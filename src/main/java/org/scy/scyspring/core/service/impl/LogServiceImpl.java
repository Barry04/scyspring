package org.scy.scyspring.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.scy.scyspring.core.domain.Log;
import org.scy.scyspring.core.mapper.LogMapper;
import org.scy.scyspring.core.service.LogService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author suncy40801
 * @description 针对表【log】的数据库操作Service实现
 * @createDate 2024-04-28 16:28:25
 */
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
        implements LogService {


    @Override
    public void insertLog(Log log) {
        save(log);
    }
}





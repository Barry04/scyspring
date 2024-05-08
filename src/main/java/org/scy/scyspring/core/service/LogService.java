package org.scy.scyspring.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.scy.scyspring.core.domain.Log;

/**
 * @author suncy40801
 * @description 针对表【log】的数据库操作Service
 * @createDate 2024-04-28 16:28:25
 */
public interface LogService extends IService<Log> {

    void insertLog(Log log);

}

package org.scy.scyspring.core.service;

import org.springframework.stereotype.Component;

@Component
public interface AsyncService {

    void asyncMethod(Long time);


    void asyncAloneTransaction(Runnable runnable, Boolean aloneTransaction);
}

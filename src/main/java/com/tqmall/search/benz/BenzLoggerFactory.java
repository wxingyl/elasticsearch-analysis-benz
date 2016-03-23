package com.tqmall.search.benz;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * Created by xing on 16/3/21.
 *
 * @author xing
 */
public class BenzLoggerFactory implements ILoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return new BenzLogger(name);
    }

}

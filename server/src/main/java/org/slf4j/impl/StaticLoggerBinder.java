package org.slf4j.impl;

import com.tqmall.search.benz.BenzLogger;
import com.tqmall.search.benz.BenzLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Created by xing on 16/3/21.
 * elasticsearch 封装了logger工具类{@link ESLogger}, 默认使用的是jdk log
 * 该项目依赖com.tqmall.search:commons-nlp, 使用slf4j, 为了保证commons-lang中的log也能正常打出来, 在这儿实现StaticLoggerBinder,
 * 作为Adapter, {@link ILoggerFactory}生产的对象还是{@link ESLogger}
 *
 * @author xing
 * @see BenzLoggerFactory
 * @see BenzLogger
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

    // to avoid constant folding by the compiler, this field must *not* be final
    public static String REQUESTED_API_VERSION = "1.7"; // !final

    private static StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    private final ILoggerFactory loggerFactory;

    private StaticLoggerBinder() {
        loggerFactory = new BenzLoggerFactory();
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return loggerFactory.getClass().getName();
    }
}

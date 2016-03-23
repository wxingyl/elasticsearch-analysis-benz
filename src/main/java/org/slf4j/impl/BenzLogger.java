package org.slf4j.impl;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.logging.jdk.JdkESLoggerFactory;
import org.slf4j.helpers.MarkerIgnoringBase;

/**
 * Created by xing on 16/3/21.
 *
 * @author xing
 * @see ESLoggerFactory
 */
public class BenzLogger extends MarkerIgnoringBase {
    /**
     * {@link ESLogger}默认使用{@link JdkESLoggerFactory}
     * {@link ESLoggerFactory}中默认使用jkd, 再检查是否支持log4j, 如果还没有再检查slf4j, 如果该函数执行, 则表明log4j是没有的, 所以不用考虑log4j
     */
    private final static ESLoggerFactory DEFAULT_LOG_FACTORY;

    static {
        DEFAULT_LOG_FACTORY = BenzLogger.class.getClassLoader().equals(ESLogger.class.getClassLoader())
                ? new JdkESLoggerFactory() : null;
    }

    private final ESLogger logger;

    public BenzLogger(String name) {
        logger = DEFAULT_LOG_FACTORY == null ? Loggers.getLogger(name) : DEFAULT_LOG_FACTORY.newInstance(name);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }
}

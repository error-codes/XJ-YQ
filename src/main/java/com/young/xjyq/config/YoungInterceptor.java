package com.young.xjyq.config;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.dtflys.forest.reflection.ForestMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/14 17:59
 */
@Slf4j
public class YoungInterceptor<T> implements Interceptor<T> {

    @Override
    public void onInvokeMethod(ForestRequest req, ForestMethod method, Object[] args) {
        log.info("[调用方法]: {}\n[方法参数]: {} ", method.getMethodName(), args);
    }

    @Override
    public void onError(ForestRuntimeException ex, ForestRequest req, ForestResponse res) {
        log.info("[请求发送失败, 错误内容]:\n\t[请求路径]: {}\n\t[请求体]: {}\n\t[响应码]: {}\n\t[结果集]: {}",
                req.getUrl(), req.getBody(), res.getStatusCode(), res.getResult());
    }

}

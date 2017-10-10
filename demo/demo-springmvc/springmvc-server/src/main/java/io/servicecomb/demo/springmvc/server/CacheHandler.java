/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.servicecomb.demo.springmvc.server;

import java.util.Map;

import io.servicecomb.common.rest.RestConst;
import io.servicecomb.core.Handler;
import io.servicecomb.core.Invocation;
import io.servicecomb.core.definition.MicroserviceMeta;
import io.servicecomb.core.definition.OperationMeta;
import io.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import io.servicecomb.swagger.invocation.AsyncResponse;
import io.servicecomb.swagger.invocation.InvocationType;
import io.servicecomb.swagger.invocation.Response;

public class CacheHandler implements Handler {
  class CacheParam {
    String[] keys;

    String expire;
  }

  @Override
  public void init(MicroserviceMeta microserviceMeta, InvocationType invocationType) {

  }

  private CacheParam getOrCreateCacheParam(OperationMeta operationMeta) {
    CacheParam cacheParam = operationMeta.getExtData("testCache");
    if (cacheParam != null) {
      return cacheParam;
    }

    @SuppressWarnings("unchecked")
    Map<String, String> param =
        (Map<String, String>) operationMeta.getSwaggerOperation().getVendorExtensions().get("x-cache");
    if (param != null) {
      cacheParam = new CacheParam();
      cacheParam.keys = param.get("key").split(",");
      cacheParam.expire = param.get("expire");
      operationMeta.putExtData("testCache", cacheParam);
    }

    return cacheParam;
  }

  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResp) throws Exception {
    CacheParam cacheParam = getOrCreateCacheParam(invocation.getOperationMeta());
    if (cacheParam == null) {
      invocation.next(asyncResp);
      return;
    }

    HttpServletRequestEx requestEx = (HttpServletRequestEx) invocation.getHandlerContext().get(RestConst.REST_REQUEST);
    if (requestEx == null) {
      invocation.next(asyncResp);
      return;
    }

    StringBuilder sb = new StringBuilder();
    for (String key : cacheParam.keys) {
      sb.append(requestEx.getParameter(key)).append(",");
    }
    String cacheKey = sb.toString();
    Object result = queryFromDCS(cacheKey);
    if (result != null) {
      // cached
      Response response = Response.ok(result);
      response.getHeaders().addHeader("expire", cacheParam.expire);
      asyncResp.handle(response);
      return;
    }

    // no cache
    invocation.next(response -> {
      if (response.isSuccessed()) {
        putDCS(cacheKey, response.getResult());
      }

      response.getHeaders().addHeader("expire", cacheParam.expire);
      asyncResp.handle(response);
    });
  }

  private void putDCS(String cacheKey, Object result) {

  }

  private Object queryFromDCS(String string) {
    return "abc";
  }
}

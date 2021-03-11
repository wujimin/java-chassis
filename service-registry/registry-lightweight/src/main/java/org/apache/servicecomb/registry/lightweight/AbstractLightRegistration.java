/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.registry.lightweight;

import java.util.Collection;

import org.apache.servicecomb.registry.api.Registration;
import org.apache.servicecomb.registry.api.registry.BasePath;
import org.apache.servicecomb.registry.api.registry.Microservice;
import org.apache.servicecomb.registry.api.registry.MicroserviceInstance;
import org.apache.servicecomb.registry.api.registry.MicroserviceInstanceStatus;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLightRegistration implements Registration {
  protected Self self;

  @Autowired
  public AbstractLightRegistration setSelf(Self self) {
    this.self = self;
    return this;
  }

  @Override
  public MicroserviceInstance getMicroserviceInstance() {
    return self.getInstance();
  }

  @Override
  public Microservice getMicroservice() {
    return self.getMicroservice();
  }

  @Override
  public String getAppId() {
    return self.getMicroservice().getAppId();
  }

  @Override
  public boolean updateMicroserviceInstanceStatus(MicroserviceInstanceStatus status) {
    self.getInstance().setStatus(status);
    return true;
  }

  @Override
  public void addSchema(String schemaId, String content) {
    self.addSchema(schemaId, content);
  }

  @Override
  public void addEndpoint(String endpoint) {
    self.getInstance().getEndpoints().add(endpoint);
  }

  @Override
  public void addBasePath(Collection<BasePath> basePaths) {
    self.getMicroservice().getPaths().addAll(basePaths);
  }
}
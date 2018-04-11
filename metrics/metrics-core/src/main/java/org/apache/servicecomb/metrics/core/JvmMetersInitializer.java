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
package org.apache.servicecomb.metrics.core;

import org.apache.servicecomb.foundation.metrics.MetricsBootstrapConfig;
import org.apache.servicecomb.foundation.metrics.MetricsInitializer;

import com.google.common.eventbus.EventBus;
import com.netflix.spectator.api.CompositeRegistry;
import com.netflix.spectator.gc.GcLogger;

public class JvmMetersInitializer implements MetricsInitializer {
//  private Registry registry;

  @Override
  public void init(CompositeRegistry globalRegistry, EventBus eventBus, MetricsBootstrapConfig config) {
    //    DefaultRegistryInitializer defaultRegistryInitializer =
    //        SPIServiceUtils.getTargetService(MetricsInitializer.class, DefaultRegistryInitializer.class);
    //    registry = defaultRegistryInitializer.getRegistry();

    createGcMeters();
  }

  protected void createGcMeters() {
    new GcLogger().start(e -> {
    });
  }

}

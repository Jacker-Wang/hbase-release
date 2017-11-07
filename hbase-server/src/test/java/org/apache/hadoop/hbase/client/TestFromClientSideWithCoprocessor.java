/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.client;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.testclassification.LargeTests;
import org.apache.hadoop.hbase.coprocessor.CoprocessorHost;
import org.apache.hadoop.hbase.coprocessor.MultiRowMutationEndpoint;
import org.apache.hadoop.hbase.regionserver.NoOpScanPolicyObserver;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Test all client operations with a coprocessor that
 * just implements the default flush/compact/scan policy.
 */
@Category(LargeTests.class)
public class TestFromClientSideWithCoprocessor extends TestFromClientSide {
  /** Set to true on Windows platforms */
  private static final boolean WINDOWS = System.getProperty("os.name").startsWith("Windows");

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Configuration conf = TEST_UTIL.getConfiguration();
    conf.setStrings(CoprocessorHost.REGION_COPROCESSOR_CONF_KEY,
        MultiRowMutationEndpoint.class.getName(), NoOpScanPolicyObserver.class.getName());
    conf.setBoolean("hbase.table.sanity.checks", true); // enable for below tests
    // We need more than one region server in this test
    TEST_UTIL.startMiniCluster(SLAVES);
  }

  @Override @Test
  public void testCheckAndDeleteWithCompareOp() throws IOException {
    Assume.assumeTrue(!WINDOWS);
    super.testCheckAndDeleteWithCompareOp();
  }
}
/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.launcher.daemon.testing

import org.gradle.internal.nativeplatform.services.NativeServices
import org.gradle.internal.service.DefaultServiceRegistry
import org.gradle.internal.service.ServiceRegistry
import org.gradle.launcher.daemon.registry.DaemonRegistry
import org.gradle.launcher.daemon.registry.DaemonRegistryServices

class DaemonLogsAnalyzer {

    private List<File> daemonLogs
    private File daemonBaseDir
    private ServiceRegistry services

    DaemonLogsAnalyzer(File daemonBaseDir) {
        this.daemonBaseDir = daemonBaseDir
        assert daemonBaseDir.listFiles().length == 1
        def daemonFiles = daemonBaseDir.listFiles()[0].listFiles()
        daemonLogs = daemonFiles.findAll { it.name.endsWith('.log') }
        services = new DefaultServiceRegistry(NativeServices.instance).addProvider(new DaemonRegistryServices(daemonBaseDir))
    }

    List<TestableDaemon> getDaemons() {
        return daemonLogs.collect { new TestableDaemon(it, registry) }
    }

    TestableDaemon getDaemon() {
        def daemons = getDaemons()
        assert daemons.size() == 1: "Expected only a single daemon."
        daemons[0]
    }

    DaemonRegistry getRegistry() {
        services.get(DaemonRegistry)
    }
}
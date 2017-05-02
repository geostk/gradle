/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.api.internal.plugins;

import org.gradle.api.Nullable;
import org.gradle.api.Plugin;

import static org.gradle.internal.Cast.uncheckedCast;

public class ImperativeOnlyPluginTarget<T> implements PluginTarget {

    private final T target;

    public ImperativeOnlyPluginTarget(T target) {
        this.target = target;
    }

    public void applyImperative(@Nullable String pluginId, Plugin<?> plugin) {
        // TODO validate that the plugin accepts this kind of argument
        Plugin<T> cast = uncheckedCast(plugin);
        cast.apply(target);
    }

    public void applyRules(@Nullable String pluginId, Class<?> clazz) {
        String message = String.format("Cannot apply model rules of plugin '%s' as the target '%s' is not model rule aware", clazz.getName(), target.toString());
        throw new UnsupportedOperationException(message);
    }

    public void applyImperativeRulesHybrid(@Nullable String pluginId, Plugin<?> plugin) {
        applyRules(pluginId, plugin.getClass());
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
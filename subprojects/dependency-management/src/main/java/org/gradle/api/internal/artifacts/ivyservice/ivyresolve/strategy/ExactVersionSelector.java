/*
 * Copyright 2013 the original author or authors.
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
package org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy;

import org.gradle.api.artifacts.ComponentMetadata;

import java.util.Comparator;

/**
 * Version matcher for "static" version selectors (1.0, 1.2.3, etc.).
 */
public class ExactVersionSelector extends AbstractVersionSelector {
    private static final Comparator<String> STATIC_VERSION_COMPARATOR = new StaticVersionComparator();

    public ExactVersionSelector(String selector) {
        super(selector);
    }

    public boolean isDynamic() {
        return false;
    }

    public boolean requiresMetadata() {
        return false;
    }

    public boolean matchesUniqueVersion() {
        return true;
    }

    public boolean accept(String candidate) {
        return getSelector().equals(candidate);
    }

    public boolean accept(ComponentMetadata candidate) {
        return accept(candidate.getId().getVersion());
    }

    /**
     * Compares a static selector with a candidate version. Algorithm is inspired
     * by PHP version_compare one.
     *
     * TODO: compare() is inconsistent with accept(), because not everything
     * that compares equal is accepted (e.g. 1.0 vs. 1_0). Can this cause problems?
     */
    public int compare(String selector, String candidate) {
        return STATIC_VERSION_COMPARATOR.compare(selector, candidate);
    }
}
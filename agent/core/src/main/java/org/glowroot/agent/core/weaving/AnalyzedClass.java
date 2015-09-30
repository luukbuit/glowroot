/*
 * Copyright 2012-2015 the original author or authors.
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
package org.glowroot.agent.core.weaving;

import java.lang.reflect.Modifier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import org.immutables.value.Value;

@Value.Immutable
abstract class AnalyzedClass {

    abstract int modifiers();
    abstract String name();
    abstract @Nullable String superName();
    abstract ImmutableList<String> interfaceNames();
    abstract ImmutableList<AnalyzedMethod> analyzedMethods();
    abstract ImmutableList<ShimType> shimTypes();
    abstract ImmutableList<MixinType> mixinTypes();

    // not using @Value.Derived to keep down memory footprint
    boolean isInterface() {
        return Modifier.isInterface(modifiers());
    }

    // not using @Value.Derived to keep down memory footprint
    boolean isAbstract() {
        return Modifier.isAbstract(modifiers());
    }

    boolean hasReweavableAdvice() {
        for (AnalyzedMethod analyzedMethod : analyzedMethods()) {
            for (Advice advice : analyzedMethod.advisors()) {
                if (advice.reweavable()) {
                    return true;
                }
            }
        }
        return false;
    }
}
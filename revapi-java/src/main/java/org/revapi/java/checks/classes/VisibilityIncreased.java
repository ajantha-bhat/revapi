/*
 * Copyright 2014-2023 Lukas Krejci
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.revapi.java.checks.classes;

import java.util.EnumSet;

import org.revapi.java.checks.common.VisibilityChanged;
import org.revapi.java.spi.Code;
import org.revapi.java.spi.JavaTypeElement;

/**
 * @author Lukas Krejci
 *
 * @since 0.1
 */
public final class VisibilityIncreased extends VisibilityChanged {
    public VisibilityIncreased() {
        super(Code.CLASS_VISIBILITY_INCREASED, true);
    }

    @Override
    public EnumSet<Type> getInterest() {
        return EnumSet.of(Type.CLASS);
    }

    @Override
    protected void doVisitClass(JavaTypeElement oldType, JavaTypeElement newType) {
        super.doVisit(oldType, newType);
    }
}

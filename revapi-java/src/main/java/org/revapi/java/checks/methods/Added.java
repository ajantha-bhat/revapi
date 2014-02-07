/*
 * Copyright 2014 Lukas Krejci
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
 * limitations under the License
 */

package org.revapi.java.checks.methods;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.revapi.MatchReport;
import org.revapi.java.checks.AbstractJavaCheck;

/**
 * @author Lukas Krejci
 * @since 0.1
 */
public class Added extends AbstractJavaCheck {
    private static final Logger LOG = LoggerFactory.getLogger(Added.class);

    private final SimpleElementVisitor7<TypeElement, Void> enclosingClassExtractor = new SimpleElementVisitor7<TypeElement, Void>() {
        @Override
        protected TypeElement defaultAction(Element e, Void ignored) {
            return null;
        }

        @Override
        public TypeElement visitType(TypeElement e, Void ignored) {
            return e;
        }
    };

    @Override
    protected void doVisitMethod(ExecutableElement oldMethod, ExecutableElement newMethod) {
        if (oldMethod == null && newMethod != null) {
            pushActive(null, newMethod);
        }
    }

    @Override
    protected List<MatchReport.Problem> doEnd() {
        ActiveElements<ExecutableElement> methods = popIfActive();
        if (methods == null) {
            return null;
        }

        // we need to consider several cases here:
        // 1) method added to a interface
        // 2) method added to a final class
        // 3) concrete method added to a non-final class
        // 4) abstract method added to a non-final class

        ExecutableElement method = methods.newElement;

        TypeElement enclosingClass = method.accept(enclosingClassExtractor, null);
        if (enclosingClass == null) {
            LOG.warn("Could not find an enclosing class of method " + method + ". That's weird.");
            return null;
        }

        if (enclosingClass.getKind() == ElementKind.INTERFACE) {
            //TODO implement
        } else if (enclosingClass.getModifiers().contains(Modifier.FINAL)) {
            //TODO implement
        } else if (method.getModifiers().contains(Modifier.ABSTRACT)) {
            //TODO implement
        } else {
            //TODO implement
        }

        return null;
    }
}

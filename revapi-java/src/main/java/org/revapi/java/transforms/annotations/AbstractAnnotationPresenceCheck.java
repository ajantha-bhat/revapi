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

package org.revapi.java.transforms.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.SimpleElementVisitor7;

import org.revapi.Configuration;
import org.revapi.Element;
import org.revapi.MatchReport;
import org.revapi.ProblemTransform;
import org.revapi.java.ElementPairVisitor;
import org.revapi.java.JavaModelElement;
import org.revapi.java.checks.Code;

/**
 * @author Lukas Krejci
 * @since 0.1
 */
abstract class AbstractAnnotationPresenceCheck implements ProblemTransform {
    protected Configuration configuration;
    private final String annotationQualifiedName;
    private final Code annotationCheckCode;
    private final Code transformedCode;

    protected AbstractAnnotationPresenceCheck(String annotationQualifiedName, Code annotationCheckCode,
        Code transformedCode) {
        this.annotationQualifiedName = annotationQualifiedName;
        this.annotationCheckCode = annotationCheckCode;
        this.transformedCode = transformedCode;
    }

    @Override
    public void initialize(@Nonnull Configuration configuration) {
        this.configuration = configuration;
    }

    @Nullable
    @Override
    public MatchReport.Problem transform(@Nullable final Element oldElement, @Nullable final Element newElement,
        @Nonnull final MatchReport.Problem problem) {
        if (Code.fromCode(problem.code) != annotationCheckCode) {
            return problem;
        }

        //we're checking for change of presence of an annotation on an element. Thus both the old and new version
        //of the element must be non-null.
        if (oldElement == null || newElement == null) {
            return null;
        }

        AnnotationMirror affectedAnnotation = (AnnotationMirror) problem.attachments.get(0);

        return affectedAnnotation.getAnnotationType().asElement()
            .accept(new SimpleElementVisitor7<MatchReport.Problem, Void>() {
                @Override
                protected MatchReport.Problem defaultAction(javax.lang.model.element.Element e, Void ignored) {
                    return problem;
                }

                @Override
                public MatchReport.Problem visitType(TypeElement e, Void ignored) {
                    if (!annotationQualifiedName.equals(e.getQualifiedName().toString())) {
                        return problem;
                    }

                    JavaModelElement oldE = (JavaModelElement) oldElement;
                    JavaModelElement newE = (JavaModelElement) newElement;

                    return oldE.getModelElement().accept(new ElementPairVisitor<MatchReport.Problem>() {
                        @Override
                        protected MatchReport.Problem unmatchedAction(@Nonnull javax.lang.model.element.Element element,
                            @Nullable javax.lang.model.element.Element otherElement) {
                            return problem;
                        }

                        @Override
                        protected MatchReport.Problem visitType(@Nonnull TypeElement oldElement,
                            @Nonnull TypeElement newElement) {
                            return transformedCode.createProblem(configuration.getLocale());
                        }

                        @Override
                        protected MatchReport.Problem visitPackage(@Nonnull PackageElement element,
                            @Nonnull PackageElement otherElement) {
                            return transformedCode.createProblem(configuration.getLocale());
                        }

                        @Override
                        protected MatchReport.Problem visitVariable(@Nonnull VariableElement element,
                            @Nonnull VariableElement otherElement) {
                            return transformedCode.createProblem(configuration.getLocale());
                        }

                        @Override
                        protected MatchReport.Problem visitExecutable(@Nonnull ExecutableElement element,
                            @Nonnull ExecutableElement otherElement) {
                            return transformedCode.createProblem(configuration.getLocale());
                        }
                    }, newE.getModelElement());
                }
            }, null);
    }

    @Override
    public void close() throws Exception {
    }
}

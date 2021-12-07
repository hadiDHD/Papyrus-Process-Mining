/******************************************************************************
 * Copyright (c) 2021 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Rengin Battal (ARTAL) - rengin.battal@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.all;

import org.eclipse.papyrus.junit.framework.classification.ClassificationSuite;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.edges.Edge_Abstraction_DeleteSemanticTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.edges.Edge_Dependency_DeleteSemanticTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.edges.Edge_Generalization_DeleteSemanticTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.nodes.TopNode_Class_DeleteSemanticTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.nodes.TopNode_Comment_DeleteSemanticTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.nodes.TopNode_DurationObservation_DeleteSemanticTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.semantic.nodes.TopNode_PackageWithSubNodes_DeleteSemanticTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.edges.Edge_Abstraction_DeleteViewTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.edges.Edge_Dependency_DeleteViewTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.edges.Edge_Generalization_DeleteViewTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.nodes.TopNode_Class_DeleteViewTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.nodes.TopNode_Comment_DeleteViewTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.nodes.TopNode_DurationObservation_DeleteViewTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.delete.view.nodes.TopNode_PackageWithSubNodes_DeleteViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All nodes/edges delete tests
 */
@RunWith(ClassificationSuite.class)
@SuiteClasses({
		TopNode_Class_DeleteSemanticTest.class,
		TopNode_Class_DeleteViewTest.class,
		TopNode_Comment_DeleteSemanticTest.class,
		TopNode_Comment_DeleteViewTest.class,
		TopNode_DurationObservation_DeleteSemanticTest.class,
		TopNode_DurationObservation_DeleteViewTest.class,
		Edge_Dependency_DeleteSemanticTest.class,
		Edge_Abstraction_DeleteSemanticTest.class,
		Edge_Generalization_DeleteSemanticTest.class,
		TopNode_PackageWithSubNodes_DeleteSemanticTest.class,
		TopNode_PackageWithSubNodes_DeleteViewTest.class,
		Edge_Dependency_DeleteViewTest.class,
		Edge_Abstraction_DeleteViewTest.class,
		Edge_Generalization_DeleteViewTest.class
})
public class AllDeleteTests {
}

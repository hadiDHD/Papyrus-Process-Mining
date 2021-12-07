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
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Class_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Comment_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Component_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Constraint_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.DataType_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.DurationObservation_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Enumeration_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.InformationItem_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.InstanceSpecification_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Interface_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Model_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Package_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.PrimitiveType_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.Signal_TopNode_DropTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes.TimeObservation_TopNode_DropTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All top nodes drop tests
 */
@RunWith(ClassificationSuite.class)
@SuiteClasses({
		Class_TopNode_DropTest.class,
		Comment_TopNode_DropTest.class,
		Component_TopNode_DropTest.class,
		Constraint_TopNode_DropTest.class,
		DataType_TopNode_DropTest.class,
		PrimitiveType_TopNode_DropTest.class,
		Enumeration_TopNode_DropTest.class,
		DurationObservation_TopNode_DropTest.class,
		InformationItem_TopNode_DropTest.class,
		InstanceSpecification_TopNode_DropTest.class,
		Model_TopNode_DropTest.class,
		Interface_TopNode_DropTest.class,
		Package_TopNode_DropTest.class,
		Signal_TopNode_DropTest.class,
		TimeObservation_TopNode_DropTest.class,
})
public class AllTopNodesDropTests {
}

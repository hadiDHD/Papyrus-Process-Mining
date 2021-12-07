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
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Class_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Comment_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Component_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Constraint_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_DataType_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_DurationObservation_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Enumeration_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_InformationItem_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_InstanceSpecification_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Interface_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Model_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Package_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_PrimitiveType_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_Signal_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.TopNode_TimeObservation_CreationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All top nodes creation test
 */
@RunWith(ClassificationSuite.class)
@SuiteClasses({
		TopNode_Class_CreationTest.class,
		TopNode_Comment_CreationTest.class,
		TopNode_Component_CreationTest.class,
		TopNode_Constraint_CreationTest.class,
		TopNode_DataType_CreationTest.class,
		TopNode_DurationObservation_CreationTest.class,
		TopNode_Enumeration_CreationTest.class,
		TopNode_InformationItem_CreationTest.class,
		TopNode_InstanceSpecification_CreationTest.class,
		TopNode_Interface_CreationTest.class,
		TopNode_Model_CreationTest.class,
		TopNode_Package_CreationTest.class,
		TopNode_PrimitiveType_CreationTest.class,
		TopNode_Signal_CreationTest.class,
		TopNode_TimeObservation_CreationTest.class,
})
public class AllTopNodesCreationTests {
}

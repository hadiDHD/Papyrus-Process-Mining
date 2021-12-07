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
package org.eclipse.papyrus.uml.siriusdiag.sequence.tests.all;

import org.eclipse.papyrus.junit.framework.classification.ClassificationSuite;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_ActionExecution_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_CombinedFragment_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_Comment_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_Constraint_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_Gate_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_InteractionOperand_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_InteractionUse_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_Lifeline_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.sequence.tests.creation.nodes.Node_StateInvariant_CreationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All Nodes creation tests
 */
@RunWith(ClassificationSuite.class)
@SuiteClasses({
		Node_ActionExecution_CreationTest.class,
		Node_CombinedFragment_CreationTest.class,
		Node_Comment_CreationTest.class,
		Node_Constraint_CreationTest.class,
		Node_InteractionUse_CreationTest.class,
		Node_Lifeline_CreationTest.class,
		Node_StateInvariant_CreationTest.class,
		Node_Gate_CreationTest.class,
		Node_InteractionOperand_CreationTest.class

})
public class AllNodesCreationTests {

}

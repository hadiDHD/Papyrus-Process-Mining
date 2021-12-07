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
 *  Aurélien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.siriusdiag.statemachine.tests;

import org.eclipse.papyrus.junit.framework.classification.ClassificationSuite;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.FinalStateCreationTest;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.PseudostateCreationTest;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.StateBorderedNodesCreationTest;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.State_statemachine_CreationTest;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.edges.TestTransitionLinks;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.edges.Transition_statemachine_EdgeCreation;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.delete.semantic.StateDelete;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All Nodes creation tests
 */
@RunWith(ClassificationSuite.class)
@SuiteClasses({
	FinalStateCreationTest.class,
	PseudostateCreationTest.class,
	State_statemachine_CreationTest.class,
	StateBorderedNodesCreationTest.class,
	TestTransitionLinks.class,
	Transition_statemachine_EdgeCreation.class,
	StateDelete.class,

})
public class AllStateMachineTests {

}

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
 *  Etienne Allogo (ARTAL) - etienne.allogo@artal.fr - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation;

import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.uml2.uml.FinalState;
import org.junit.Test;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/nodes/create_statemachine_node.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class FinalStateCreationTest extends AbstractStatemachineTopNodeCreationTest {

	private static final String STATEMACHINE_TAB = "create_statemachine_node";

	private static final String STATE_SUB_NODE_TAB = "create_state_subnode";
	
	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void test_create_state_subnode_final_state() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();

		createAndCheckStateNode("FinalState", DNode.class, FinalState.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB) // open the diagram
	public void test_create_statemachine_final_state() {
		createAndCheckStateNode("FinalState", DNode.class, FinalState.class);
	}
}

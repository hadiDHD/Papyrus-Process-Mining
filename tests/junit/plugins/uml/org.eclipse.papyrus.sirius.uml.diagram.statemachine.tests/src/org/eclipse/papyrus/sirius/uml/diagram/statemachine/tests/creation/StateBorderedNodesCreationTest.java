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

package org.eclipse.papyrus.sirius.uml.diagram.statemachine.tests.creation;

import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.uml2.uml.Pseudostate;
import org.junit.Assert;
import org.junit.Test;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/nodes/create_statemachine_node.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class StateBorderedNodesCreationTest extends AbstractStatemachineTopNodeCreationTests {

	private static final String STATEMACHINE_TAB = "create_statemachine_node";

	private static final String STATE_SUB_NODE_TAB = "create_state_subnode";
	

	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void test_create_state_subnode_entry_point() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();

		// create sub state container
		AbstractDNode stateSiriusElement = createStateNode(subStateContainer, "State");

		 // create entry point Bordered Node
		 Assert.assertTrue("The state not should not have any existing bordered node before ", stateSiriusElement.getOwnedBorderedNodes().isEmpty());
		 createAndCheckStateNode("EntryPoint", DNode.class, Pseudostate.class,  DNodeContainer.class.cast(stateSiriusElement));
		 Assert.assertEquals("The entry point should be a new borrderedNode after creation ", 1, stateSiriusElement.getOwnedBorderedNodes().size());

	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB) // open the diagram
	public void test_create_statemachine_entry_point() {
		// create sub state container
		AbstractDNode stateSiriusElement = createStateNode("State");

		 // create entry point Bordered Node
		 Assert.assertTrue("The state not should not have any existing bordered node before ", stateSiriusElement.getOwnedBorderedNodes().isEmpty());
		 createAndCheckStateNode("EntryPoint", DNode.class, Pseudostate.class,  DNodeContainer.class.cast(stateSiriusElement));
		 Assert.assertEquals("The entry point should be a new borrderedNode after creation ", 1, stateSiriusElement.getOwnedBorderedNodes().size());

	}

	
	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void test_create_state_subnode_exit_point() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();

		// create sub state container
		AbstractDNode stateSiriusElement = createStateNode(subStateContainer, "State");

		 // create exit point Bordered Node
		 Assert.assertTrue("The state not should not have any existing bordered node before ", stateSiriusElement.getOwnedBorderedNodes().isEmpty());
		 createAndCheckStateNode("ExitPoint", DNode.class, Pseudostate.class,  DNodeContainer.class.cast(stateSiriusElement));
		 Assert.assertEquals("The create exit point node should be a bordered node ", 1, stateSiriusElement.getOwnedBorderedNodes().size());

	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB) // open the diagram
	public void test_create_statemachine_exit_point() {
		// create sub state container
		AbstractDNode stateSiriusElement = (AbstractDNode) createStateNode("State");

		 // create exit point Bordered Node
		 Assert.assertTrue("The state not should not have any existing bordered node before ", stateSiriusElement.getOwnedBorderedNodes().isEmpty());
		 createAndCheckStateNode("ExitPoint", DNode.class, Pseudostate.class,  DNodeContainer.class.cast(stateSiriusElement));
		 Assert.assertEquals("The create exit point node should be a bordered node ", 1, stateSiriusElement.getOwnedBorderedNodes().size());

	}
}

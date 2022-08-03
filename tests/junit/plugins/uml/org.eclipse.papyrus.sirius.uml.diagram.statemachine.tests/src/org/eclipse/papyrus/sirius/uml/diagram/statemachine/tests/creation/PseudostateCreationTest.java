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

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.uml2.uml.Pseudostate;
import org.junit.Test;

/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/nodes/create_statemachine_node.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class PseudostateCreationTest extends AbstractStatemachineTopNodeCreationTests {

	private static final String STATEMACHINE_TAB = "create_statemachine_node";

	private static final String STATE_SUB_NODE_TAB = "create_state_subnode";


	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB)
	public void test_create_state_subnode_terminate() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("Terminate", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB)
	public void test_create_statemachine_terminate() {
		createAndCheckStateNode("Terminate", DNode.class, Pseudostate.class);
	}

	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void test_create_state_subnode_choice() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("Choice", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB)
	public void test_create_statemachine_choice() {
		DNode choiceSource = (DNode) createStateNode("Choice");
		// GraphicalEditPart choiceEditPart = (GraphicalEditPart) fixture.findEditPart(choiceNode);
		fixture.flushDisplayEvents();

		DNode choiceTarget = (DNode) createStateNode("Choice");
		GraphicalEditPart choiceTargetEditPart = (GraphicalEditPart) fixture.findEditPart(choiceTarget);
		fixture.move(choiceTargetEditPart, choiceTargetEditPart.getFigure().getBounds().translate(100, 100).getLocation());
		fixture.flushDisplayEvents();

		fixture.applyEdgeCreationTool("Transition", getRootSiriusRegion().getParentDiagram(), choiceSource, choiceTarget);
		fixture.flushDisplayEvents();
		fixture.flushDisplayEvents();

	}

	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB)
	public void test_create_state_subnode_deep_history() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("DeepHistory", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB)
	public void test_create_statemachine_deep_history() {
		createAndCheckStateNode("DeepHistory", DNode.class, Pseudostate.class);
	}


	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB)
	public void test_create_state_subnode_fork() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("Fork", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB)
	public void test_create_statemachine_fork() {
		createAndCheckStateNode("Fork", DNode.class, Pseudostate.class);
	}


	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB)
	public void test_create_state_subnode_initial() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("Initial", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB)
	public void test_create_statemachine_initial() {
		createAndCheckStateNode("Initial", DNode.class, Pseudostate.class);
	}

	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB)
	public void test_create_state_subnode_join() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("Join", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB)
	public void test_create_statemachine_join() {
		createAndCheckStateNode("Join", DNode.class, Pseudostate.class);
	}

	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void test_create_state_junction() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("Junction", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB) // open the diagram
	public void test_create_statemachine_junction() {
		createAndCheckStateNode("Junction", DNode.class, Pseudostate.class);
	}


	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void test_create_state_subnode_shallow_history() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();
		createAndCheckStateNode("ShallowHistory", DNode.class, Pseudostate.class, subStateContainer);
	}

	@Test
	@ActiveDiagram(STATEMACHINE_TAB) // open the diagram
	public void test_create_statemachine_shallow_history() {
		createAndCheckStateNode("ShallowHistory", DNode.class, Pseudostate.class);
	}

	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void test_all_subnodes() {

	}
}

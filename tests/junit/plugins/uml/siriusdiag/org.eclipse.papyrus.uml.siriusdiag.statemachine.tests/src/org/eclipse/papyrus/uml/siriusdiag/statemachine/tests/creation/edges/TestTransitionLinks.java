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

package org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.edges;

import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.AbstractStatemachineTopNodeCreationTest;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.junit.Assert;
import org.junit.Test;

@PluginResource("resources/nodes/create_statemachine_node.di") // the resource to import for the test in the workspace
public class TestTransitionLinks extends AbstractStatemachineTopNodeCreationTest {
	
	private static final String STATEMACHINE_TAB = "create_statemachine_node";
	private static final String STATE_SUB_NODE_TAB = "create_state_subnode";


	@Test
	@ActiveDiagram(STATEMACHINE_TAB) // open the diagram
	public void testTransitionLink() {
		AbstractDNode stateEP = createStateNode("State");
		AbstractDNode pseudostateEP = createStateNode("Choice");
		checkTransitionLink(stateEP, pseudostateEP);
	}

	
	@Test
	@ActiveDiagram(STATEMACHINE_TAB) // open the diagram
	public void testTransitionLinkWithSameSourceAndTarget() {
		AbstractDNode stateEP = createStateNode("State");
		checkTransitionLink(stateEP, stateEP);
	}
	
	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void testTransitionLinkSub() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();

		AbstractDNode stateEP = createStateNode(subStateContainer, "State");
		AbstractDNode pseudostateEP = createStateNode(subStateContainer, "Choice");
		checkTransitionLink(stateEP, pseudostateEP);
	}


	
	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB) // open the diagram
	public void testTransitionLinkWithSameSourceAndTargetSub() {
		final var subStateContainer = checkAndGetEmptyStateNodeRegion();

		AbstractDNode stateEP = createStateNode(subStateContainer, "State");
		checkTransitionLink(stateEP, stateEP);
	}



	private DEdge checkTransitionLink(AbstractDNode source, AbstractDNode target) {
		return checkTransitionLink((EdgeTarget)source, (EdgeTarget)target, 1);
	}

	private DEdge checkTransitionLink(EdgeTarget source, EdgeTarget target, int expectedConnections) {
		final var diagram = getRootSiriusRegion().getParentDiagram();
		boolean endCommand = fixture.applyEdgeCreationTool("Transition", diagram, source, target);
		Assert.assertTrue(endCommand);
		Assert.assertEquals(expectedConnections, diagram.getEdges().size());
		DEdge transitionConnection = diagram.getEdges().get(expectedConnections - 1);
		Assert.assertEquals(transitionConnection.getSemanticElements().get(0).eContainer(), ((AbstractDNode)source).getSemanticElements().get(0).eContainer());
		return transitionConnection;
	}
}

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
package org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.delete.semantic;

import org.eclipse.gef.EditPart;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.AbstractStatemachineTopNodeCreationTest;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.uml2.uml.Region;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
/**
 * adapted from org.eclipse.papyrus.uml.diagram.clazz.test.canonical.TestClassDiagramTopNode
 */
@PluginResource("resources/nodes/create_statemachine_node.di") // the resource to import for the test in the workspace
@SuppressWarnings("nls")
public class StateDelete extends AbstractStatemachineTopNodeCreationTest {


	private static final String STATE_SUB_NODE_TAB = "create_state_subnode";



	@Test
	@ActiveDiagram(STATE_SUB_NODE_TAB)
	public void destroySemanticElementFromDiagram() {
		Region root = (Region) getRootSiriusRegion().getSemanticElements().get(0);

		DNodeContainer stateNode = (DNodeContainer) getExistingStateNode();
		EditPart toSelect = fixture.findEditPart(stateNode);
		Assert.assertNotNull("We don't found the Editpart to select for destruction", toSelect);

		fixture.applySemanticDeletionTool(stateNode);
		
		this.fixture.flushDisplayEvents();
		// the semantic element has been destroy
		Assert.assertNull("The UML model must not contain the destroyed element", root.getMember("State1"));
		Assert.assertNull("The class diagram must not contain view after destruction of the class", getExistingStateNode());

		// undo
		this.fixture.getEditingDomain().getCommandStack().undo();
		Assert.assertNotNull("The UML model must contain the destroyed element after undo", root.getMember("State1"));
		Assert.assertNotNull("The class diagram must contain view after destruction of the class after undo", getExistingStateNode());

		// redo
		this.fixture.getEditingDomain().getCommandStack().redo();
		Assert.assertNull("The UML model must not contain the destroyed element", root.getMember("State1"));
		Assert.assertNull("The class diagram must not contain view after destruction of the class", getExistingStateNode());
	}

}

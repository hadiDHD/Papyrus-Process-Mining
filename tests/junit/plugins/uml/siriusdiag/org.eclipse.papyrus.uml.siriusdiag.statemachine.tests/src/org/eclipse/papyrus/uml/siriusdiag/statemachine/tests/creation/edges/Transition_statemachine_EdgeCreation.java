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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.siriusdiag.statemachine.tests.creation.AbstractStatemachineTopNodeCreationTest;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Test;

@PluginResource("resources/edges/test_create_sm_edges.di")
public class Transition_statemachine_EdgeCreation extends AbstractStatemachineTopNodeCreationTest {

	protected DDiagramElement findNodeWithText(String text) {
		DDiagramElementContainer rootNode = getRootSiriusRegion();
		return findNodeWithText(rootNode, text);
	}

	/**
	 * @param rootNode
	 * @param text
	 */
	private DDiagramElement findNodeWithText(DDiagramElementContainer rootNode, String text) {
		EList<DDiagramElement> elements = rootNode.getElements();
		for (DDiagramElement dDiagramElement : elements) {
			if (isSemanticElementHasText(text, dDiagramElement)) {
				return dDiagramElement;
			}
			if (dDiagramElement instanceof DDiagramElementContainer) {
				DDiagramElement subFind = findNodeWithText((DDiagramElementContainer) dDiagramElement, text);
				if (subFind != null) {
					return subFind;
				}
			}
		}
		return null;
	}

	/**
	 * @param text
	 * @param dDiagramElement
	 */
	private boolean isSemanticElementHasText(String text, DDiagramElement dDiagramElement) {
		EList<EObject> semantics = dDiagramElement.getSemanticElements();
		if (!semantics.isEmpty()) {
			EObject semantic = semantics.get(0);
			String name = null;
			if (semantic instanceof NamedElement) {
				name = ((NamedElement) semantic).getName();
			}
			else if(semantic instanceof Comment) {
				name = ((Comment) semantic).getBody();
			}
			return name != null && name.equals(text);
		}
		return false;
	}
	private static final String DIAGRAM_NAME = "test_create_sm_edges";

	@Test
	@ActiveDiagram(DIAGRAM_NAME) // open the diagram
	public void test_create_sm_transition() {
		DDiagramElement init00 = findNodeWithText("Initial00");
		DDiagramElement init10 = findNodeWithText("Initial10");
		DDiagramElement entyPoint100 = findNodeWithText("EntryPoint100");
		DDiagramElement comment0 = findNodeWithText("Comment0");

		fixture.applyEdgeCreationTool("Transition", getSirusDiagram(), (EdgeTarget)init00, (EdgeTarget)entyPoint100);
		fixture.flushDisplayEvents();

		fixture.applyEdgeCreationTool("Transition", getSirusDiagram(), (EdgeTarget)init00, (EdgeTarget)entyPoint100);
		fixture.flushDisplayEvents();

		fixture.applyEdgeCreationTool("Transition", getSirusDiagram(), (EdgeTarget)entyPoint100, (EdgeTarget)init10);
		fixture.flushDisplayEvents();
		
		fixture.applyEdgeCreationTool("Link", getSirusDiagram(), (EdgeTarget)comment0, (EdgeTarget)init10);
		fixture.flushDisplayEvents();
		fixture.flushDisplayEvents();


	}

	/**
	 * @return
	 */
	private DDiagram getSirusDiagram() {
		return getRootSiriusRegion().getParentDiagram();
	}
}

/*****************************************************************************
 * Copyright (c) 2022 CEA LIST
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Vincent Lorenzo (CEA LIST) <vincent.lorenzo@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.creation.topNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.GraphicalOwnerUtils;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.SemanticAndGraphicalCreationChecker;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.viewpoint.DMappingBased;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.uml2.uml.Package;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

/**
 * Abstract Test for Node Creation
 */
public abstract class AbstractCreateNodeTests {

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	/**
	 * the root of the model
	 */
	protected Package root;

	/**
	 * This method set up the test environment
	 */
	@Before
	public void setUp() {
		this.root = this.fixture.getModel();
	}

	/**
	 * 
	 * @return
	 *         the semantic owner to use for the test
	 */
	protected abstract EObject getSemanticOwner();

	/**
	 * 
	 * @return
	 *         the "top" node of the graphical container
	 */
	protected abstract EObject getTopGraphicalContainer();

	/**
	 * 
	 * @param mappingID
	 *            the mapping used by the subnode
	 * @return
	 *         the subnode of the top graphical Container with the expected mappingID. basically this method is used to find a compartment in the node
	 */
	protected EObject getSubNodeOfGraphicalContainer(final String mappingID) {
		final EObject container = getTopGraphicalContainer();
		if (container instanceof DMappingBased && ((DMappingBased) container).getMapping().getName().equals(mappingID)) {
			return container;
		}
		if (container instanceof DNodeContainer) {
			for (final DDiagramElement diagramElement : ((DNodeContainer) container).getOwnedDiagramElements()) {
				if (mappingID.equals(diagramElement.getMapping().getName())) {
					return diagramElement;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param creationToolId
	 *            the ID of the creation tool to used
	 * @param checker
	 *            the checker used to validate the creation of the node
	 * @param graphicalContainer
	 *            the graphical container to use to create the node
	 */
	protected void createNode(final String creationToolId, final SemanticAndGraphicalCreationChecker checker, final EObject graphicalContainer) {
		Assert.assertEquals("The graphical container must be empty before the creation of the node.", 0, GraphicalOwnerUtils.getGraphicalOwnerChildrenSize(graphicalContainer)); //$NON-NLS-1$
		boolean result = fixture.applyContainerCreationTool(creationToolId, getDDiagram(), graphicalContainer);
		Assert.assertTrue("The creation of element failed", result); //$NON-NLS-1$
		fixture.flushDisplayEvents();

		Assert.assertEquals("The graphical container must contains only 1 additional element after the creation of the node.", 1, GraphicalOwnerUtils.getGraphicalOwnerChildrenSize(graphicalContainer)); //$NON-NLS-1$
		final EObject createdElementRepresentation = GraphicalOwnerUtils.getChildren(graphicalContainer).get(0);
		Assert.assertTrue(createdElementRepresentation instanceof DRepresentationElement);

		checker.validateNode((DRepresentationElement) createdElementRepresentation);

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		checker.validateAfterUndo();


		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		checker.validateAfterRedo();
	}

	/**
	 * 
	 * @return
	 *         the active GMF {@link Diagram
	 */
	protected final Diagram getDiagram() {
		return this.fixture.getActiveDiagram().getDiagramView();
	}

	/**
	 * 
	 * @return
	 *         the active Sirius {@link DDiagram}
	 */
	protected final DDiagram getDDiagram() {
		return (DDiagram) getDiagram().getElement();
	}

	/**
	 * This method clean the test environment
	 */
	@After
	public void tearDown() {
		this.root = null;
	}
}

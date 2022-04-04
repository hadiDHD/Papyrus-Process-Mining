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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.topNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.IGraphicalNodeChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.ISemanticNodeChecker;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DragAndDropTarget;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

/**
 * Abstract Class for Drop Top Node tests
 */
public abstract class AbstractTopNodeDropTests {

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	/**
	 * The root of the model
	 */
	protected Package root;

	protected Diagram diagram;

	protected DDiagram diagramRepresentation;

	/**
	 * Init the test field
	 */
	@Before
	public void setUp() {
		this.root = (Package) this.fixture.getModel();
		Assert.assertNotNull(this.root);
		final DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		diagram = diagramEditpart.getDiagramView();

		diagramRepresentation = (DDiagram) diagram.getElement();
	}

	/**
	 * 
	 * @param dropToolId
	 *            the id of the drop tool to use
	 * @param elementMappingType
	 *            the expected mapping type of the created view
	 * @param elementToBeDropped
	 *            the element to drop
	 */
	protected void dropNode(final String dropToolId, final IGraphicalNodeChecker graphicalChecker, final ISemanticNodeChecker semanticChecker, final Element elementToBeDropped) {
		DragAndDropTarget dropTarget = getTopNodeGraphicalContainer();
		Assert.assertNotNull(dropTarget);

		boolean result = fixture.applyContainerDropDescriptionTool(diagramRepresentation, dropToolId, dropTarget, elementToBeDropped);
		Assert.assertTrue("The drop of element failed", result); //$NON-NLS-1$
		fixture.flushDisplayEvents();

		EObject createdElementRepresentation = null;
		if (dropTarget instanceof DNodeContainer) {
			createdElementRepresentation = ((DNodeContainer) dropTarget).getOwnedDiagramElements().get(0);
		}
		if (dropTarget instanceof DSemanticDiagram) {
			createdElementRepresentation = ((DSemanticDiagram) dropTarget).getOwnedDiagramElements().get(0);
		}

		Assert.assertTrue(createdElementRepresentation instanceof DRepresentationElement);

		graphicalChecker.validateNode((DRepresentationElement) createdElementRepresentation);
		semanticChecker.validateNode((DRepresentationElement) createdElementRepresentation);

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		graphicalChecker.validateAfterUndo();
		semanticChecker.validateAfterUndo();

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();

		graphicalChecker.validateAfterRedo();
		semanticChecker.validateAfterRedo();
	}

	protected abstract DragAndDropTarget getTopNodeGraphicalContainer();

	/**
	 * Release resources
	 */
	@After
	public void tearDown() {
		this.root = null;
	}
}

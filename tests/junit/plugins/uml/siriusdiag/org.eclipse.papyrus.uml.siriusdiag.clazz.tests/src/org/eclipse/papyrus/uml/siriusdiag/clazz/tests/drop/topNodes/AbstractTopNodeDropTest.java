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

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.sirusdiag.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

/**
 * Abstract Class for Drop Top Node tests
 */
public abstract class AbstractTopNodeDropTest {

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	/**
	 * The root of the model
	 */
	protected Package root;

	/**
	 * Init the test field
	 */
	@Before
	public void setUp() {
		this.root = (Package) this.fixture.getModel();
		Assert.assertNotNull(this.root);
	}

	/**
	 * 
	 * @param elementToBeDropped
	 *            the element to drop
	 * @param dropToolId
	 *            the id of the drop tool to use
	 * @param elementMappingType
	 *            the expected mapping type of the created view
	 */
	protected void dropDNodeListElement(final Element elementToBeDropped, final String dropToolId, final String elementMappingType) {
		final DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		final Diagram diagram = diagramEditpart.getDiagramView();
		Assert.assertEquals("The diagram must not yet have children.", 0, diagram.getChildren().size()); //$NON-NLS-1$

		final DDiagram diagramRepresentation = (DDiagram) diagram.getElement();
		Assert.assertEquals("The diagram representation must not yet have children.", 0, diagramRepresentation.getDiagramElements().size());//$NON-NLS-1$
		fixture.applyContainerDropDescriptionTool(diagramRepresentation, dropToolId, diagramRepresentation, elementToBeDropped);
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram must have one child after the Drop action.", 1, diagram.getChildren().size()); //$NON-NLS-1$
		Assert.assertEquals("The diagram representation must have one child after the Drop action.", 1, diagramRepresentation.getOwnedRepresentationElements().size());//$NON-NLS-1$
		final DDiagramElement elementRepresentation = diagramRepresentation.getDiagramElements().get(0);
		Assert.assertEquals("The mapping type of the created representation is not the expected one.", elementMappingType, elementRepresentation.getMapping().getName());//$NON-NLS-1$
		Assert.assertTrue("The created sirus node must be a DNodeList", elementRepresentation instanceof DNodeList);//$NON-NLS-1$
		Assert.assertEquals("Only one semantic element must be associated to the representation.", 1, ((DNodeList) elementRepresentation).getSemanticElements().size());//$NON-NLS-1$
		Assert.assertTrue("The semantic element associated to the view must be the dropped element.", elementToBeDropped == ((DNodeList) elementRepresentation).getSemanticElements().get(0));//$NON-NLS-1$

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must contain any children after undoig the Drop action", 0, diagram.getChildren().size()); //$NON-NLS-1$
		Assert.assertEquals("The diagram representation must contain any children after undoig the Drop action", 0, diagramRepresentation.getDiagramElements().size()); //$NON-NLS-1$

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must have one child after redoing of the Drop action.", 1, diagram.getChildren().size()); //$NON-NLS-1$
		Assert.assertEquals("The diagram representation must have one child after redoing of the Drop action.", 1, diagramRepresentation.getOwnedDiagramElements().size());//$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param elementToBeDropped
	 *            the element to drop
	 * @param dropToolId
	 *            the id of the drop tool to use
	 * @param elementMappingType
	 *            the expected mapping type of the created view
	 */
	protected void dropDNodeContainer(final Element elementToBeDropped, final String dropToolId, final String elementMappingType) {
		final DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		final Diagram diagram = diagramEditpart.getDiagramView();
		Assert.assertEquals("The diagram must not yet have children.", 0, diagram.getChildren().size()); //$NON-NLS-1$

		final DDiagram diagramRepresentation = (DDiagram) diagram.getElement();
		Assert.assertEquals("The diagram representation must not yet have children.", 0, diagramRepresentation.getDiagramElements().size());//$NON-NLS-1$
		fixture.applyContainerDropDescriptionTool(diagramRepresentation, dropToolId, diagramRepresentation, elementToBeDropped);
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram must have one child after the Drop action.", 1, diagram.getChildren().size()); //$NON-NLS-1$
		Assert.assertEquals("The diagram representation must have one child after the Drop action.", 1, diagramRepresentation.getOwnedRepresentationElements().size());//$NON-NLS-1$
		final DDiagramElement elementRepresentation = diagramRepresentation.getDiagramElements().get(0);
		Assert.assertEquals("The mapping type of the created representation is not the expected one.", elementMappingType, elementRepresentation.getMapping().getName());//$NON-NLS-1$
		Assert.assertTrue("The created sirus node must be a DNodeContainer", elementRepresentation instanceof DNodeContainer);//$NON-NLS-1$
		Assert.assertEquals("Only one semantic element must be associated to the representation.", 1, ((DNodeContainer) elementRepresentation).getSemanticElements().size());//$NON-NLS-1$
		Assert.assertTrue("The semantic element associated to the view must be the dropped element.", elementToBeDropped == ((DNodeContainer) elementRepresentation).getSemanticElements().get(0));//$NON-NLS-1$

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must contain any children after undoig the Drop action", 0, diagram.getChildren().size()); //$NON-NLS-1$
		Assert.assertEquals("The diagram representation must contain any children after undoig the Drop action", 0, diagramRepresentation.getDiagramElements().size()); //$NON-NLS-1$

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals("The diagram must have one child after redoing of the Drop action.", 1, diagram.getChildren().size()); //$NON-NLS-1$
		Assert.assertEquals("The diagram representation must have one child after redoing of the Drop action.", 1, diagramRepresentation.getOwnedDiagramElements().size());//$NON-NLS-1$
	}

	/**
	 * Release resources
	 */
	@After
	public void tearDown() {
		this.root = null;
	}
}

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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.drop.subNodes;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramConstants;
import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

/**
 * 
 * TODO //adapter from AbstractSubNodeListElementCreationTests maybe a common class could help us
 */
public abstract class AbstractSemanticDropSubNodeTests<T extends Element> {

	/**
	 * this fixture is used to access to Papyrus environment (editor/diagram/commandstack/editingdomain/...)
	 */
	@Rule
	public final SiriusDiagramEditorFixture fixture = new SiriusDiagramEditorFixture();

	protected Package root;

	protected T semanticOwner;

	@Before
	public void setUp() {
		this.root = this.fixture.getModel();
		this.semanticOwner = getSemanticOwner();
		// Assert.assertEquals(1, semanticOwner.getOwnedElements().size()); //depends on the initial model
		// TODO : to be upddated later
	}

	protected abstract T getSemanticOwner();

	/**
	 * 
	 * @param compartmentMappingType
	 *            the type of the compartment in which we want to do the creation
	 * @param creationToolId
	 *            the id of the creation tool to test
	 * @param expectedMappingType
	 *            the expected mapping type for the created view
	 * @return
	 *         the created NodeListElement
	 */
	protected final DDiagramElement dropNodeInDNodeContainer(final String compartmentMappingType, final String dropToolId, final String expectedMappingType, final Element elementToBeDropped) {
		final Diagram diagram = getClassDiagram();
		Assert.assertEquals("The root model must have only one node element before dropping the sub node", 1, diagram.getChildren().size()); //$NON-NLS-1$
		final Object firstView = diagram.getChildren().get(0);

		Assert.assertTrue(((View) firstView).getElement() instanceof DNodeContainer);
		final DNodeContainer classNode = (DNodeContainer) ((View) firstView).getElement();
		// only one semantic element must be associated to the classNodeContainer
		Assert.assertEquals(1, classNode.getSemanticElements().size());
		Assert.assertEquals(this.semanticOwner, classNode.getSemanticElements().get(0));

		final DDiagramElementContainer subNodeContainer = getDNodeContainer(classNode, compartmentMappingType);
		Assert.assertNotNull(NLS.bind("We didn't find the compartment type {0}", compartmentMappingType), subNodeContainer); //$NON-NLS-1$
		Assert.assertEquals("The compartment must be empty", 0, subNodeContainer.getElements().size()); //$NON-NLS-1$
		final DDiagram diagramRepresentation = (DDiagram) diagram.getElement();

		// create the element in the container
		boolean result = fixture.applyContainerDropDescriptionTool(diagramRepresentation, dropToolId, subNodeContainer, elementToBeDropped);
		Assert.assertTrue("The drop of element failed", result); //$NON-NLS-1$
		fixture.flushDisplayEvents();

		Assert.assertEquals("The diagram children size does not change on adding a sub node", 1, diagram.getChildren().size()); //$NON-NLS-1$
		Assert.assertEquals(1, subNodeContainer.getElements().size());

		Collection<DDiagramElement> subElements = subNodeContainer.getElements();
		Assert.assertEquals(1, subElements.size());
		DDiagramElement createdElementRepresentation = subNodeContainer.getElements().get(0);

		Assert.assertEquals("The mapping is not the expected one", expectedMappingType, createdElementRepresentation.getMapping().getName()); //$NON-NLS-1$
		Assert.assertEquals("The created element representation must have 1 associated semantic element", 1, createdElementRepresentation.getSemanticElements().size()); //$NON-NLS-1$
		final EObject createdSemanticElement = createdElementRepresentation.getSemanticElements().get(0);
		Assert.assertEquals(2, this.semanticOwner.getOwnedElements().size());
		Assert.assertEquals(createdSemanticElement, this.semanticOwner.getOwnedElements().get(0));

		// undo
		fixture.getEditingDomain().getCommandStack().undo();
		fixture.flushDisplayEvents();
		Assert.assertEquals(0, subNodeContainer.getElements().size());

		// redo
		fixture.getEditingDomain().getCommandStack().redo();
		fixture.flushDisplayEvents();
		Assert.assertEquals(1, subNodeContainer.getElements().size());
		return (DDiagramElement) subNodeContainer.getElements().get(0);
	}

	/**
	 * 
	 * @return
	 *         the active Sirius Class Diagram
	 */
	protected final Diagram getClassDiagram() {
		final DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart); //$NON-NLS-1$
		final Diagram diagram = diagramEditpart.getDiagramView();

		Assert.assertTrue(diagram.getElement() instanceof DSemanticDiagram);

		// check diagram type
		final DSemanticDiagram ddiagram = (DSemanticDiagram) diagram.getElement();
		DAnnotation dAnnotation = ddiagram.getDAnnotation(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_SOURCE);
		String detail = dAnnotation.getDetails().get(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_KEY);
		Assert.assertEquals("org.eclipse.papyrus.sirius.uml.diagram.class", detail); // TODO : create a constant for this field when the code will be refactored

		return diagram;
	}

	/**
	 * 
	 * @param container
	 *            a DNodeContainer
	 * @param subNodeMappingType
	 *            the type of the wanted DNodeList
	 * @return
	 *         the expected {@link DNodeList} or <code>null</code>
	 */
	protected final DNodeContainer getDNodeContainer(final DNodeContainer container, final String wantedDNodeListType) {
		for (final DDiagramElement subNode : container.getOwnedDiagramElements()) {
			if (subNode instanceof DNodeContainer && subNode.getMapping().getName().equals(wantedDNodeListType)) {
				return (DNodeContainer) subNode;
			}
		}
		return null;
	}

	@After
	public void tearDown() {
		this.semanticOwner = null;
		this.root = null;
	}
}

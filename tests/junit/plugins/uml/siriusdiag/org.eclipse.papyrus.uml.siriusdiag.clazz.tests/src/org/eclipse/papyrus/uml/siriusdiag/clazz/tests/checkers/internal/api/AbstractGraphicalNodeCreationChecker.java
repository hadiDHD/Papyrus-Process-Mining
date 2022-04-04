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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.junit.Assert;

/**
 * Abstract GraphicalNodeChecker for creation tests.
 */
public abstract class AbstractGraphicalNodeCreationChecker implements IGraphicalNodeCreationChecker {
	/**
	 * the GMF Diagram
	 */
	protected final Diagram diagram;

	/**
	 * THE Sirius Diagram representation
	 */
	protected final DDiagram semanticDiagram;

	/**
	 * the initial number of elements owned by the Sirius Diagram
	 */
	protected final int nbSiriusDiagramOwnedChildren;

	/**
	 * The initial total number of elements in the Sirius Diagram
	 */
	protected final int nbSiriusDiagramTotalElement;

	/**
	 * The graphical parent in which we will create a Node
	 */
	protected final EObject graphicalParent;

	/**
	 * the initial number of children inside the graphical parent
	 */
	protected final int nbGraphicalContainerChildren;


	/**
	 * 
	 * Constructor.
	 *
	 * @param diagram
	 *            the gmf diagram
	 * @param graphicalParent
	 *            the graphical parent
	 */
	public AbstractGraphicalNodeCreationChecker(final Diagram diagram, final EObject graphicalParent) {
		this.diagram = diagram;
		this.semanticDiagram = (DDiagram) this.diagram.getElement();

		this.nbSiriusDiagramOwnedChildren = this.semanticDiagram.getOwnedDiagramElements().size();
		this.nbSiriusDiagramTotalElement = this.semanticDiagram.getDiagramElements().size();

		this.graphicalParent = graphicalParent;
		this.nbGraphicalContainerChildren = getGraphicalOwnerChildrenSize();

		Assert.assertNotEquals(-1, this.nbGraphicalContainerChildren);
	}

	/**
	 * 
	 * @return
	 *         the number of owned children in the graphical parent
	 */
	protected int getGraphicalOwnerChildrenSize() {
		return GraphicalOwnerUtils.getGraphicalOwnerChildrenSize(this.graphicalParent);
	}

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.IGraphicalNodeChecker#validateNode(org.eclipse.sirius.viewpoint.DRepresentationElement)
	 *
	 * @param createdElementRepresentation
	 */

	@Override
	public void validateNode(DRepresentationElement createdElementRepresentation) {
		// 1. check the type of
		checkCreatedElementInstanceOf(createdElementRepresentation);

		// 2. check the mapping type of the node
		checkCreatedElementMapping(createdElementRepresentation);

		// 3. check the number of created element
		checkNumberOfCreatedElement(createdElementRepresentation);
	}


	/**
	 * check the instance of on the created element
	 * 
	 * @param createdElementRepresentation
	 *            the created {@link DRepresentationElement}
	 */
	protected abstract void checkCreatedElementInstanceOf(final DRepresentationElement createdElementRepresentation);

	/**
	 * Check the mapping of the created element
	 * 
	 * @param createdElementRepresentation
	 *            the created {@link DRepresentationElement}
	 */
	protected void checkCreatedElementMapping(final DRepresentationElement createdElementRepresentation) {
		Assert.assertEquals("The mapping is not the expected one.", getNodeMappingType(), createdElementRepresentation.getMapping().getName()); //$NON-NLS-1$
	}

	/**
	 * Check the number of created element. In this implementation we always consider that 1 node has been created, but this node could contains sub-elements
	 * 
	 * @param createdElementRepresentation
	 *            the created {@link DRepresentationElement}
	 */
	protected void checkNumberOfCreatedElement(final DRepresentationElement createdElementRepresentation) {
		// check the parent of the created element is the expected one.
		Assert.assertEquals("The parent of the created graphical element is not the expected one.", this.graphicalParent, createdElementRepresentation.eContainer()); //$NON-NLS-1$

		// check we create only one node
		Assert.assertEquals("The parent node must contain only one additional element.", this.nbGraphicalContainerChildren + 1, getGraphicalOwnerChildrenSize()); //$NON-NLS-1$

		// check the total number of created element in the diagram
		List<?> semanticChildren = this.semanticDiagram.getDiagramElements();
		Assert.assertEquals(this.nbSiriusDiagramTotalElement + getNumberOfExpectedCreatedElement(), semanticChildren.size());
	}

	/**
	 * 
	 * @return
	 *         the total number of elements composing the node we are checking!
	 */
	protected int getNumberOfExpectedCreatedElement() {
		return 1;// just one for 1 node!
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.IGraphicalNodeChecker#validateAfterUndo()
	 *
	 */
	public void validateAfterUndo() {
		Assert.assertEquals("The diagram must contains the same number of elements as initially", this.nbSiriusDiagramTotalElement, this.semanticDiagram.getDiagramElements().size()); //$NON-NLS-1$
		Assert.assertEquals("The graphical owner must contains the same number of elements as initially", this.nbGraphicalContainerChildren, getGraphicalOwnerChildrenSize()); //$NON-NLS-1$
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.IGraphicalNodeChecker#validateAfterRedo()
	 *
	 */
	public void validateAfterRedo() {
		Assert.assertEquals(this.nbSiriusDiagramTotalElement + getNumberOfExpectedCreatedElement(), this.semanticDiagram.getDiagramElements().size());
		// check we create only one node
		Assert.assertEquals("The parent node must contain only one additional element.", this.nbGraphicalContainerChildren + 1, getGraphicalOwnerChildrenSize()); //$NON-NLS-1$

	}

	/**
	 * 
	 * @return
	 *         the expected mapping type for the created node
	 */
	protected abstract String getNodeMappingType();
}
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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.subNodes;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.CreationToolsIds;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.MappingTypes;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_OperationLabelNodeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_PropertyLabelNodeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.OperationSemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic.PropertySemanticCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.IGraphicalNodeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.ISemanticNodeCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.SemanticAndGraphicalCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.AbstractCreateNodeTests;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the creation of subnode for PrimitiveType Node
 */
@PluginResource("resources/creation/subNodes/primitivetypeSubNodes/primitivetypeSubNodes.di")
public class PrimitiveTypeSubNodes_CreationTest extends AbstractCreateNodeTests{

	private static final String SEMANTIC_ONWER_NAME = "PrimitiveType1"; //$NON-NLS-1$

	private static final String DIAGRAM_NAME = "PrimitiveTypeSubNodesDiagram"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.subNodes.AbstractSubNodeListElementCreationTests#getSemanticOwner()
	 *
	 * @return
	 */
	@Override
	protected org.eclipse.uml2.uml.PrimitiveType getSemanticOwner() {
		return (org.eclipse.uml2.uml.PrimitiveType) this.root.getMember(SEMANTIC_ONWER_NAME);
	}
	
	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.topNodes.AbstractCreateNodeTests#getTopGraphicalContainer()
	 *
	 * @return
	 */
	@Override
	protected EObject getTopGraphicalContainer() {
		final DDiagram ddiagram = getDDiagram();
		Assert.assertEquals(1, ddiagram.getOwnedDiagramElements().size());
		final DDiagramElement element = ddiagram.getOwnedDiagramElements().get(0);
		Assert.assertEquals(getSemanticOwner(), element.getSemanticElements().get(0));
		Assert.assertEquals(MappingTypes.PRIMITIVETYPE_NODE_TYPE, element.getMapping().getName());
		return element;
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPropertyLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.PRIMITIVETYPE_NODE_ATTRIBUTES_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new PropertySemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_PropertyLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__PROPERTY__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createOperationLabelNodeTest() {
		final EObject graphicalContainer = getSubNodeOfGraphicalContainer(MappingTypes.PRIMITIVETYPE_NODE_OPERATIONS_COMPARTMENT_TYPE);
		final ISemanticNodeCreationChecker semanticChecker = new OperationSemanticCreationChecker(getSemanticOwner());
		final IGraphicalNodeCreationChecker graphicalChecker = new CD_OperationLabelNodeCreationChecker(getDiagram(), graphicalContainer);
		createNode(CreationToolsIds.CREATE__OPERATION__TOOL, new SemanticAndGraphicalCreationChecker(semanticChecker, graphicalChecker), graphicalContainer);
	}
	
}
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
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.CreationToolsIds;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.MappingTypes;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the creation of subnode for PrimitiveType Node
 */
@PluginResource("resources/creation/subNodes/primitivetypeSubNodes/primitivetypeSubNodes.di")
public class PrimitiveTypeSubNodes_CreationTest extends AbstractSubNodeListElementCreationTests<org.eclipse.uml2.uml.PrimitiveType>{

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
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPropertyLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.PRIMITIVETYPE_NODE_ATTRIBUTES_COMPARTMENT_TYPE, CreationToolsIds.CREATE_PROPERTY_TOOL, MappingTypes.PROPERTY_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be an Property instead of a {0}.", semantic.eClass().getName()),semantic instanceof Property); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getOwnedAttributes().contains(semantic)); //$NON-NLS-1$
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createOperationLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.PRIMITIVETYPE_NODE_OPERATIONS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_OPERATION_TOOL, MappingTypes.OPERATION_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be an Operation instead of a {0}.", semantic.eClass().getName()),semantic instanceof Operation); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getOwnedOperations().contains(semantic)); //$NON-NLS-1$
	}

}
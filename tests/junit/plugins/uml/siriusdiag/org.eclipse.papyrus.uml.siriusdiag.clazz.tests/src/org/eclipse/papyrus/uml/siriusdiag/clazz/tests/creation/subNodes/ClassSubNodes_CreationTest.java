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
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the creation of subnode for Class Node
 */
@PluginResource("resources/creation/subNodes/classSubNodes/classSubNodes.di") // the resource to import for the test in the workspace
public class ClassSubNodes_CreationTest extends AbstractSubNodeListElementCreationTests<org.eclipse.uml2.uml.Class>{

	private static final String SEMANTIC_ONWER_NAME = "Class1"; //$NON-NLS-1$

	private static final String DIAGRAM_NAME = "ClassSubNodesDiagram"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.creation.subNodes.AbstractSubNodeListElementCreationTests#getSemanticOwner()
	 *
	 * @return
	 */
	@Override
	protected org.eclipse.uml2.uml.Class getSemanticOwner() {
		return (org.eclipse.uml2.uml.Class) this.root.getMember(SEMANTIC_ONWER_NAME);
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPropertyLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.CLASS_NODE_ATTRIBUTES_COMPARTMENT_TYPE, CreationToolsIds.CREATE_PROPERTY_TOOL, MappingTypes.CLASS_NODE_OWNED_PROPERTY_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be an Property instead of a {0}.", semantic.eClass().getName()),semantic instanceof Property); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getOwnedAttributes().contains(semantic)); //$NON-NLS-1$
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createOperationLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.CLASS_NODE_OPERATIONS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_OPERATION_TOOL, MappingTypes.CLASS_NODE_OWNED_OPERATION_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be an Operation instead of a {0}.", semantic.eClass().getName()),semantic instanceof Operation); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getOwnedOperations().contains(semantic)); //$NON-NLS-1$
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createDataTypeLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_DATATYPE_TOOL, MappingTypes.DATATYPE_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be a DataType instead of a {0}.", semantic.eClass().getName()),semantic instanceof DataType); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getNestedClassifiers().contains(semantic)); //$NON-NLS-1$
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createEnumerationLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_ENUMERATION_TOOL, MappingTypes.ENUMERATION_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be an Enumeration instead of a {0}.", semantic.eClass().getName()),semantic instanceof Enumeration); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getNestedClassifiers().contains(semantic)); //$NON-NLS-1$
	}
	
	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void createPrimitiveTypeLabelNodeTest() {
		final DDiagramElement createdElement = createSubNodeInDNodeContainer(MappingTypes.CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE, CreationToolsIds.CREATE_PRIMITIVETYPE_TOOL, MappingTypes.PRIMITIVETYPE_LABEL_NODE_TYPE);
		final EObject semantic = createdElement.getSemanticElements().get(0);
		Assert.assertTrue(NLS.bind("The created element must be an PrimitiveType instead of a {0}.", semantic.eClass().getName()),semantic instanceof PrimitiveType); //$NON-NLS-1$
		Assert.assertTrue("The created element is not owned by the expected feature", this.semanticOwner.getNestedClassifiers().contains(semantic)); //$NON-NLS-1$
	}

}


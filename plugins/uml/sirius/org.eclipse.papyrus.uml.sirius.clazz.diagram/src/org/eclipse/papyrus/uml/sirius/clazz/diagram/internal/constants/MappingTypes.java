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
package org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants;

/**
 * This class provides mapping types for Sirius Class Diagram odesign
 */
public class MappingTypes {

	private MappingTypes() {
		// to prevent instantiation
	}

	public static final String CLASS_NODE_TYPE = "CD_Class"; //$NON-NLS-1$

	public static final String CLASS_NODE_ATTRIBUTES_COMPARTMENT_TYPE = "CD_ClassAttributesCompartment"; //$NON-NLS-1$

	public static final String CLASS_NODE_OPERATIONS_COMPARTMENT_TYPE = "CD_ClassOperationsCompartment"; //$NON-NLS-1$

	public static final String CLASS_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE = "CD_ClassNestedClassifiersCompartment"; //$NON-NLS-1$

	public static final String DATATYPE_LABEL_NODE_TYPE = "CD_DataTypeLabelNode"; //$NON-NLS-1$

	public static final String DATATYPE_NODE_TYPE = "CD_DataType"; //$NON-NLS-1$

	public static final String DATATYPE_NODE_ATTRIBUTES_COMPARTMENT_TYPE = "CD_DataTypeAttributesCompartment"; //$NON-NLS-1$

	public static final String DATATYPE_NODE_OPERATIONS_COMPARTMENT_TYPE = "CD_DataTypeOperationsCompartment"; //$NON-NLS-1$

	public static final String ENUMERATION_LABEL_NODE_TYPE = "CD_EnumerationLabelNode"; //$NON-NLS-1$

	public static final String ENUMERATION_NODE_TYPE = "CD_Enumeration"; //$NON-NLS-1$

	// TODO remove me after created a common type for operation in class, in interface, ...
	public static final String CLASS_NODE_OWNED_OPERATION_TYPE = "CD_ClassOperation"; //$NON-NLS-1$

	// TODO remove me after created a common type for property in class, in interface, ...
	public static final String CLASS_NODE_OWNED_PROPERTY_TYPE = "CD_ClassProperty"; //$NON-NLS-1$

	public static final String INTERFACE_NODE_OWNED_PROPERTY_TYPE = "CD_InterfaceProperty"; //$NON-NLS-1$

	public static final String INTERFACE_NODE_TYPE = "CD_Interface"; //$NON-NLS-1$

	public static final String INTERFACE_NODE_ATTRIBUTES_COMPARTMENT_TYPE = "CD_InterfaceAttributesCompartment"; //$NON-NLS-1$

	public static final String INTERFACE_NODE_OPERATIONS_COMPARTMENT_TYPE = "CD_InterfaceOperationsCompartment"; //$NON-NLS-1$

	public static final String INTERFACE_NODE_NESTED_CLASSIFIERS_COMPARTMENT_TYPE = "CD_InterfaceNestedClassifiersCompartment"; //$NON-NLS-1$

	public static final String OPERATION_LABEL_NODE_TYPE = "CD_OperationLabelNode"; //$NON-NLS-1$

	public static final String PRIMITIVETYPE_NODE_TYPE = "CD_PrimitiveType"; //$NON-NLS-1$

	public static final String PRIMITIVETYPE_NODE_ATTRIBUTES_COMPARTMENT_TYPE = "CD_PrimitiveTypeAttributesCompartment"; //$NON-NLS-1$

	public static final String PRIMITIVETYPE_NODE_OPERATIONS_COMPARTMENT_TYPE = "CD_PrimitiveTypeOperationsCompartment"; //$NON-NLS-1$

	public static final String PRIMITIVETYPE_LABEL_NODE_TYPE = "CD_PrimitiveTypeLabelNode"; //$NON-NLS-1$

	public static final String PROPERTY_LABEL_NODE_TYPE = "CD_PropertyLabelNode"; //$NON-NLS-1$
	
	public static final String SIGNAL_NODE_TYPE = "CD_Signal"; //$NON-NLS-1$
}

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
 * This class provides creation tools ids for Sirius Class Diagram odesign
 */
public class CreationToolsIds {

	private CreationToolsIds() {
		// to prevent instantiation
	}

	/*----------------------------------------------The Node creation tools IDs----------------------------------------------*/

	public static final String CREATE__CONNECTABLE_ELEMENT_TEMPLATE_PARAMETER__TOOL = "CreateConnectableElementTemplateParameterTool";//$NON-NLS-1$

	public static final String CREATE__CLASS__TOOL = "CreateClassTool"; //$NON-NLS-1$

	public static final String CREATE__CLASSIFIER_TEMPLATE_PARAMETER__TOOL = "CreateClassifierTemplateParameterTool"; //$NON-NLS-1$

	public static final String CREATE__COMMENT__TOOL = "CreateCommentTool"; //$NON-NLS-1$
	
	public static final String CREATE__COMPONENT__TOOL = "CreateComponentTool"; //$NON-NLS-1$

	public static final String CREATE__CONSTRAINT__TOOL = "CreateConstraintTool"; //$NON-NLS-1$

	public static final String CREATE__DATATYPE__TOOL = "CreateDataTypeTool"; //$NON-NLS-1$

	public static final String CREATE__DURATION_OBSERVATION__TOOL = "CreateDurationObservationTool"; //$NON-NLS-1$

	public static final String CREATE__ENUMERATION__TOOL = "CreateEnumerationTool"; //$NON-NLS-1$

	public static final String CREATE__ENUMERATION_LITERAL__TOOL = "CreateEnumerationLiteralTool"; //$NON-NLS-1$

	public static final String CREATE__INFORMATION_ITEM__TOOL = "CreateInformationItemTool"; //$NON-NLS-1$

	/**
	 * ID of the creation tool for Instance Specification Node (we add Node to avoid mistake with the creation tool for InstanceSpecification Link)
	 */
	public static final String CREATE__INSTANCE_SPECIFICATION__NODE_TOOL = "CreateInstanceSpecificationNodeTool"; //$NON-NLS-1$

	public static final String CREATE__INTERFACE__TOOL = "CreateInterfaceTool";//$NON-NLS-1$

	public static final String CREATE__MODEL__TOOL = "CreateModelTool"; //$NON-NLS-1$

	public static final String CREATE__OPERATION__TOOL = "CreateOperationTool"; //$NON-NLS-1$

	public static final String CREATE__PACKAGE__TOOL = "CreatePackageTool"; //$NON-NLS-1$

	public static final String CREATE__PRIMITIVETYPE__TOOL = "CreatePrimitiveTypeTool"; //$NON-NLS-1$

	public static final String CREATE__PROPERTY__TOOL = "CreatePropertyTool"; //$NON-NLS-1$

	public static final String CREATE__RECEPTION__TOOL = "CreateReceptionTool"; //$NON-NLS-1$

	public static final String CREATE__REDEFINABLE_TEMPLATE_SIGNATURE__TOOL = "CreateRedefinableTemplateSignatureTool"; //$NON-NLS-1$

	public static final String CREATE__SIGNAL__TOOL = "CreateSignalTool"; //$NON-NLS-1$

	public static final String CREATE__SLOT__TOOL = "CreateSlotTool"; //$NON-NLS-1$

	public static final String CREATE__TEMPLATE_PARAMETER__TOOL = "CreateTemplateParameterTool"; //$NON-NLS-1$

	public static final String CREATE__TEMPLATE_SIGNATURE__TOOL = "CreateTemplateSignatureTool"; //$NON-NLS-1$

	public static final String CREATE__TIME_OBSERVATION__TOOL = "CreateTimeObservationTool"; //$NON-NLS-1$

	/*----------------------------------------------The Edge creation tools IDs----------------------------------------------*/

	public static final String CREATE__ABSTRACTION__TOOL = "CreateAbstractionTool"; //$NON-NLS-1$
	
	public static final String CREATE__ASSOCIATION_DIRECTED__TOOL = "CreateAssociation_Directed_Tool"; //$NON-NLS-1$
	
	public static final String CREATE__ASSOCIATION__TOOL = "CreateAssociationTool"; //$NON-NLS-1$
	
	public static final String CREATE__COMPOSITE_ASSOCIATION_DIRECTED__TOOL = "CreateCompositeAssociation_Directed_Tool"; //$NON-NLS-1$
	
	public static final String CREATE__SHARE_ASSOCIATION_DIRECTED__TOOL = "CreateSharedAssociation_Directed_Tool"; //$NON-NLS-1$
	
	public static final String CREATE__ASSOCIATION_CLASS__TOOL = "CreateAssociationClassTool"; //$NON-NLS-1$
	
	public static final String CREATE__CONTAINMENT_LINK__TOOL = "CreateCreateContainmentLinkTool"; //$NON-NLS-1$
	
	public static final String CREATE__CONTEXT_LINK__TOOL__TOOL = "CreateContextLinkTool"; //$NON-NLS-1$
	
	public static final String CREATE__DEPENDENCY__TOOL = "CreateDependencyTool"; //$NON-NLS-1$
	
	public static final String CREATE__ELEMENT_IMPORT__TOOL = "CreateElementImportTool"; //$NON-NLS-1$
	
	public static final String CREATE__GENERALISATION__TOOL = "CreateGeneralizationTool"; //$NON-NLS-1$
	
	public static final String CREATE__GENERALISATION_SET__TOOL = "CreateGeneralizationSetTool"; //$NON-NLS-1$
	
	public static final String CREATE__INFORMATION_FLOW__TOOL = "CreateInformationFlowTool"; //$NON-NLS-1$
	/**
	 * ID of the creation tool for Instance Specification Link (we add Link to avoid mistake with the creation tool for InstanceSpecification Node)
	 */
	public static final String CREATE__INSTANCE_SPECIFICATION__LINK_TOOL = "CreateInstanceSpecificationLinkTool"; //$NON-NLS-1$
	
	public static final String CREATE__INTERFACE_REALIZATION__TOOL = "CreateInterfaceRealizationTool"; //$NON-NLS-1$
	
	public static final String CREATE__LINK__TOOL = "CreateLinkTool"; //$NON-NLS-1$
	
	public static final String CREATE__PACKAGE_IMPORT__TOOL = "CreatePackageImportTool"; //$NON-NLS-1$
	
	public static final String CREATE__PACKAGE_MERGE__TOOL = "CreatePackageMergeTool"; //$NON-NLS-1$
	
	public static final String CREATE__PROFILE_APPLICATION__TOOL = "CreateProfileApplicationTool"; //$NON-NLS-1$
	
	public static final String CREATE__REALIZATION__TOOL = "CreateRealizationTool"; //$NON-NLS-1$
	
	public static final String CREATE__SUBSTITUTION__TOOL = "CreateSubstitutionTool"; //$NON-NLS-1$
	
	public static final String CREATE__TEMPLATE_BINDING__TOOL = "CreateTemplateBindingTool"; //$NON-NLS-1$
	
	public static final String CREATE__USAGE__TOOL = "CreateUsageTool"; //$NON-NLS-1$

}


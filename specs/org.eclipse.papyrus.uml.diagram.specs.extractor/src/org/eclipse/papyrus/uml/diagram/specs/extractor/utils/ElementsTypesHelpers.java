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
package org.eclipse.papyrus.uml.diagram.specs.extractor.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.papyrus.uml.service.types.element.UMLDIElementTypes;

/**
 * This class provides all Graphical Elements Types from {@link org.eclipse.papyrus.uml.service.types.element.UMLDIElementTypes} (extracted in june 2022)
 */
public class ElementsTypesHelpers {

	private static final List<IElementType> allElementTypes = new ArrayList<>();

	static {
		allElementTypes.add(UMLDIElementTypes.ABSTRACTION_EDGE);

		allElementTypes.add(UMLDIElementTypes.ABSTRACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ABSTRACTION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ACCEPT_CALL_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACCEPT_EVENT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_EXECUTION_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_EXECUTION_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ACCEPT_CALL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ACCEPT_EVENT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_INSERT_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ADD_VARIABLE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ADD_VARIABLE_VALUE_ACTION_INSERT_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_ADD_VARIABLE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_BROADCAST_SIGNAL_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_BROADCAST_SIGNAL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CALL_BEHAVIOR_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CALL_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CALL_OPERATION_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CALL_OPERATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CALL_OPERATION_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CLEAR_ASSOCIATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CLEAR_ASSOCIATION_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CLEAR_VARIABLE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CONDITIONAL_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CREATE_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CREATE_LINK_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_CREATE_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_DESTROY_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_DESTROY_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_DESTROY_OBJECT_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_EXPANSION_REGION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_LOOP_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_LOOP_NODE_VARIABLE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_OPAQUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_RAISE_EXCEPTION_ACTION_EXCEPTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_RAISE_EXCEPTION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_EXTENT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_LINK_OBJECT_END_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_LINK_OBJECT_END_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_SELF_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_STRUCTURAL_FEATURE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_STRUCTURAL_FEATURE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_READ_VARIABLE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_RECLASSIFY_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_RECLASSIFY_OBJECT_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REDUCE_ACTION_COLLECTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REDUCE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_REMOVE_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REMOVE_VARIABLE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REMOVE_VARIABLE_VALUE_ACTION_REMOVE_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REMOVE_VARIABLE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REPLY_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REPLY_ACTION_REPLY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_REPLY_ACTION_RETURN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SEND_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SEND_OBJECT_ACTION_REQUEST_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SEND_OBJECT_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SEND_SIGNAL_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SEND_SIGNAL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SEND_SIGNAL_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SEQUENCE_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_START_CLASSIFIER_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_START_CLASSIFIER_BEHAVIOR_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_STRUCTURED_ACTIVITY_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_TEST_IDENTITY_ACTION_FIRST_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_TEST_IDENTITY_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_TEST_IDENTITY_ACTION_SECOND_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_UNMARSHALL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_UNMARSHALL_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_INPUT_PIN_VALUE_SPECIFICATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTION_LOCAL_POSTCONDITION_EDGE);

		allElementTypes.add(UMLDIElementTypes.ACTION_LOCAL_PRECONDITION_EDGE);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_FINAL_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_PARAMETER_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_PARTITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_PARTITION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_OWNED_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTIVITY_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ACTOR_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTOR_CLASSIFIER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTOR_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTOR_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTOR_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ACTOR_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ACTOR_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.ACTOR_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ADD_STRUCTURAL_FEATURE_VALUE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ADD_VARIABLE_VALUE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ANY_RECEIVE_EVENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_DEPLOYED_ARTIFACT_LABEL);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_NESTED_ARTIFACT_LABEL);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_SHAPE_ACN);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.ARTIFACT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_CLASS_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_CLASS_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_CLASS_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_CLASS_TETHER_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_BRANCH_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_NON_DIRECTED_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_DIRECTED_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_COMPOSITE_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_COMPOSITE_DIRECTED_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_SHARED_EDGE);

		allElementTypes.add(UMLDIElementTypes.ASSOCIATION_SHARED_DIRECTED_EDGE);

		allElementTypes.add(UMLDIElementTypes.BEHAVIOR_EXECUTION_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.BEHAVIOR_EXECUTION_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.BEHAVIOR_DO_ACTIVITY_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.BEHAVIOR_ENTRY_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.BEHAVIOR_EXIT_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.BEHAVIOR_INTERNAL_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.BROADCAST_SIGNAL_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CALL_BEHAVIOR_ACTION_BASE_TYPE);

		allElementTypes.add(UMLDIElementTypes.CALL_BEHAVIOR_ACTION_INTERACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CALL_BEHAVIOR_ACTION_INTERACTION_USE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CALL_BEHAVIOR_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CALL_EVENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CALL_OPERATION_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CENTRAL_BUFFER_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CHANGE_EVENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CLASS_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.CLASS_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.CLASS_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.CLASS_METACLASS_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CLASS_METACLASS_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.CLASS_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CLASS_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CLASSIFIER_TEMPLATE_PARAMETER_TEMPLATE_PARAMETER_LABEL);

		allElementTypes.add(UMLDIElementTypes.CLASSIFIER_SUBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CLEAR_ASSOCIATION_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CLEAR_STRUCTURAL_FEATURE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CLEAR_VARIABLE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_USE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_USE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COLLABORATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.COMBINED_FRAGMENT_CO_REGION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COMBINED_FRAGMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COMBINED_FRAGMENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.COMMENT_ANNOTATED_ELEMENT_EDGE);

		allElementTypes.add(UMLDIElementTypes.COMMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COMMENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.COMMUNICATION_PATH_EDGE);

		allElementTypes.add(UMLDIElementTypes.COMMUNICATION_PATH_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COMMUNICATION_PATH_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_REALIZATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_REALIZATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_REALIZATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_PACKAGED_ELEMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_PACKAGED_ELEMENT_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_PACKAGED_ELEMENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.COMPONENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CONDITIONAL_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONNECTABLE_ELEMENT_TEMPLATE_PARAMETER_TEMPLATE_PARAMETER_LABEL);

		allElementTypes.add(UMLDIElementTypes.CONNECTABLE_ELEMENT_COLLABORATION_ROLE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONNECTION_POINT_REFERENCE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONNECTION_POINT_REFERENCE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CONNECTOR_EDGE);

		allElementTypes.add(UMLDIElementTypes.CONSIDER_IGNORE_FRAGMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONSIDER_IGNORE_FRAGMENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_CONSTRAINED_ELEMENT_EDGE);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_CONTEXT_EDGE);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_LOCAL_POSTCONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_LOCAL_PRECONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_PACKAGED_ELEMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_PACKAGED_ELEMENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_POSTCONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_PRECONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.CONSTRAINT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CONTINUATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CONTINUATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.CONTROL_FLOW_EDGE);

		allElementTypes.add(UMLDIElementTypes.CREATE_LINK_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CREATE_LINK_OBJECT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.CREATE_OBJECT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DATA_STORE_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DATA_TYPE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DATA_TYPE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DATA_TYPE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DATA_TYPE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DATA_TYPE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DATA_TYPE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DECISION_NODE_DECISION_INPUT_LABEL);

		allElementTypes.add(UMLDIElementTypes.DECISION_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DEPENDENCY_BRANCH_EDGE);

		allElementTypes.add(UMLDIElementTypes.DEPENDENCY_EDGE);

		allElementTypes.add(UMLDIElementTypes.DEPENDENCY_ROLE_BINDING_EDGE);

		allElementTypes.add(UMLDIElementTypes.DEPENDENCY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DEPENDENCY_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_DEPLOYED_ARTIFACT_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_NESTED_ARTIFACT_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_SHAPE_ACN);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_EDGE);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DEPLOYMENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DESTROY_LINK_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DESTROY_OBJECT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DESTRUCTION_OCCURRENCE_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DESTRUCTION_OCCURRENCE_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DEVICE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEVICE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEVICE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEVICE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEVICE_NESTED_NODE_LABEL);

		allElementTypes.add(UMLDIElementTypes.DEVICE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DEVICE_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.DEVICE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DIAGRAM_SHORTCUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DURATION_CONSTRAINT_LOCAL_POSTCONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DURATION_CONSTRAINT_LOCAL_PRECONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DURATION_CONSTRAINT_POSTCONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.DURATION_CONSTRAINT_PRECONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.DURATION_CONSTRAINT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DURATION_CONSTRAINT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DURATION_INTERVAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DURATION_OBSERVATION_EVENT_EDGE);

		allElementTypes.add(UMLDIElementTypes.DURATION_OBSERVATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DURATION_OBSERVATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.DURATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.DURATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ELEMENT_IMPORT_EDGE);

		allElementTypes.add(UMLDIElementTypes.ELEMENT_CONTAINMENT_EDGE);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_LITERAL_DEPLOYED_ARTIFACT_LABEL);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_LITERAL_LITERAL_LABEL);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_LITERAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_LITERAL_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.ENUMERATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.EXCEPTION_HANDLER_EDGE);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_NESTED_NODE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_ENVIRONMENT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_OCCURRENCE_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXECUTION_OCCURRENCE_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.EXPANSION_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXPANSION_NODE_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXPANSION_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXPANSION_REGION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXPRESSION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXTEND_EDGE);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_CLASS_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_COMPONENT_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_DATA_TYPE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_EDGE);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_INTERFACE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_PRIMITIVE_TYPE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_END_SIGNAL_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_POINT_CLASSIFIER_EXTENSION_POINT_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_POINT_EXTENSION_POINT_LABEL);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_EDGE);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.EXTENSION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.FINAL_STATE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.FINAL_STATE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.FLOW_FINAL_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.FORK_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.FUNCTION_BEHAVIOR_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.FUNCTION_BEHAVIOR_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.FUNCTION_BEHAVIOR_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.FUNCTION_BEHAVIOR_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.FUNCTION_BEHAVIOR_OWNED_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.FUNCTION_BEHAVIOR_SHAPE);

		allElementTypes.add(UMLDIElementTypes.FUNCTION_BEHAVIOR_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.GATE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.GATE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.GENERAL_ORDERING_EDGE);

		allElementTypes.add(UMLDIElementTypes.GENERAL_ORDERING_SHAPE);

		allElementTypes.add(UMLDIElementTypes.GENERAL_ORDERING_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.GENERALIZATION_SET_EDGE);

		allElementTypes.add(UMLDIElementTypes.GENERALIZATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.INCLUDE_EDGE);

		allElementTypes.add(UMLDIElementTypes.INFORMATION_FLOW_EDGE);

		allElementTypes.add(UMLDIElementTypes.INFORMATION_ITEM_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INFORMATION_ITEM_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INFORMATION_ITEM_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INFORMATION_ITEM_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INFORMATION_ITEM_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INFORMATION_ITEM_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INITIAL_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ACCEPT_CALL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ACCEPT_EVENT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_INSERT_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ADD_VARIABLE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ADD_VARIABLE_VALUE_ACTION_INSERT_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_ADD_VARIABLE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_BROADCAST_SIGNAL_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_BROADCAST_SIGNAL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CALL_BEHAVIOR_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CALL_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CALL_OPERATION_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CALL_OPERATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CALL_OPERATION_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CLEAR_ASSOCIATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CLEAR_ASSOCIATION_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CLEAR_VARIABLE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CONDITIONAL_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CREATE_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CREATE_LINK_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_CREATE_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_DESTROY_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_DESTROY_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_DESTROY_OBJECT_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_EXPANSION_REGION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_LOOP_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_LOOP_NODE_VARIABLE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_OPAQUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_RAISE_EXCEPTION_ACTION_EXCEPTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_RAISE_EXCEPTION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_EXTENT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_LINK_OBJECT_END_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_LINK_OBJECT_END_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_SELF_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_STRUCTURAL_FEATURE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_STRUCTURAL_FEATURE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_READ_VARIABLE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_RECLASSIFY_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_RECLASSIFY_OBJECT_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REDUCE_ACTION_COLLECTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REDUCE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_REMOVE_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REMOVE_VARIABLE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REMOVE_VARIABLE_VALUE_ACTION_REMOVE_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REMOVE_VARIABLE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REPLY_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REPLY_ACTION_REPLY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_REPLY_ACTION_RETURN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SEND_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SEND_OBJECT_ACTION_REQUEST_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SEND_OBJECT_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SEND_SIGNAL_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SEND_SIGNAL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SEND_SIGNAL_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SEQUENCE_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_START_CLASSIFIER_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_START_CLASSIFIER_BEHAVIOR_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_STRUCTURED_ACTIVITY_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_TEST_IDENTITY_ACTION_FIRST_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_TEST_IDENTITY_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_TEST_IDENTITY_ACTION_SECOND_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_UNMARSHALL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_UNMARSHALL_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INPUT_PIN_VALUE_SPECIFICATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INSTANCE_SPECIFICATION_DEPLOYED_ARTIFACT_LABEL);

		allElementTypes.add(UMLDIElementTypes.INSTANCE_SPECIFICATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.INSTANCE_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INSTANCE_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INSTANCE_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_CONSTRAINT_POSTCONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_CONSTRAINT_PRECONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_CONSTRAINT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_CONSTRAINT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_OPERAND_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_OPERAND_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_USE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_USE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_OWNED_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERACTION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_REALIZATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_REALIZATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_REALIZATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_CLASSIFIER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_CLASSIFIER_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERFACE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERRUPTIBLE_ACTIVITY_REGION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERRUPTIBLE_ACTIVITY_REGION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERVAL_CONSTRAINT_LOCAL_POSTCONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERVAL_CONSTRAINT_LOCAL_PRECONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERVAL_CONSTRAINT_POSTCONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERVAL_CONSTRAINT_PRECONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.INTERVAL_CONSTRAINT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.INTERVAL_CONSTRAINT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.INTERVAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.JOIN_NODE_JOIN_SPEC_LABEL);

		allElementTypes.add(UMLDIElementTypes.JOIN_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LIFELINE_COMPACT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LIFELINE_FULL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LIFELINE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LIFELINE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.LINK_DESCRIPTOR_EDGE);

		allElementTypes.add(UMLDIElementTypes.LINK_INTERFACE_PORT_EDGE);

		allElementTypes.add(UMLDIElementTypes.LITERAL_BOOLEAN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LITERAL_INTEGER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LITERAL_NULL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LITERAL_REAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LITERAL_STRING_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LITERAL_UNLIMITED_NATURAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.LOOP_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.MANIFESTATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.MANIFESTATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.MANIFESTATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.MERGE_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_OCCURRENCE_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_OCCURRENCE_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_ASYNCH_EDGE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_CREATE_EDGE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_DELETE_EDGE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_EDGE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_FOUND_EDGE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_LOST_EDGE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_REPLY_EDGE);

		allElementTypes.add(UMLDIElementTypes.MESSAGE_SYNCH_EDGE);

		allElementTypes.add(UMLDIElementTypes.MODEL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.MODEL_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.NAMED_ELEMENT_DEFAULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.NODE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.NODE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.NODE_FREE_TIME_RULER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.NODE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.NODE_LINEAR_TIME_RULER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.NODE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.NODE_NESTED_NODE_LABEL);

		allElementTypes.add(UMLDIElementTypes.NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.NODE_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.NODE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.NODE_STATE_DEFINITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.NODE_STATE_INVARIANT_TRANSITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.NODE_TICK_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OBJECT_FLOW_EDGE);

		allElementTypes.add(UMLDIElementTypes.OCCURRENCE_SPECIFICATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OCCURRENCE_SPECIFICATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_BEHAVIOR_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_BEHAVIOR_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_BEHAVIOR_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_BEHAVIOR_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_BEHAVIOR_OWNED_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_BEHAVIOR_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_BEHAVIOR_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.OPAQUE_EXPRESSION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OPERATION_TEMPLATE_PARAMETER_TEMPLATE_PARAMETER_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPERATION_CLASS_OPERATION_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPERATION_COMPONENT_OPERATION_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPERATION_DATA_TYPE_OPERATION_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPERATION_INTERFACE_OPERATION_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPERATION_OPERATION_LABEL);

		allElementTypes.add(UMLDIElementTypes.OPERATION_PRIMITIVE_TYPE_OPERATION_LABEL);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ACCEPT_CALL_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ACCEPT_CALL_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ACCEPT_CALL_ACTION_RETURN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ACCEPT_EVENT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ACCEPT_EVENT_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_ADD_VARIABLE_VALUE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_BROADCAST_SIGNAL_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CALL_BEHAVIOR_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CALL_BEHAVIOR_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CALL_OPERATION_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CALL_OPERATION_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CLEAR_ASSOCIATION_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CLEAR_VARIABLE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CONDITIONAL_NODE_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CONDITIONAL_NODE_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CREATE_LINK_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CREATE_LINK_OBJECT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CREATE_LINK_OBJECT_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CREATE_OBJECT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_CREATE_OBJECT_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_DESTROY_LINK_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_DESTROY_OBJECT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_EXPANSION_REGION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_LOOP_NODE_BODY_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_LOOP_NODE_DECIDER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_LOOP_NODE_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_LOOP_NODE_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_LOOP_NODE_VARIABLE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_OPAQUE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_RAISE_EXCEPTION_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_EXTENT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_EXTENT_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_LINK_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_LINK_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_LINK_OBJECT_END_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_LINK_OBJECT_END_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_SELF_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_SELF_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_STRUCTURAL_FEATURE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_STRUCTURAL_FEATURE_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_VARIABLE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_READ_VARIABLE_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_RECLASSIFY_OBJECT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_REDUCE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_REDUCE_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_REMOVE_VARIABLE_VALUE_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_REPLY_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_SEND_OBJECT_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_SEND_SIGNAL_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_SEQUENCE_NODE_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_START_CLASSIFIER_BEHAVIOR_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_START_OBJECT_BEHAVIOR_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_STRUCTURED_ACTIVITY_NODE_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_TEST_IDENTITY_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_TEST_IDENTITY_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_UNMARSHALL_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_UNMARSHALL_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_VALUE_SPECIFICATION_ACTION_OUTPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.OUTPUT_PIN_VALUE_SPECIFICATION_ACTION_RESULT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_IMPORT_EDGE);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_MERGE_EDGE);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_ACTIVITY_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_CLASS_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_COMMUNICATION_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_COMPONENT_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_COMPOSITE_STRUCTURE_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_DEPLOYMENT_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_INTERACTION_OVERVIEW_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_PROFILE_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_SEQUENCE_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_STATE_MACHINE_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_TIMING_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PACKAGE_USE_CASE_DIAGRAM);

		allElementTypes.add(UMLDIElementTypes.PARAMETER_PARAMETER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PARAMETER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PART_DECOMPOSITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PART_DECOMPOSITION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PATH_EDGE);

		allElementTypes.add(UMLDIElementTypes.PORT_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PORT_BEHAVIOR_EDGE);

		allElementTypes.add(UMLDIElementTypes.PORT_BEHAVIOR_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PORT_CLASS_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PORT_COMPONENT_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PORT_DATA_TYPE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PORT_EDGE);

		allElementTypes.add(UMLDIElementTypes.PORT_INTERFACE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PORT_PRIMITIVE_TYPE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PORT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PORT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PORT_SIGNAL_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PRIMITIVE_TYPE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PRIMITIVE_TYPE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PRIMITIVE_TYPE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PRIMITIVE_TYPE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PRIMITIVE_TYPE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PRIMITIVE_TYPE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PROFILE_APPLICATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.PROFILE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PROFILE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_CLASS_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_COMPONENT_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_DATA_TYPE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_EDGE);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_INTERFACE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_PRIMITIVE_TYPE_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PROPERTY_SIGNAL_ATTRIBUTE_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_STATE_MACHINE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_STATE_MACHINE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_STATE_MACHINE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_STATE_MACHINE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_STATE_MACHINE_OWNED_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_STATE_MACHINE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_STATE_MACHINE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_TRANSITION_EDGE);

		allElementTypes.add(UMLDIElementTypes.PROTOCOL_TRANSITION_INTERNAL_TRANSITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_CHOICE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_DEEP_HISTORY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_ENTRY_POINT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_EXIT_POINT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_FORK_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_INITIAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_JOIN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_JUNCTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_SHALLOW_HISTORY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.PSEUDOSTATE_TERMINATE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.RAISE_EXCEPTION_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_EXTENT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_IS_CLASSIFIED_OBJECT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_LINK_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_LINK_OBJECT_END_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_LINK_OBJECT_END_QUALIFIER_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_SELF_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_STRUCTURAL_FEATURE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.READ_VARIABLE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REALIZATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.REALIZATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REALIZATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.RECEPTION_INTERFACE_RECEPTION_LABEL);

		allElementTypes.add(UMLDIElementTypes.RECEPTION_RECEPTION_LABEL);

		allElementTypes.add(UMLDIElementTypes.RECLASSIFY_OBJECT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REDEFINABLE_TEMPLATE_SIGNATURE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REDEFINABLE_TEMPLATE_SIGNATURE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.REDUCE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REGION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REGION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REMOVE_VARIABLE_VALUE_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REPLY_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.REPRESENTATION_EDGE);

		allElementTypes.add(UMLDIElementTypes.SEND_OBJECT_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.SEND_SIGNAL_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.SEQUENCE_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.SIGNAL_EVENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.SIGNAL_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.SIGNAL_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.SIGNAL_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.SIGNAL_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.SIGNAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.SIGNAL_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.SLOT_SLOT_LABEL);

		allElementTypes.add(UMLDIElementTypes.START_CLASSIFIER_BEHAVIOR_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.START_OBJECT_BEHAVIOR_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STATE_INVARIANT_COMPACT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STATE_INVARIANT_FULL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STATE_INVARIANT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STATE_INVARIANT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.STATE_MACHINE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STATE_MACHINE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STATE_MACHINE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STATE_MACHINE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STATE_MACHINE_OWNED_BEHAVIOR_LABEL);

		allElementTypes.add(UMLDIElementTypes.STATE_MACHINE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STATE_MACHINE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.STATE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STATE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.STEREOTYPE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STEREOTYPE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STEREOTYPE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STEREOTYPE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.STEREOTYPE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STEREOTYPE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.STEREOTYPE_STEREOTYPE_LABEL);

		allElementTypes.add(UMLDIElementTypes.STRING_EXPRESSION_PACKAGED_ELEMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STRING_EXPRESSION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.STRUCTURED_ACTIVITY_NODE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.SUBSTITUTION_EDGE);

		allElementTypes.add(UMLDIElementTypes.SUBSTITUTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.SUBSTITUTION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.TEMPLATE_BINDING_EDGE);

		allElementTypes.add(UMLDIElementTypes.TEMPLATE_PARAMETER_TEMPLATE_PARAMETER_LABEL);

		allElementTypes.add(UMLDIElementTypes.TEMPLATE_SIGNATURE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TEMPLATE_SIGNATURE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.TEST_IDENTITY_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_CONSTRAINT_LOCAL_POSTCONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_CONSTRAINT_LOCAL_PRECONDITION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_CONSTRAINT_POSTCONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.TIME_CONSTRAINT_PRECONDITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.TIME_CONSTRAINT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_CONSTRAINT_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.TIME_EVENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_EXPRESSION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_EXPRESSION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.TIME_INTERVAL_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_OBSERVATION_EVENT_EDGE);

		allElementTypes.add(UMLDIElementTypes.TIME_OBSERVATION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.TIME_OBSERVATION_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.TRANSITION_EDGE);

		allElementTypes.add(UMLDIElementTypes.TRANSITION_INTERNAL_TRANSITION_LABEL);

		allElementTypes.add(UMLDIElementTypes.TRIGGER_DEFERRABLE_TRIGGER_LABEL);

		allElementTypes.add(UMLDIElementTypes.UNMARSHALL_ACTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.USAGE_EDGE);

		allElementTypes.add(UMLDIElementTypes.USAGE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.USAGE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_CLASS_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_CLASSIFIER_SHAPE);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_COMPONENT_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_INTERFACE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_NESTED_CLASSIFIER_LABEL);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_OWNED_USE_CASE_LABEL);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_SHAPE_CCN);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_SHAPE_CN);

		allElementTypes.add(UMLDIElementTypes.USE_CASE_USE_CASE_LABEL);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ACCEPT_CALL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ACCEPT_EVENT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_INSERT_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ADD_STRUCTURAL_FEATURE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ADD_VARIABLE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ADD_VARIABLE_VALUE_ACTION_INSERT_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_ADD_VARIABLE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_BROADCAST_SIGNAL_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_BROADCAST_SIGNAL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CALL_BEHAVIOR_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CALL_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CALL_OPERATION_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CALL_OPERATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CALL_OPERATION_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CLEAR_ASSOCIATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CLEAR_ASSOCIATION_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CLEAR_STRUCTURAL_FEATURE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CLEAR_VARIABLE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CONDITIONAL_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CREATE_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CREATE_LINK_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_CREATE_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_DESTROY_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_DESTROY_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_DESTROY_OBJECT_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_EXPANSION_REGION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_LOOP_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_LOOP_NODE_VARIABLE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_OPAQUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_RAISE_EXCEPTION_ACTION_EXCEPTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_RAISE_EXCEPTION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_EXTENT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_IS_CLASSIFIED_OBJECT_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_LINK_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_LINK_OBJECT_END_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_LINK_OBJECT_END_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_LINK_OBJECT_END_QUALIFIER_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_SELF_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_STRUCTURAL_FEATURE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_STRUCTURAL_FEATURE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_READ_VARIABLE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_RECLASSIFY_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_RECLASSIFY_OBJECT_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REDUCE_ACTION_COLLECTION_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REDUCE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_REMOVE_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REMOVE_STRUCTURAL_FEATURE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REMOVE_VARIABLE_VALUE_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REMOVE_VARIABLE_VALUE_ACTION_REMOVE_AT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REMOVE_VARIABLE_VALUE_ACTION_VALUE_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REPLY_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REPLY_ACTION_REPLY_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_REPLY_ACTION_RETURN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SEND_OBJECT_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SEND_OBJECT_ACTION_REQUEST_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SEND_OBJECT_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SEND_SIGNAL_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SEND_SIGNAL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SEND_SIGNAL_ACTION_TARGET_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SEQUENCE_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_START_CLASSIFIER_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_START_CLASSIFIER_BEHAVIOR_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_START_OBJECT_BEHAVIOR_ACTION_ARGUMENT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_START_OBJECT_BEHAVIOR_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_START_OBJECT_BEHAVIOR_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_STRUCTURED_ACTIVITY_NODE_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_TEST_IDENTITY_ACTION_FIRST_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_TEST_IDENTITY_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_TEST_IDENTITY_ACTION_SECOND_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_UNMARSHALL_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_UNMARSHALL_ACTION_OBJECT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_PIN_VALUE_SPECIFICATION_ACTION_INPUT_SHAPE);

		allElementTypes.add(UMLDIElementTypes.VALUE_SPECIFICATION_ACTION_SHAPE);
	}

	/**
	 * 
	 * Constructor.
	 *
	 */
	public ElementsTypesHelpers() {
		// nothing to do
	}

	/**
	 * 
	 * @return
	 *         all elementType with an ID owning the string shape or label
	 */
	public final List<IElementType> getAllShapesAndLabel() {
		List<IElementType> shapes = new ArrayList<>();
		for (final IElementType current : allElementTypes) {
			final String id = current.getId().toLowerCase();
			if (id.contains("shape") || id.contains("label")) { //$NON-NLS-1$ //$NON-NLS-2$
				shapes.add(current);
			}
		}
		return shapes;
	}

	/**
	 * 
	 * @param collection
	 *            a collection of "metaclass" identified by their name (in reality, the label used in the palette)
	 * @return
	 */
	public final Map<String, List<IElementType>> getAllShapesFor(final Collection<String> collection) {
		final List<IElementType> allShapes = getAllShapesAndLabel();
		final Map<String, List<IElementType>> wantedShapes = new HashMap<String, List<IElementType>>();
		for (String current : collection) {
			List<IElementType> values = new ArrayList<>();
			wantedShapes.put(current, values);
			String elementAsLowerCase = current.toLowerCase();
			for (IElementType tmp : allShapes) {
				if (tmp.getId().toLowerCase().contains(elementAsLowerCase)) {
					values.add(tmp);
				}
			}
		}
		return wantedShapes;
	}
}

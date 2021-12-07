/**
 * Copyright (c) 2021 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 */
package org.eclipse.papyrus.infra.siriusdiag.representation;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * This package contains the elements allowing to integrate the DDiagram metamodel into the Papyrus ArchitectureFramework
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.papyrus.infra.siriusdiag.representation.RepresentationFactory
 * @model kind="package"
 * @generated
 */
public interface RepresentationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNAME = "representation"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/papyrus/sirius/integration/1.0.0/emf/siriusdiagram/representation"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_PREFIX = "representation"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	RepresentationPackage eINSTANCE = org.eclipse.papyrus.infra.siriusdiag.representation.impl.RepresentationPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.papyrus.infra.siriusdiag.representation.impl.SiriusDiagramPrototypeImpl <em>Sirius Diagram Prototype</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.impl.SiriusDiagramPrototypeImpl
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.impl.RepresentationPackageImpl#getSiriusDiagramPrototype()
	 * @generated
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__ID = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__NAME = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__DESCRIPTION = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__QUALIFIED_NAME = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Icon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__ICON = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__ICON;

	/**
	 * The feature id for the '<em><b>Language</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__LANGUAGE = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__LANGUAGE;

	/**
	 * The feature id for the '<em><b>Concerns</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__CONCERNS = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__CONCERNS;

	/**
	 * The feature id for the '<em><b>Grayed Icon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__GRAYED_ICON = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__GRAYED_ICON;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__PARENT = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__PARENT;

	/**
	 * The feature id for the '<em><b>Model Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__MODEL_RULES = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__MODEL_RULES;

	/**
	 * The feature id for the '<em><b>Owning Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__OWNING_RULES = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__OWNING_RULES;

	/**
	 * The feature id for the '<em><b>Implementation ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__IMPLEMENTATION_ID = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND__IMPLEMENTATION_ID;

	/**
	 * The feature id for the '<em><b>Diagram Description</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Creation Command Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Session</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE__SESSION = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Sirius Diagram Prototype</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE_FEATURE_COUNT = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Is Valid Class</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE___IS_VALID_CLASS__DIAGNOSTICCHAIN_MAP = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Sirius Diagram Prototype</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SIRIUS_DIAGRAM_PROTOTYPE_OPERATION_COUNT = org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.PAPYRUS_REPRESENTATION_KIND_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.sirius.business.api.session.Session <em>Session</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.sirius.business.api.session.Session
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.impl.RepresentationPackageImpl#getSession()
	 * @generated
	 */
	int SESSION = 1;

	/**
	 * The number of structural features of the '<em>Session</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SESSION_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Session</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int SESSION_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype <em>Sirius Diagram Prototype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Sirius Diagram Prototype</em>'.
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype
	 * @generated
	 */
	EClass getSiriusDiagramPrototype();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#getDiagramDescription <em>Diagram Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Diagram Description</em>'.
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#getDiagramDescription()
	 * @see #getSiriusDiagramPrototype()
	 * @generated
	 */
	EReference getSiriusDiagramPrototype_DiagramDescription();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#getCreationCommandClass <em>Creation Command Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Creation Command Class</em>'.
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#getCreationCommandClass()
	 * @see #getSiriusDiagramPrototype()
	 * @generated
	 */
	EAttribute getSiriusDiagramPrototype_CreationCommandClass();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#getSession <em>Session</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Session</em>'.
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#getSession()
	 * @see #getSiriusDiagramPrototype()
	 * @generated
	 */
	EReference getSiriusDiagramPrototype_Session();

	/**
	 * Returns the meta object for the '{@link org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#isValidClass(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Is Valid Class</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the '<em>Is Valid Class</em>' operation.
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype#isValidClass(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
	EOperation getSiriusDiagramPrototype__IsValidClass__DiagnosticChain_Map();

	/**
	 * Returns the meta object for class '{@link org.eclipse.sirius.business.api.session.Session <em>Session</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Session</em>'.
	 * @see org.eclipse.sirius.business.api.session.Session
	 * @model instanceClass="org.eclipse.sirius.business.api.session.Session"
	 * @generated
	 */
	EClass getSession();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RepresentationFactory getRepresentationFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.papyrus.infra.siriusdiag.representation.impl.SiriusDiagramPrototypeImpl <em>Sirius Diagram Prototype</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.papyrus.infra.siriusdiag.representation.impl.SiriusDiagramPrototypeImpl
		 * @see org.eclipse.papyrus.infra.siriusdiag.representation.impl.RepresentationPackageImpl#getSiriusDiagramPrototype()
		 * @generated
		 */
		EClass SIRIUS_DIAGRAM_PROTOTYPE = eINSTANCE.getSiriusDiagramPrototype();

		/**
		 * The meta object literal for the '<em><b>Diagram Description</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION = eINSTANCE.getSiriusDiagramPrototype_DiagramDescription();

		/**
		 * The meta object literal for the '<em><b>Creation Command Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS = eINSTANCE.getSiriusDiagramPrototype_CreationCommandClass();

		/**
		 * The meta object literal for the '<em><b>Session</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference SIRIUS_DIAGRAM_PROTOTYPE__SESSION = eINSTANCE.getSiriusDiagramPrototype_Session();

		/**
		 * The meta object literal for the '<em><b>Is Valid Class</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EOperation SIRIUS_DIAGRAM_PROTOTYPE___IS_VALID_CLASS__DIAGNOSTICCHAIN_MAP = eINSTANCE.getSiriusDiagramPrototype__IsValidClass__DiagnosticChain_Map();

		/**
		 * The meta object literal for the '{@link org.eclipse.sirius.business.api.session.Session <em>Session</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see org.eclipse.sirius.business.api.session.Session
		 * @see org.eclipse.papyrus.infra.siriusdiag.representation.impl.RepresentationPackageImpl#getSession()
		 * @generated
		 */
		EClass SESSION = eINSTANCE.getSession();

	}

} // RepresentationPackage

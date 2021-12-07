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
package org.eclipse.papyrus.infra.siriusdiag.representation.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.papyrus.infra.constraints.ConstraintsPackage;
import org.eclipse.papyrus.infra.core.architecture.ArchitecturePackage;
import org.eclipse.papyrus.infra.siriusdiag.representation.RepresentationFactory;
import org.eclipse.papyrus.infra.siriusdiag.representation.RepresentationPackage;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.representation.util.RepresentationValidator;
import org.eclipse.papyrus.infra.types.ElementTypesConfigurationsPackage;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class RepresentationPackageImpl extends EPackageImpl implements RepresentationPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass siriusDiagramPrototypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass sessionEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.papyrus.infra.siriusdiag.representation.RepresentationPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RepresentationPackageImpl() {
		super(eNS_URI, RepresentationFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>
	 * This method is used to initialize {@link RepresentationPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RepresentationPackage init() {
		if (isInited) {
			return (RepresentationPackage) EPackage.Registry.INSTANCE.getEPackage(RepresentationPackage.eNS_URI);
		}

		// Obtain or create and register package
		Object registeredRepresentationPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		RepresentationPackageImpl theRepresentationPackage = registeredRepresentationPackage instanceof RepresentationPackageImpl ? (RepresentationPackageImpl) registeredRepresentationPackage : new RepresentationPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		ArchitecturePackage.eINSTANCE.eClass();
		ConstraintsPackage.eINSTANCE.eClass();
		DiagramPackage.eINSTANCE.eClass();
		EcorePackage.eINSTANCE.eClass();
		ElementTypesConfigurationsPackage.eINSTANCE.eClass();
		org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.eINSTANCE.eClass();
		ViewpointPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theRepresentationPackage.createPackageContents();

		// Initialize created meta-data
		theRepresentationPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put(theRepresentationPackage,
				new EValidator.Descriptor() {
					@Override
					public EValidator getEValidator() {
						return RepresentationValidator.INSTANCE;
					}
				});

		// Mark meta-data to indicate it can't be changed
		theRepresentationPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RepresentationPackage.eNS_URI, theRepresentationPackage);
		return theRepresentationPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getSiriusDiagramPrototype() {
		return siriusDiagramPrototypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getSiriusDiagramPrototype_DiagramDescription() {
		return (EReference) siriusDiagramPrototypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getSiriusDiagramPrototype_CreationCommandClass() {
		return (EAttribute) siriusDiagramPrototypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getSiriusDiagramPrototype_Session() {
		return (EReference) siriusDiagramPrototypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getSiriusDiagramPrototype__IsValidClass__DiagnosticChain_Map() {
		return siriusDiagramPrototypeEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getSession() {
		return sessionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public RepresentationFactory getRepresentationFactory() {
		return (RepresentationFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) {
			return;
		}
		isCreated = true;

		// Create classes and their features
		siriusDiagramPrototypeEClass = createEClass(SIRIUS_DIAGRAM_PROTOTYPE);
		createEReference(siriusDiagramPrototypeEClass, SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION);
		createEAttribute(siriusDiagramPrototypeEClass, SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS);
		createEReference(siriusDiagramPrototypeEClass, SIRIUS_DIAGRAM_PROTOTYPE__SESSION);
		createEOperation(siriusDiagramPrototypeEClass, SIRIUS_DIAGRAM_PROTOTYPE___IS_VALID_CLASS__DIAGNOSTICCHAIN_MAP);

		sessionEClass = createEClass(SESSION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) {
			return;
		}
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage theRepresentationPackage_1 = (org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage) EPackage.Registry.INSTANCE
				.getEPackage(org.eclipse.papyrus.infra.architecture.representation.RepresentationPackage.eNS_URI);
		DescriptionPackage theDescriptionPackage = (DescriptionPackage) EPackage.Registry.INSTANCE.getEPackage(DescriptionPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		siriusDiagramPrototypeEClass.getESuperTypes().add(theRepresentationPackage_1.getPapyrusRepresentationKind());

		// Initialize classes, features, and operations; add parameters
		initEClass(siriusDiagramPrototypeEClass, SiriusDiagramPrototype.class, "SiriusDiagramPrototype", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getSiriusDiagramPrototype_DiagramDescription(), theDescriptionPackage.getDiagramDescription(), null, "diagramDescription", null, 1, 1, SiriusDiagramPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, //$NON-NLS-1$
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSiriusDiagramPrototype_CreationCommandClass(), theEcorePackage.getEString(), "creationCommandClass", null, 1, 1, SiriusDiagramPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, //$NON-NLS-1$
				IS_ORDERED);
		initEReference(getSiriusDiagramPrototype_Session(), this.getSession(), null, "session", null, 0, 1, SiriusDiagramPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, //$NON-NLS-1$
				IS_ORDERED);

		EOperation op = initEOperation(getSiriusDiagramPrototype__IsValidClass__DiagnosticChain_Map(), theEcorePackage.getEBoolean(), "isValidClass", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEDiagnosticChain(), "chain", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		EGenericType g1 = createEGenericType(theEcorePackage.getEMap());
		EGenericType g2 = createEGenericType(theEcorePackage.getEJavaObject());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(theEcorePackage.getEJavaObject());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "context", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(sessionEClass, Session.class, "Session", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Create resource
		createResource(eNS_URI);
	}

} // RepresentationPackageImpl

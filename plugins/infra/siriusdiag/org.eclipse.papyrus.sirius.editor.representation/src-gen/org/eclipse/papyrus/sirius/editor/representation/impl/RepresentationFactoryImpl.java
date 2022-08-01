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
package org.eclipse.papyrus.sirius.editor.representation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.papyrus.sirius.editor.representation.RepresentationFactory;
import org.eclipse.papyrus.sirius.editor.representation.RepresentationPackage;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramPrototype;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class RepresentationFactoryImpl extends EFactoryImpl implements RepresentationFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static RepresentationFactory init() {
		try {
			RepresentationFactory theRepresentationFactory = (RepresentationFactory) EPackage.Registry.INSTANCE.getEFactory(RepresentationPackage.eNS_URI);
			if (theRepresentationFactory != null) {
				return theRepresentationFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RepresentationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RepresentationFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE:
			return createSiriusDiagramPrototype();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public SiriusDiagramPrototype createSiriusDiagramPrototype() {
		SiriusDiagramPrototypeImpl siriusDiagramPrototype = new SiriusDiagramPrototypeImpl();
		return siriusDiagramPrototype;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public RepresentationPackage getRepresentationPackage() {
		return (RepresentationPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RepresentationPackage getPackage() {
		return RepresentationPackage.eINSTANCE;
	}

} // RepresentationFactoryImpl

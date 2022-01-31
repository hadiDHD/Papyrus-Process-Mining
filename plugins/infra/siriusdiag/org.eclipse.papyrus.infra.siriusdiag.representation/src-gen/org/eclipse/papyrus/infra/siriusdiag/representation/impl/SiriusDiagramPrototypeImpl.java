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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.papyrus.infra.architecture.representation.impl.PapyrusRepresentationKindImpl;
import org.eclipse.papyrus.infra.siriusdiag.representation.RepresentationPackage;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.representation.util.RepresentationValidator;
import org.eclipse.sirius.diagram.description.DiagramDescription;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sirius Diagram Prototype</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.papyrus.infra.siriusdiag.representation.impl.SiriusDiagramPrototypeImpl#getDiagramDescription <em>Diagram Description</em>}</li>
 * <li>{@link org.eclipse.papyrus.infra.siriusdiag.representation.impl.SiriusDiagramPrototypeImpl#getCreationCommandClass <em>Creation Command Class</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SiriusDiagramPrototypeImpl extends PapyrusRepresentationKindImpl implements SiriusDiagramPrototype {
	/**
	 * The cached value of the '{@link #getDiagramDescription() <em>Diagram Description</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDiagramDescription()
	 * @generated
	 * @ordered
	 */
	protected DiagramDescription diagramDescription;

	/**
	 * The default value of the '{@link #getCreationCommandClass() <em>Creation Command Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCreationCommandClass()
	 * @generated
	 * @ordered
	 */
	protected static final String CREATION_COMMAND_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCreationCommandClass() <em>Creation Command Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCreationCommandClass()
	 * @generated
	 * @ordered
	 */
	protected String creationCommandClass = CREATION_COMMAND_CLASS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SiriusDiagramPrototypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RepresentationPackage.Literals.SIRIUS_DIAGRAM_PROTOTYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DiagramDescription getDiagramDescription() {
		if (diagramDescription != null && diagramDescription.eIsProxy()) {
			InternalEObject oldDiagramDescription = (InternalEObject) diagramDescription;
			diagramDescription = (DiagramDescription) eResolveProxy(oldDiagramDescription);
			if (diagramDescription != oldDiagramDescription) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION, oldDiagramDescription, diagramDescription));
				}
			}
		}
		return diagramDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DiagramDescription basicGetDiagramDescription() {
		return diagramDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDiagramDescription(DiagramDescription newDiagramDescription) {
		DiagramDescription oldDiagramDescription = diagramDescription;
		diagramDescription = newDiagramDescription;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION, oldDiagramDescription, diagramDescription));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getCreationCommandClass() {
		return creationCommandClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCreationCommandClass(String newCreationCommandClass) {
		String oldCreationCommandClass = creationCommandClass;
		creationCommandClass = newCreationCommandClass;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS, oldCreationCommandClass, creationCommandClass));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isValidClass(DiagnosticChain chain, Map<Object, Object> context) {
		// TODO: implement this method
		// -> specify the condition that violates the invariant
		// -> verify the details of the diagnostic, including severity and message
		// Ensure that you remove @generated or mark it @generated NOT
		if (false) {
			if (chain != null) {
				chain.add(new BasicDiagnostic(Diagnostic.ERROR,
						RepresentationValidator.DIAGNOSTIC_SOURCE,
						RepresentationValidator.SIRIUS_DIAGRAM_PROTOTYPE__IS_VALID_CLASS,
						EcorePlugin.INSTANCE.getString("_UI_GenericInvariant_diagnostic", new Object[] { "isValidClass", EObjectValidator.getObjectLabel(this, context) }), //$NON-NLS-1$ //$NON-NLS-2$
						new Object[] { this }));
			}
			return false;
		}
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION:
			if (resolve) {
				return getDiagramDescription();
			}
			return basicGetDiagramDescription();
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS:
			return getCreationCommandClass();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION:
			setDiagramDescription((DiagramDescription) newValue);
			return;
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS:
			setCreationCommandClass((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION:
			setDiagramDescription((DiagramDescription) null);
			return;
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS:
			setCreationCommandClass(CREATION_COMMAND_CLASS_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__DIAGRAM_DESCRIPTION:
			return diagramDescription != null;
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE__CREATION_COMMAND_CLASS:
			return CREATION_COMMAND_CLASS_EDEFAULT == null ? creationCommandClass != null : !CREATION_COMMAND_CLASS_EDEFAULT.equals(creationCommandClass);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case RepresentationPackage.SIRIUS_DIAGRAM_PROTOTYPE___IS_VALID_CLASS__DIAGNOSTICCHAIN_MAP:
			return isValidClass((DiagnosticChain) arguments.get(0), (Map<Object, Object>) arguments.get(1));
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (creationCommandClass: "); //$NON-NLS-1$
		result.append(creationCommandClass);
		result.append(')');
		return result.toString();
	}

} // SiriusDiagramPrototypeImpl

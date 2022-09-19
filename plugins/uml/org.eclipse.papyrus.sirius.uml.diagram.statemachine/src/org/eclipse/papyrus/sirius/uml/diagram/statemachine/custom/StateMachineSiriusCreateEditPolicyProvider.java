/******************************************************************************
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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.statemachine.custom;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerViewNodeContainerCompartment2EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerViewNodeContainerCompartmentEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.policies.AbstractCreateEditPolicyProvider;
import org.eclipse.uml2.uml.Region;

/**
 * The Class MyCreateEditPolicyProvider.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
@SuppressWarnings("restriction")
public class StateMachineSiriusCreateEditPolicyProvider extends AbstractCreateEditPolicyProvider {

	/**
	 * Creates the edit policies.
	 *
	 * @param arg0
	 *            the arg 0
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider#createEditPolicies(org.eclipse.gef.EditPart)
	 */

	@Override
	public void createEditPolicies(EditPart editpart) {
		EditPolicy regionCreationPolicy = new StateMachineRegionPolicy();
		editpart.installEditPolicy(EditPolicyRoles.CREATION_ROLE, regionCreationPolicy);
	}

	/**
	 * Checks if is valid edit part.
	 *
	 * @param editPart
	 *            the edit part
	 * @return true, if is valid edit part
	 * @see org.eclipse.sirius.diagram.ui.internal.edit.policies.AbstractCreateEditPolicyProvider#isValidEditPart(org.eclipse.gef.EditPart)
	 */

	@Override
	protected boolean isValidEditPart(EditPart editPart) {
		if (editPart instanceof DNodeContainerViewNodeContainerCompartmentEditPart || editPart instanceof DNodeContainerViewNodeContainerCompartment2EditPart) {
			Object model = editPart.getModel();
			if (model instanceof Node) {
				EObject element = ((Node) model).getElement();
				if (element instanceof DNodeContainer) {
					EObject target = ((DNodeContainer) element).getTarget();
					if (target instanceof Region) {
						return true;
					}
				}
			}
		}
		return false;
	}

}

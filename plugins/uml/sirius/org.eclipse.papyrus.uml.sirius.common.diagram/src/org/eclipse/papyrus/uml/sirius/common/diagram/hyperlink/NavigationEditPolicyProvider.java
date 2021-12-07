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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and others
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.hyperlink;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.ui.internal.edit.policies.AbstractCreateEditPolicyProvider;
import org.eclipse.uml2.uml.Element;

/**
 * The Class MyCreateEditPolicyProvider.
 *
 * @author Aurélien Didier (Artal Technologies)
 */
@SuppressWarnings("restriction")
public class NavigationEditPolicyProvider extends AbstractCreateEditPolicyProvider {

	/**
	 * Creates the edit policies.
	 *
	 * @param arg0 the arg 0
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider#createEditPolicies(org.eclipse.gef.EditPart)
	 */

	@Override
	public void createEditPolicies(EditPart editpart) {

		editpart.installEditPolicy(EditPolicyRoles.OPEN_ROLE, new NavigationEditPolicy());
	}

	/**
	 * Checks if is valid edit part.
	 *
	 * @param editPart the edit part
	 * @return true, if is valid edit part
	 * @see org.eclipse.sirius.diagram.ui.internal.edit.policies.AbstractCreateEditPolicyProvider#isValidEditPart(org.eclipse.gef.EditPart)
	 */

	@Override
	protected boolean isValidEditPart(EditPart editPart) {

		Object model = editPart.getModel();
		if (model instanceof NodeImpl) {
			EObject element = ((NodeImpl) model).getElement();
			if (element instanceof DNode) {
				EObject target = ((DNode) element).getTarget();
				if (!(target instanceof Element)) {
					return true;
				}
			}
		}
		return false;
	}

}

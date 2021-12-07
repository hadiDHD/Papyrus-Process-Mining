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
 *    Aurélien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.sirius.diagram.DragAndDropTarget;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.SiriusContainerDropPolicy;
import org.eclipse.sirius.diagram.ui.internal.edit.policies.AbstractCreateEditPolicyProvider;

/**
 * The Class SiriusEditPolicyProvider.
 *
 * @author Aurélien Didier (Artal Technologies)
 */
@SuppressWarnings("restriction")
public class SiriusEditPolicyProvider extends AbstractCreateEditPolicyProvider {

	/**
	 * Creates the edit policies.
	 *
	 * @param editpart
	 *            the editpart
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider#createEditPolicies(org.eclipse.gef.EditPart)
	 */

	@Override
	public void createEditPolicies(EditPart editpart) {
		editpart.installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new SiriusContainerDropPolicy());
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
		Object model = editPart.getModel();
		if (model instanceof NodeImpl) {
			EObject element = ((NodeImpl) model).getElement();
			if (element instanceof DragAndDropTarget) {
				return true;
			}
		}
		if (model instanceof Diagram) {
			EObject element = ((Diagram) model).getElement();
			if (element instanceof DragAndDropTarget) {
				return true;
			}
		}

		return false;
	}

}

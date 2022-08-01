/******************************************************************************
 * Copyright (c) 2021, 2022 CEA LIST, Artal Technologies
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
 *    Vincent Lorenzo (CEA-LIST) - vincent.lorenzo@cea.fr - Bug 578383
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.common.validation;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.siriusdiag.sirius.utils.SiriusDiagramUtils;
import org.eclipse.papyrus.sirius.uml.diagram.common.provider.ValidationDecoratorProvider;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;

/**
 * Adapted from Papyrus GMF Diagram code
 */
public class SiriusValidationDecoratorProvider extends ValidationDecoratorProvider implements IDecoratorProvider {

	/**
	 * 
	 * @see org.eclipse.papyrus.uml.diagram.common.providers.ValidationDecoratorProvider#createDecorators(org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget)
	 *
	 * @param decoratorTarget
	 */
	@Override
	public void createDecorators(IDecoratorTarget decoratorTarget) {
		EditPart editPart = decoratorTarget.getAdapter(EditPart.class);
		if (editPart instanceof GraphicalEditPart ||
				editPart instanceof AbstractConnectionEditPart) {
			Object model = editPart.getModel();
			if ((model instanceof View)) {
				View view = (View) model;
				if (!(view instanceof Edge) && !view.isSetElement()) {
					return;
				}
			}
			EditDomain ed = editPart.getViewer().getEditDomain();
			if (!(ed instanceof DiagramEditDomain)) {
				return;
			}
			if (((DiagramEditDomain) ed).getEditorPart() instanceof DDiagramEditor) {
				decoratorTarget.installDecorator(KEY, new StatusDecorator(decoratorTarget));
			}
		}
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.uml.diagram.common.providers.ValidationDecoratorProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 *
	 * @param operation
	 * @return
	 */
	@Override
	public boolean provides(IOperation operation) {
		if (!(operation instanceof CreateDecoratorsOperation)) {
			return false;
		}
		IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation) operation).getDecoratorTarget();
		View view = decoratorTarget.getAdapter(View.class);
		return view != null && SiriusDiagramUtils.isSiriusDiagramView(view);
	}

}

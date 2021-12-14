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
package org.eclipse.papyrus.uml.sirius.sequence.diagram.provider;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.ObservationPointEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.provider.SequenceDiagramEditPartProvider;

/**
 * The Class SequenceEditPartProvider.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class SequenceEditPartProvider extends SequenceDiagramEditPartProvider {


	/**
	 * Gets the node edit part class.
	 *
	 * @param view the view
	 * @return the node edit part class
	 * @see org.eclipse.sirius.diagram.sequence.ui.tool.internal.provider.SequenceDiagramEditPartProvider#getNodeEditPartClass(org.eclipse.gmf.runtime.notation.View)
	 */
	
	@Override
	protected Class<?> getNodeEditPartClass(View view) {
		// int visualID = SiriusVisualIDRegistry.getVisualID(view);
		// if (visualID == 2001) {
		// return SiriusObservationPointPart.class;
		// }

		Class<?> nodeEditPartClass = super.getNodeEditPartClass(view);


		return nodeEditPartClass;
	}

	/**
	 * Creates the graphic edit part.
	 *
	 * @param view the view
	 * @return the i graphical edit part
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#createGraphicEditPart(org.eclipse.gmf.runtime.notation.View)
	 */
	
	@Override
	public IGraphicalEditPart createGraphicEditPart(View view) {
		Class editpartClass = getEditPartClass(view);
		if (editpartClass.isAssignableFrom(ObservationPointEditPart.class)) {
			IGraphicalEditPart graphicEditPart = new ObservationPointEditPart(view) {
				@Override
				protected void createDefaultEditPolicies() {
					super.createDefaultEditPolicies();
					removeEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE);
					installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new SiriusGraphicalNodeEditPolicy());

				}
			};
			return graphicEditPart;
		}



		return super.createGraphicEditPart(view);
	}

	/**
	 * Gets an editpart class for the given view.
	 *
	 * @param view the view
	 * @return <code>Class</code>
	 */
	private Class getEditPartClass(View view) {
		if (view instanceof Diagram)
			return getDiagramEditPartClass(view);
		else if (view instanceof Edge)
			return getEdgeEditPartClass(view);
		else if (view instanceof Node)
			return getNodeEditPartClass(view);
		return null;
	}

}

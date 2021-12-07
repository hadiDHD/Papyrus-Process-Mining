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
 *    Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.xtext.integration.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.sirius.xtext.integration.ui.editpart.XtextDEdgeNameEditPart;
import org.eclipse.papyrus.uml.sirius.xtext.integration.ui.editpart.XtextSiriusDNode3EditPart;
import org.eclipse.papyrus.uml.sirius.xtext.integration.ui.editpart.XtextSiriusDNodeContainerName2EditPart;
import org.eclipse.papyrus.uml.sirius.xtext.integration.ui.editpart.XtextSiriusDNodeContainerNameEditPart;
import org.eclipse.papyrus.uml.sirius.xtext.integration.ui.editpart.XtextSiriusDNodeListElementEditPart;
import org.eclipse.papyrus.uml.sirius.xtext.integration.ui.editpart.XtextSiriusDNodeNameEditPart;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.CombinedFragment;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.EndOfLife;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Execution;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.InstanceRole;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.InteractionUse;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Lifeline;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.LostMessageEnd;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Message;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.ObservationPoint;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.Operand;
import org.eclipse.sirius.diagram.sequence.business.internal.elements.State;
import org.eclipse.sirius.diagram.sequence.business.internal.metamodel.SequenceDDiagramSpec;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.CombinedFragmentCompartmentEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.CombinedFragmentEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.EndOfLifeEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.ExecutionEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.InstanceRoleEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.InteractionUseEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.LifelineEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.LostMessageEndEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.ObservationPointEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.OperandCompartmentEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.OperandEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.SequenceDiagramEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.SequenceMessageNameEditPart;
import org.eclipse.sirius.diagram.sequence.ui.tool.internal.edit.part.StateEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DDiagramEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DEdgeNameEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode3EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerName2EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerNameEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListElementEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.NotationViewIDs;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.SiriusEditPartFactory;
import org.eclipse.sirius.diagram.ui.part.SiriusVisualIDRegistry;

/**
 * A factory for creating ITextAwareEditPart EditPart objects.
 */
@SuppressWarnings("restriction")
public class XtextSiriusEditPartFactory extends SiriusEditPartFactory {

	/**
	 * Creates a new ITextAwareEditPart EditPart object.
	 * This method allows to extends Sirius graphical model elements to integrate Xtext.
	 *
	 * @param context
	 *            the context
	 * @param model
	 *            the model
	 * @return the edits the part
	 * @see org.eclipse.sirius.diagram.ui.internal.edit.parts.SiriusEditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */

	@Override
	public EditPart createEditPart(EditPart context, Object model) {

		if (model instanceof View) {
			final View view = (View) model;

			//For sequence diagram edit part
			if (SiriusVisualIDRegistry.getVisualID(view)==(DDiagramEditPart.VISUAL_ID)
					&& view.getElement() instanceof SequenceDDiagramSpec){
				return new SequenceDiagramEditPart(view);
			}
	        if (view instanceof Node) {
	            EObject semanticElement = ViewUtil.resolveSemanticElement(view);
	            if (DiagramPackage.eINSTANCE.getDNode().isInstance(semanticElement)) {
	                if (InstanceRole.notationPredicate().apply(view)) {
	                    return new InstanceRoleEditPart(view);
	                } else if (Lifeline.notationPredicate().apply(view)) {
	                    return new LifelineEditPart(view);
	                } else if (Execution.notationPredicate().apply(view)) {
	                    return new ExecutionEditPart (view);
	                } else if (State.notationPredicate().apply(view)) {
	                    return new StateEditPart(view);
	                } else if (EndOfLife.notationPredicate().apply(view)) {
	                    return new EndOfLifeEditPart(view);
	                } else if (LostMessageEnd.notationPredicate().apply(view)) {
	                    return new LostMessageEndEditPart(view);
	                } else if (ObservationPoint.notationPredicate().apply(view)) {
	                    return new ObservationPointEditPart(view);
	                }
	            } else if (DiagramPackage.eINSTANCE.getDNodeContainer().isInstance(semanticElement)) {
	                if (InteractionUse.notationPredicate().apply(view)) {
	                    return new InteractionUseEditPart(view);
	                } else if (CombinedFragment.notationPredicate().apply(view)) {
	                    return new CombinedFragmentEditPart(view);
	                } else if (CombinedFragment.compartmentNotationPredicate().apply(view)) {
	                    return new CombinedFragmentCompartmentEditPart(view);
	                } else if (Operand.notationPredicate().apply(view)) {
	                    return new OperandEditPart(view);
	                } else if (Operand.compartmentNotationPredicate().apply(view)) {
	                    return new OperandCompartmentEditPart(view);
	                }
	            } else if (DiagramPackage.eINSTANCE.getDEdge().isInstance(semanticElement)) {
	                DEdge edge = (DEdge) semanticElement;
	                if (Message.viewpointElementPredicate().apply(edge) && SiriusVisualIDRegistry.getVisualID(view) == SequenceMessageNameEditPart.VISUAL_ID) {
	                    return new SequenceMessageNameEditPart(view);
	                }
	            }
	        }


	        //For other edit part
			switch (SiriusVisualIDRegistry.getVisualID(view)) {
			case NotationViewIDs.DNODE_NAME_EDIT_PART_VISUAL_ID:
			case NotationViewIDs.DNODE_NAME_2_EDIT_PART_VISUAL_ID:
			case NotationViewIDs.DNODE_NAME_3_EDIT_PART_VISUAL_ID:
			case NotationViewIDs.DNODE_NAME_4_EDIT_PART_VISUAL_ID:
				return new XtextSiriusDNodeNameEditPart(view);
			case DNodeContainerName2EditPart.VISUAL_ID:
				return new XtextSiriusDNodeContainerName2EditPart(view);
			case DNodeContainerNameEditPart.VISUAL_ID:
				return new XtextSiriusDNodeContainerNameEditPart(view);
			case DNodeListElementEditPart.VISUAL_ID:
				return new XtextSiriusDNodeListElementEditPart(view);
			case DNode3EditPart.VISUAL_ID:
				return new XtextSiriusDNode3EditPart(view);
			case DEdgeNameEditPart.VISUAL_ID:
				return new XtextDEdgeNameEditPart(view);
			
				
			}
			
		}

		return super.createEditPart(context, model);
	}

}

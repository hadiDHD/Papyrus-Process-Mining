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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.gmfdiag.common.adapter.SemanticAdapter;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.commands.CustomRegionCreateElementCommand;
import org.eclipse.sirius.diagram.description.tool.ContainerCreationDescription;
import org.eclipse.uml2.uml.State;

/**
 * The Class MyRegionPolicy.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class StateMachineRegionPolicy extends CreationEditPolicy {

	/** The size on drop feedback. */
	IFigure sizeOnDropFeedback = null;

	/** The drop location. */
	String dropLocation = "";

	/**
	 * Erase target feedback.
	 *
	 * @param request
	 *            the request
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#eraseTargetFeedback(org.eclipse.gef.Request)
	 */

	@Override
	public void eraseTargetFeedback(Request request) {
		if (sizeOnDropFeedback != null) {
			LayerManager.Helper.find(getHost()).getLayer(LayerConstants.FEEDBACK_LAYER).remove(sizeOnDropFeedback);
			sizeOnDropFeedback = null;
		}
	}

	/**
	 * Gets the reparent command.
	 *
	 * @param changeBoundsRequest the change bounds request
	 * @return the reparent command
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy#getReparentCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */

	@Override
	protected Command getReparentCommand(ChangeBoundsRequest changeBoundsRequest) {
		View container = getHost().getAdapter(View.class);
		EObject context = container == null ? null : ViewUtil.resolveSemanticElement(container);
		CompositeCommand cc = new CompositeCommand("move (re-parent) state");
		Iterator<?> it = changeBoundsRequest.getEditParts().iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof EditPart) {
				EditPart ep = (EditPart) next;
				if (ep instanceof LabelEditPart) {
					continue;
				}
				if (ep instanceof GroupEditPart) {
					cc.compose(getReparentGroupCommand((GroupEditPart) ep));
				}
				View view = ep.getAdapter(View.class);
				if (view == null) {
					continue;
				}
				EObject semantic = ViewUtil.resolveSemanticElement(view);
				if (semantic == null) {
					cc.compose(getReparentViewCommand((IGraphicalEditPart) ep));
				} else if (context != null && shouldReparent(semantic, context)) {
					cc.compose(getReparentCommand((IGraphicalEditPart) ep));
					if (semantic instanceof State) {
						// State state = (State) semantic;
						// correct container of transitions
//						for (Transition transition : state.getOutgoings()) {
//							cc.compose(new EMFtoGMFCommandWrapper(
//									new EMFCustomTransitionRetargetContainerCommand(transition)));
//						}
//						for (Transition transition : state.getIncomings()) {
//							cc.compose(new EMFtoGMFCommandWrapper(
//									new EMFCustomTransitionRetargetContainerCommand(transition)));
//						}
					}
				}
			}
		}
		return cc.isEmpty() ? null : new ICommandProxy(cc.reduce());
	}

	/**
	 * Gets the command.
	 *
	 * @param request
	 *            the request
	 * @return the command
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy#getCommand(org.eclipse.gef.Request)
	 */

	@Override
	public Command getCommand(Request request) {
		// CHECK THIS
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
		CompositeTransactionalCommand cc = new CompositeTransactionalCommand(editingDomain, DiagramUIMessages.AddCommand_Label);
		if (understandsRequest(request)) {
			if (request instanceof CreateUnspecifiedTypeRequest) {
				CreateUnspecifiedTypeRequest unspecReq = (CreateUnspecifiedTypeRequest) request;
				for (Iterator<?> iter = unspecReq.getElementTypes().iterator(); iter.hasNext();) {
					IElementType elementType = (IElementType) iter.next();
					if (((IHintedType) elementType).getSemanticHint().equals(((IHintedType) getElementTypeByUniqueId("org.eclipse.papyrus.umldi.Region_Shape")).getSemanticHint())) {
						// starting point is the existing region compartment on
						// which mouse was moving
						View existingRegionCompartmentView = (View) getHost().getModel();
						// the existing region view
						View existingRegionView = (View) existingRegionCompartmentView.eContainer();
						// get and adaptable for it, to pass on to commands
						IAdaptable adaptableForExistingRegionView = new SemanticAdapter(null, existingRegionView);
						// do the whole job
						CustomRegionCreateElementCommand createNewRegion = new CustomRegionCreateElementCommand(adaptableForExistingRegionView, null, ((IGraphicalEditPart) getHost()).getDiagramPreferencesHint(), editingDomain,
								DiagramUIMessages.CreateCommand_Label, dropLocation);
						cc.compose(createNewRegion);
						return new ICommandProxy(cc.reduce());
					}
				}
			} else if (request instanceof CreateViewRequest) {
				CreateViewRequest create = (CreateViewRequest) request;
				for (CreateViewRequest.ViewDescriptor descriptor : create.getViewDescriptors()) {
					if (((IHintedType) getElementTypeByUniqueId("org.eclipse.papyrus.umldi.Region_Shape")).getSemanticHint().equals(descriptor.getSemanticHint())) {
						// Creating a region view as a sibling of the region owning this compartment
						IAdaptable compartment = new SemanticAdapter(null, ((View) getHost().getModel()).eContainer());

						// Create the view with a reasonable initial layout
						CustomRegionCreateElementCommand command = new CustomRegionCreateElementCommand(
								compartment, descriptor.getElementAdapter(),
								((IGraphicalEditPart) getHost()).getDiagramPreferencesHint(),
								editingDomain, DiagramUIMessages.CreateCommand_Label, dropLocation);
						cc.compose(command);
					}
				}

				// It's all-or-nothing: if anything besides regions were being created (which
				// would be an odd sort of a compound request), then only the regions will be
				if (!cc.isEmpty()) {
					return new ICommandProxy(cc.reduce());
				} else {
					return super.getCommand(request);
				}
			} else if (request instanceof ChangeBoundsRequest) {
				return getReparentCommand((ChangeBoundsRequest) request);
				/*
				 * ChangeBoundsRequest changeBoundsRequest = (ChangeBoundsRequest)request;
				 * Point mouseLocation = changeBoundsRequest.getLocation();
				 *
				 * DropObjectsRequest dropRequest = new DropObjectsRequest();
				 * dropRequest.setLocation(mouseLocation);
				 *
				 * List<View> list = new ArrayList<View>();
				 * Iterator<EditPart> it = changeBoundsRequest.getEditParts().iterator();
				 * while(it.hasNext()) {
				 * EditPart ep = it.next();
				 * if(ep instanceof RegionEditPart) {
				 * View regionToDrag = (View)ep.getModel();
				 * list.add(regionToDrag);
				 * }
				 * }
				 * View container = (View)getHost().getAdapter(View.class);
				 * EObject context = container == null ? null : ViewUtil.resolveSemanticElement(container);
				 * dropRequest.setObjects(list);
				 * return getHost().getCommand(dropRequest);
				 */
			}

			return super.getCommand(request);
		}
		return null;
	}
	



	/**
	 * Gets the element type by unique id.
	 *
	 * @param id the id
	 * @return the element type by unique id
	 * @generated 
	 */
	private static IElementType getElementTypeByUniqueId(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}
	/**
	 * Gets the size on drop feedback.
	 *
	 * @return the size on drop feedback
	 */
	protected IFigure getSizeOnDropFeedback() {
		if (sizeOnDropFeedback == null) {
			sizeOnDropFeedback = new RectangleFigure();
			FigureUtilities.makeGhostShape((Shape) sizeOnDropFeedback);
			((Shape) sizeOnDropFeedback).setLineStyle(Graphics.LINE_DASHDOT);
			sizeOnDropFeedback.setForegroundColor(ColorConstants.white);
			LayerManager.Helper.find(getHost()).getLayer(LayerConstants.FEEDBACK_LAYER).add(sizeOnDropFeedback);
		}
		return sizeOnDropFeedback;
	}

	/**
	 * Gets the drop location.
	 *
	 * @return the drop location
	 */
	public String getDropLocation() {
		return dropLocation;
	}

	/**
	 * Gets the target edit part.
	 *
	 * @param request
	 *            the request
	 * @return the target edit part
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */

	// @Override
	@Override
	public EditPart getTargetEditPart(Request request) {
		if (request instanceof CreateUnspecifiedTypeRequest) {
			CreateUnspecifiedTypeRequest createUnspecifiedTypeRequest = (CreateUnspecifiedTypeRequest) request;
			if (understandsRequest(request)) {
				List<?> elementTypes = createUnspecifiedTypeRequest.getElementTypes();
				// Treat the case where only one element type is listed
				// Only take EntryPoint or ExitPoint element type into account
				if ((elementTypes.size() == 1) && (((IElementType) (elementTypes.get(0)) == getElementTypeByUniqueId("org.eclipse.papyrus.umldi.Pseudostate_EntryPointShape")) || ((IElementType) (elementTypes.get(0)) ==getElementTypeByUniqueId("org.eclipse.papyrus.umldi.Pseudostate_ExitPointShape")))) {
					// If the target is a compartment replace by its grand parent edit part
					if ((getHost() instanceof ShapeCompartmentEditPart)) {
						return getHost().getParent().getParent().getParent();
					}
				}
			}
		}
		return super.getTargetEditPart(request);
	}

	/**
	 * Show target feedback.
	 *
	 * @param request
	 *            the request
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#showTargetFeedback(org.eclipse.gef.Request)
	 */

	@Override
	public void showTargetFeedback(Request request) {

		if (request instanceof CreateRequest) {
			Object newObject = ((CreateRequest) request).getNewObject();
			if (newObject instanceof ContainerCreationDescription) {
				if (((ContainerCreationDescription) newObject).getName().equals("Region")) {
					// CreateUnspecifiedTypeRequest unspecReq = (CreateUnspecifiedTypeRequest) request;
					// for (Iterator<?> iter = unspecReq.getElementTypes().iterator(); iter.hasNext();) {
					// IElementType elementType = (IElementType) iter.next();
					// if (elementType.equals(UMLElementTypes.Region_Shape)) {
					IFigure targetFig = ((GraphicalEditPart) getHost()).getFigure();
					// make a local copy
					Rectangle targetFigBounds = targetFig.getBounds().getCopy();
					// transform the coordinates to absolute
					targetFig.translateToAbsolute(targetFigBounds);
					// retrieve mouse location
					Point mouseLocation = ((CreateRequest) request).getLocation();
					// get the drop location, i.e. RIGHT, LEFT, TOP, BOTTOM
					dropLocation = Zone.getZoneFromLocationInRectangleWithAbsoluteCoordinates(mouseLocation, targetFigBounds);
					// perform corresponding change (scaling, translation) on
					// targetFigBounds
					// and updates the graph node drop location property
					if (Zone.isTop(dropLocation)) {
						targetFigBounds.setSize(targetFigBounds.getSize().scale(1.0, 0.5));
					} else if (Zone.isLeft(dropLocation)) {
						targetFigBounds.setSize(targetFigBounds.getSize().scale(0.5, 1.0));
					} else if (Zone.isRight(dropLocation)) {
						targetFigBounds.setSize(targetFigBounds.getSize().scale(0.5, 1.0));
						targetFigBounds.translate(targetFigBounds.width, 0);
					} else if (Zone.isBottom(dropLocation)) {
						targetFigBounds.setSize(targetFigBounds.getSize().scale(1.0, 0.5));
						targetFigBounds.translate(0, targetFigBounds.height);
					}
					getSizeOnDropFeedback().setBounds(new PrecisionRectangle(targetFigBounds));
					// }
					// }//
				}
			}

//		} else {
//			super.showTargetFeedback(request);
		}
	}




	/**
	 * The Class Zone.
	 */
	public static class Zone {

		/**
		 * A default empty property string which serves when creating a region
		 * without initial graphical context.
		 */
		public static final String NONE = "";
		/**
		 * The code for a region in the TOP part of a given area.
		 */
		public static final String TOP = "T";
		/**
		 * The code for a region in the RIGHT part of a given area.
		 */
		public static final String RIGHT = "R";
		/**
		 * The code for a region in the BOTTOM part of a given area.
		 */
		public static final String BOTTOM = "B";
		/**
		 * The code for a region in the LEFT part of a given area.
		 */
		public static final String LEFT = "L";

		/**
		 * Computes the zone at location within bounds using absolute coordinates.
		 *
		 * @param location
		 *            the location
		 * @param rect
		 *            the bounds
		 * @return the zone
		 */
		public static String getZoneFromLocationInRectangleWithAbsoluteCoordinates(Point location, Rectangle rect) {
			// d1 is for the first diagonal (going up) rect
			double d1 = location.y - 1.0 * rect.height / rect.width * (rect.x - location.x) - rect.y - rect.height;
			// d2 is for the second (going down)
			double d2 = location.y + 1.0 * rect.height / rect.width * (rect.x - location.x) - rect.y;
			if ((d1 <= 0) && (d2 <= 0)) {
				return Zone.TOP;
			}
			if ((d1 <= 0) && (d2 > 0)) {
				return Zone.LEFT;
			}
			if ((d1 > 0) && (d2 <= 0)) {
				return Zone.RIGHT;
			}
			if ((d1 > 0) && (d2 > 0)) {
				return Zone.BOTTOM;
			}
			return Zone.NONE;
		}

		/**
		 * Checks whether the leaf location encoded is TOP.
		 *
		 * @param s
		 *            a string
		 * @return boolean true or false
		 */
		public static boolean isTop(String s) {
			if (s == null) {
				return false;
			}
			return s.endsWith(Zone.TOP);
		}

		/**
		 * Checks whether the leaf location encoded is LEFT.
		 *
		 * @param s
		 *            a string
		 *
		 * @return boolean true or false
		 */
		public static boolean isLeft(String s) {
			if (s == null) {
				return false;
			}
			return s.endsWith(Zone.LEFT);
		}

		/**
		 * Checks whether the leaf location encoded is BOTTOM.
		 *
		 * @param s
		 *            a string
		 *
		 * @return boolean true or false
		 */
		public static boolean isBottom(String s) {
			if (s == null) {
				return false;
			}
			return s.endsWith(Zone.BOTTOM);
		}

		/**
		 * Checks whether the leaf location encoded is RIGHT.
		 *
		 * @param s
		 *            a string
		 *
		 * @return boolean true or false
		 */
		public static boolean isRight(String s) {
			if (s == null) {
				return false;
			}
			return s.endsWith(Zone.RIGHT);
		}

	}


}

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
package org.eclipse.papyrus.uml.sirius.common.diagram.hyperlink;

import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode3EditPart;

/**
 * @generated
 */
public class SiriusShortCutEditPart extends DNode3EditPart {

	public SiriusShortCutEditPart(View view) {
		super(view);
	}
//
//
//	/**
//	 * @generated
//	 */
//	public static final String VISUAL_ID = "Diagram_ShortcutShape";
//
//	/**
//	 * @generated
//	 */
//	protected IFigure contentPane;
//
//	/**
//	 * @generated
//	 */
//	protected IFigure primaryShape;
//
//	/**
//	 * @generated
//	 */
//	public SiriusShortCutEditPart(View view) {
//		super(view);
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	protected void createDefaultEditPolicies() {
//		super.createDefaultEditPolicies();
////		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new DefaultSemanticEditPolicy());
////
////		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new DefaultGraphicalNodeEditPolicy());
////
////		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
//		installEditPolicy(EditPolicyRoles.OPEN_ROLE, new ShortCutDiagramEditPolicy());
//		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
//		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
//	}
//
//	/**
//	 * @generated
//	 */
//	protected LayoutEditPolicy createLayoutEditPolicy() {
//		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {
//
//			@Override
//			protected EditPolicy createChildEditPolicy(EditPart child) {
//				View childView = (View) child.getModel();
//				String vid = UMLVisualIDRegistry.getVisualID(childView);
//				if (vid != null) {
//					switch (vid) {
//					case DiagramNameEditPart.VISUAL_ID:
//						return new BorderItemSelectionEditPolicy() {
//
//							@Override
//							protected List<?> createSelectionHandles() {
//								MoveHandle mh = new MoveHandle((GraphicalEditPart) getHost());
//								mh.setBorder(null);
//								return Collections.singletonList(mh);
//							}
//						};
//					}
//				}
//				EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
//				if (result == null) {
//					result = new NonResizableEditPolicy();
//				}
//				return result;
//			}
//
//			@Override
//			protected Command getMoveChildrenCommand(Request request) {
//				return null;
//			}
//
//			@Override
//			protected Command getCreateCommand(CreateRequest request) {
//				return null;
//			}
//		};
//		return lep;
//	}
//
//	/**
//	 * Papyrus codeGen
//	 *
//	 * @generated
//	 **/
//	@Override
//	protected void handleNotificationEvent(Notification event) {
//		/*
//		 * when a node have external node labels, the methods refreshChildren() remove the EditPart corresponding to the Label from the EditPart
//		 * Registry. After that, we can't reset the visibility to true (using the Show/Hide Label Action)!
//		 */
//		if (NotationPackage.eINSTANCE.getView_Visible().equals(event.getFeature())) {
//			Object notifier = event.getNotifier();
//			List<?> modelChildren = ((View) getModel()).getChildren();
//			if (false == notifier instanceof Edge
//					&& false == notifier instanceof BasicCompartment) {
//				if (modelChildren.contains(event.getNotifier())) {
//					return;
//				}
//			}
//		}
//		super.handleNotificationEvent(event);
//
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	protected IFigure createNodeShape() {
//		return primaryShape = new DiagramNodeFigure();
//	}
//
//	/**
//	 * org.eclipse.papyrus.uml.diagram.common.figure.node.DiagramNodeFigure
//	 *
//	 * @generated
//	 */
//	@Override
//	public DiagramNodeFigure getPrimaryShape() {
//		return (DiagramNodeFigure) primaryShape;
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	protected void addBorderItem(IFigure borderItemContainer, IBorderItemEditPart borderItemEditPart) {
//		if (borderItemEditPart instanceof DiagramNameEditPart) {
//			BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.SOUTH);
//			locator.setBorderItemOffset(new Dimension(-20, -20));
//			borderItemContainer.add(borderItemEditPart.getFigure(), locator);
//		} else {
//			super.addBorderItem(borderItemContainer, borderItemEditPart);
//		}
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	protected NodeFigure createNodePlate() {
//		RoundedRectangleNodePlateFigure result = new RoundedRectangleNodePlateFigure(20, 20);
//		return result;
//	}
//
//	/**
//	 * Creates figure for this edit part.
//	 *
//	 * Body of this method does not depend on settings in generation model
//	 * so you may safely remove <i>generated</i> tag and modify it.
//	 *
//	 * @generated
//	 */
//	@Override
//	protected NodeFigure createMainFigure() {
//		return new SelectableBorderedNodeFigure(createMainFigureWithSVG());
//
//	}
//
//	/**
//	 * Default implementation treats passed figure as content pane.
//	 * Respects layout one may have set for generated figure.
//	 *
//	 * @param nodeShape
//	 *            instance of generated figure class
//	 * @generated
//	 */
//	@Override
//	protected IFigure setupContentPane(IFigure nodeShape) {
//		return nodeShape; // use nodeShape itself as contentPane
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	public IFigure getContentPane() {
//		if (contentPane != null) {
//			return contentPane;
//		}
//		return super.getContentPane();
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	protected void setForegroundColor(Color color) {
//		if (primaryShape != null) {
//			primaryShape.setForegroundColor(color);
//		}
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	protected void setLineWidth(int width) {
//		super.setLineWidth(width);
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	protected void setLineType(int style) {
//		if (primaryShape instanceof IPapyrusNodeFigure) {
//			((IPapyrusNodeFigure) primaryShape).setLineStyle(style);
//		}
//	}
//
//	/**
//	 * @generated
//	 */
//	@Override
//	public EditPart getPrimaryChildEditPart() {
//		return getChildBySemanticHint(UMLVisualIDRegistry.getType(DiagramNameEditPart.VISUAL_ID));
//	}
//
}

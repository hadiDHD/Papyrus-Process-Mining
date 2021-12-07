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

import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.util.DiagramRenderUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.gmfdiag.common.editpolicies.PapyrusPopupBarEditPolicy;
import org.eclipse.papyrus.infra.internationalization.utils.utils.LabelInternationalization;
import org.eclipse.papyrus.uml.sirius.common.diagram.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * This class is used to open a popup containing a preview of the diagram shortcut
 * when the mouse hovers over the shortcut
 *
 */
public class SiriusShortCutPreviewEditPolicy extends PapyrusPopupBarEditPolicy {
	private final double SCALE_FACTOR = 0.5;
	private int CUSTOM_ITEM_WIDTH;
	private int CUSTOM_ITEM_HEIGHT;

	private Diagram diagram;
	private Image previewImage;
	private Image scaledPreviewImage;
	private int maxHeight;
	private int maxWidth;

	public SiriusShortCutPreviewEditPolicy() {
		super();
		maxHeight = 0;
		maxWidth = 0;
	}

	@Override
	public void activate() {
		super.activate();
		if (getHost() instanceof GraphicalEditPart) {
			if (((GraphicalEditPart) getHost()).getNotationView() != null
					&& ((GraphicalEditPart) getHost()).getNotationView().getElement() instanceof Diagram) {
				diagram = (Diagram) ((GraphicalEditPart) getHost()).getNotationView().getElement();
			}
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();
		if (previewImage != null) {
			previewImage.dispose();
		}

		if (scaledPreviewImage != null) {
			scaledPreviewImage.dispose();
		}
	}

	@Override
	protected void showDiagramAssistant(Point referencePoint) {
		if (diagram != null) {
			if (previewImage == null) {
				try {
					previewImage = DiagramRenderUtil.renderToSWTImage(diagram);
				} catch (Exception e) {
					Activator.log.error(e);
				}
			} else {
				int optimalWidth = (int) (this.getHost().getRoot().getViewer().getControl().getBounds().width * SCALE_FACTOR);
				int optimalHeight = (int) (this.getHost().getRoot().getViewer().getControl().getBounds().height * SCALE_FACTOR);

				if (scaledPreviewImage == null || optimalWidth != maxWidth || optimalHeight != maxHeight) {
					maxHeight = optimalHeight;
					maxWidth = optimalWidth;
					if (scaledPreviewImage != null) {
						scaledPreviewImage.dispose();
					}
					scaledPreviewImage = resize(previewImage, maxHeight, maxWidth);
				}

				CUSTOM_ITEM_WIDTH = scaledPreviewImage.getBounds().width;
				CUSTOM_ITEM_HEIGHT = scaledPreviewImage.getBounds().height;

				super.showDiagramAssistant(referencePoint);
			}
		}
	}

	/**
	 * initialize the popup bars from the list of action descriptors.
	 */
	@Override
	protected void initPopupBars() {

		List<PopupBarDescriptor> theList = getPopupBarDescriptors();
		if (theList.isEmpty()) {
			return;
		}
		myBalloon = createPopupBarFigure();

		int iTotal = CUSTOM_ITEM_WIDTH * theList.size() + ACTION_MARGIN_RIGHT;

		getBalloon().setSize(
				iTotal,
				CUSTOM_ITEM_HEIGHT + 2 * ACTION_BUTTON_START_Y);

		int xLoc = ACTION_BUTTON_START_X;
		int yLoc = ACTION_BUTTON_START_Y;

		for (PopupBarDescriptor theDesc : theList) {
			// Button b = new Button(theDesc.myButtonIcon);
			PreviewPopupBarLabelHandle b = new PreviewPopupBarLabelHandle(theDesc.getIcon());

			Rectangle r1 = new Rectangle();
			r1.setLocation(xLoc, yLoc);
			xLoc += CUSTOM_ITEM_WIDTH;
			r1.setSize(CUSTOM_ITEM_WIDTH, CUSTOM_ITEM_HEIGHT - ACTION_MARGIN_RIGHT);

			Label l = new Label();
			l.setText(theDesc.getToolTip());

			b.setToolTip(l);
			b.setPreferredSize(CUSTOM_ITEM_WIDTH, CUSTOM_ITEM_HEIGHT);
			b.setBounds(r1);

			getBalloon().add(b);

			b.addMouseMotionListener(this);
			b.addMouseListener(this.myMouseKeyListener);
		}
	}

	@Override
	protected void appendPopupBarDescriptors() {
		addPopupBarDescriptor(null, scaledPreviewImage, null, "Preview of " + LabelInternationalization.getInstance().getDiagramLabel(diagram)); // IElementType, Image, DragTracker, String
	}

	private Image resize(Image image, int maxWidth, int maxHeight) {
		double widthD = image.getBounds().width;
		double heightD = image.getBounds().height;
		double maxWidthD = maxWidth;
		double maxHeightD = maxHeight;

		if (widthD > maxWidthD || heightD > maxHeightD) {
			Double scale = 1.0;

			if (widthD > maxWidthD && heightD > maxHeightD) {
				if (widthD >= heightD) {
					scale = maxWidthD / widthD;
				} else {
					scale = maxHeightD / heightD;
				}
			} else {
				if (widthD > maxWidthD) {
					scale = maxWidthD / widthD;
				} else {
					scale = maxHeightD / heightD;
				}
			}

			int scaledWidth = (int) (widthD * scale);
			int scaledHeight = (int) (heightD * scale);

			Image scaled = new Image(Display.getDefault(), scaledWidth, scaledHeight);
			GC gc = new GC(scaled);
			gc.setAntialias(SWT.ON);
			gc.setInterpolation(SWT.HIGH);
			gc.drawImage(image, 0, 0,
					image.getBounds().width, image.getBounds().height,
					0, 0, scaledWidth, scaledHeight);
			gc.dispose();

			return scaled;
		} else {
			return image;
		}
	}

	/**
	 * The preview image handler
	 *
	 */
	private class PreviewPopupBarLabelHandle extends PopupBarLabelHandle {
		public PreviewPopupBarLabelHandle(Image theImage) {
			super(null, theImage);
		}

		/**
		 * @see org.eclipse.draw2d.IFigure#handleMouseEntered(org.eclipse.draw2d.MouseEvent)
		 *      flip myMouseOver bit and repaint
		 */
		@Override
		public void handleMouseEntered(MouseEvent event) {
			calculateEnabled();
			super.handleMouseEntered(event);
			myMouseOver = true;
		}

		/**
		 * @see org.eclipse.draw2d.IFigure#handleMouseExited(org.eclipse.draw2d.MouseEvent)
		 *      flip myMouseOver bit and repaint
		 */
		@Override
		public void handleMouseExited(MouseEvent event) {
			super.handleMouseExited(event);
			myMouseOver = false;
		}
	}
}

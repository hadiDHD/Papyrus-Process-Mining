/*****************************************************************************
 * Copyright (c) 2011, 2022  CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Amine EL KOUHEN (CEA LIST/LIFL) - Amine.Elkouhen@cea.fr
 *  Ansgar Radermacher (CEA LIST) - ansgar.radermacher@cea.fr
 *  Vincent Lorenzo (CEA LIST) - vincent.lorenzo@cea.fr - bug 578383
 *****************************************************************************/
package org.eclipse.papyrus.sirius.editor.internal.decorations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IPrimaryEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.Decoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.papyrus.infra.services.decoration.util.IPapyrusDecoration;
import org.eclipse.papyrus.sirius.editor.Activator;
import org.eclipse.swt.graphics.Image;


/**
 * TODO : this code should be moved in another plugin in order to be able to use decoration/validation in a pure Sirius Diagram context
 *
 * Duplicated and adapter from org.eclipse.papyrus.uml.diagram.common.providers.DiagramDecorationAdapter
 * The Class DiagramDecorationAdapter.
 */
public class DiagramDecorationAdapter {

	/** The decorator target. */
	protected IDecoratorTarget decoratorTarget;

	/** The decoration. */
	final protected List<IDecoration> decorations = new ArrayList<>();

	/**
	 * Instantiates a new diagram decoration adapter.
	 *
	 * @param decoratorTarget
	 *            the decorator target
	 */
	public DiagramDecorationAdapter(IDecoratorTarget decoratorTarget) {
		this.decoratorTarget = decoratorTarget;
	}

	/**
	 * Gets the decorator target.
	 *
	 * @return the decorator target
	 */
	public IDecoratorTarget getDecoratorTarget() {
		return this.decoratorTarget;
	}

	/**
	 * Removes the decoration.
	 */
	public void removeDecorations() {
		if ((decoratorTarget == null) || (decorations == null)) {
			return;
		}
		for (IDecoration decoration : decorations) {
			removeDecoration(decoration);
		}

	}

	/**
	 * Sets the decorator target.
	 *
	 * @param decoratorTarget
	 *            the new decorator target
	 */
	public void setDecoratorTarget(IDecoratorTarget decoratorTarget) {
		this.decoratorTarget = decoratorTarget;
	}

	/**
	 * Gets the tool tip.
	 *
	 * @param message
	 *            the message
	 * @return the tool tip
	 */
	public final Label getToolTip(String message) {
		return new Label(message);
	}

	/**
	 * Sets the decorations for a node.
	 *
	 * @param pDecoration
	 *            A set of Papyrus decorations
	 * @param margin
	 *            the margin from the right corner
	 * @param isVolatile
	 *            the is volatile
	 * @return the set of JFace decorations
	 */
	public final List<IDecoration> setDecorationsNode(List<IPapyrusDecoration> pDecorations, int margin, boolean isVolatile) {

		decorations.clear();
		for (IPapyrusDecoration pDecoration : pDecorations) {
			Image image = addDecorationNode(pDecoration, margin, isVolatile);
			if (image != null) {
				margin += image.getBounds().width;
			}
		}
		return decorations;
	}

	/**
	 * Sets the decorations on an edge.
	 *
	 * @param pDecoration
	 *            A set of Papyrus decorations
	 * @param percentageFromSource
	 *            the distance in percent from source node. 50 indicates in the middle
	 *            between source and target
	 * @param isVolatile
	 *            the is volatile
	 * @return the set of JFace decorations
	 */
	public final List<IDecoration> setDecorationsEdge(List<IPapyrusDecoration> pDecorations, int percentageFromSource, boolean isVolatile) {

		final int distBetweenIconsPercent = 20;
		final int percentageMin = 10;
		final int percentageMax = 90;

		decorations.clear();
		if (pDecorations.size() > 1) {
			percentageFromSource -= distBetweenIconsPercent / 2 * (pDecorations.size() - 1);
		}
		if (percentageFromSource < percentageMin) {
			percentageFromSource = percentageMin;
		}
		for (IPapyrusDecoration pDecoration : pDecorations) {
			addDecoration(pDecoration, percentageFromSource, 0, isVolatile);
			percentageFromSource += distBetweenIconsPercent;
			if (percentageFromSource > percentageMax) {
				percentageFromSource = percentageMax;
			}
		}
		// this.pDecorations = pDecorations;

		return decorations;
	}

	/**
	 * Add a decoration to a node. This function can also be used to add decorations incrementally (see bug 400593)
	 *
	 * @param pDecoration
	 *            the Papyrus decoration
	 * @param margin
	 *            The margin from the right corner. If -1, the proper position left of other icons is calculated
	 * @param isVolatile
	 *            whether volatile (typically true)
	 * @return
	 */
	public final Image addDecorationNode(IPapyrusDecoration pDecoration, int margin, boolean isVolatile) {

		if (margin == -1) {
			margin = 0;
			// calc distance from right margin, if not given.
			for (IDecoration decoration : decorations) {
				if (decoration instanceof Decoration) {
					margin += ((Decoration) decoration).getBounds().width;
				}
			}
		}
		return addDecoration(pDecoration, 0, margin, true);
	}

	public final Image addDecoration(IPapyrusDecoration pDecoration, int percentageFromSource, int margin, boolean isVolatile) {
		// use image registry, see bug 401056
		Image image = getPluginIconImage(Activator.PLUGIN_ID, pDecoration.getDecorationImageForGE());
		IDecoration decoration = createDecorationImage(decoratorTarget, image, percentageFromSource, margin, isVolatile);
		if (decoration != null) {
			decorations.add(decoration);
			String message = pDecoration.getMessage();
			Label toolTip = getToolTip(message);
			if (decoration instanceof Decoration) {
				((Decoration) decoration).setToolTip(toolTip);
			}
			return image;
		} else {
			return null;
		}
	}

	/**
	 * This method returns an <code>org.eclipse.swt.graphics.Image</code> identified by its pluginId and image Descriptor.<BR>
	 *
	 * By default, it returns a default image. This image is the image placed in
	 * the directory <em>resources/icons/default.gif</em>
	 *
	 * @param pluginId
	 *            id of plugin
	 * @param descriptor
	 *            the image descriptor of the image
	 * @return the Image
	 */
	private static final Image getPluginIconImage(String pluginId, ImageDescriptor descriptor) {
		String key = pluginId + descriptor;
		ImageRegistry registry = Activator.getDefault().getImageRegistry();
		Image image = registry.get(key);

		if (image == null) {

			registry.put(key, descriptor);
			image = registry.get(key);

		}
		return image;
	}



	/**
	 * Removes a decoration.
	 *
	 * @param decoratorTarget
	 *            the decorator target
	 * @param Decoration
	 *            the decoration
	 */
	public final void removeDecoration(IDecoration decoration) {
		if ((decoration == null) || (decoratorTarget == null)) {
			return;
		}

		if (decoration instanceof IFigure) {
			IFigure parent = ((IFigure) decoration).getParent();
			if (parent != null) {
				parent.remove((IFigure) decoration);
			}
		}
		// decorations.remove(decoration);
		GraphicalEditPart ownerEditPart = decoratorTarget.getAdapter(GraphicalEditPart.class);
		if (ownerEditPart.getViewer() != null) {
			ownerEditPart.getViewer().getVisualPartMap().remove(decoration);
		}
	}

	/**
	 * Create a decoration based on an image
	 *
	 * @param decoratorTarget
	 *            the decorator target
	 * @param image
	 *            the image
	 * @param position
	 *            the position
	 * @param percentageFromSource
	 *            the percentage from source, only evaluated for edge decorations
	 * @param margin
	 *            the margin in pixels from the target position.
	 * @See org.eclipse.gmf.runtime.diagram.ui.services.decorator.addShapeDecoration:
	 *      The margin is the space, in himetric units, between the
	 *      shape's edge and the decoration. A positive margin will place
	 *      the figure outside the shape, a negative margin will place the
	 *      decoration inside the shape.
	 * @param isVolatile
	 *            the is volatile
	 * @return the decoration
	 */
	public final IDecoration createDecorationImage(IDecoratorTarget decoratorTarget, Image image, int percentageFromSource, int margin, boolean isVolatile) {
		final View view = decoratorTarget.getAdapter(View.class);
		org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration decoration = null;
		if (view == null || view.eResource() == null || image == null) {
			return decoration;
		}
		EditPart editPart = decoratorTarget.getAdapter(EditPart.class);
		if (editPart == null || editPart.getViewer() == null) {
			return decoration;
		}
		if (view instanceof Edge) {

			decoration = decoratorTarget.addConnectionDecoration(image,
					percentageFromSource, isVolatile);

		} else {

			IPrimaryEditPart primaryEditPart = getPrimaryEditPart(editPart);

			if (primaryEditPart != null && primaryEditPart instanceof GraphicalEditPart) {
				IFigure parentFig = ((GraphicalEditPart) primaryEditPart).getFigure();

				// only support figures with handle bounds
				Locator locator = new MultiIconTopRightLocator(parentFig,
						margin);

				// Direction NORTH_EAST will be ignored, since we impose
				// "our" locator below
				decoration = decoratorTarget.addShapeDecoration(image,
						IDecoratorTarget.Direction.NORTH_EAST, margin,
						isVolatile);
				if (decoration instanceof Decoration) {
					((Decoration) decoration).setLocator(locator);
				}
			}
		}
		return decoration;
	}

	/**
	 * Steps up the parent EditPart hierarchy until the parent is type of an {@link IPrimaryEditPart}. If the given EditPart is already a {@link IPrimaryEditPart} it is directly returned.
	 *
	 * @param editPart
	 *            - EditPart to start from
	 * @return The EditPart directly or one of the parent EditParts or {@code NULL} if none of the parents match IPrimaryEditPart
	 */
	protected IPrimaryEditPart getPrimaryEditPart(EditPart editPart) {
		IPrimaryEditPart ret = null;
		if (editPart instanceof IPrimaryEditPart) {
			return (IPrimaryEditPart) editPart;
		} else if (editPart.getParent() != null) {
			ret = getPrimaryEditPart(editPart.getParent());
		}
		return ret;
	}

	/**
	 * Create a decoration based on a figure
	 *
	 * @param decoratorTarget
	 *            the decorator target
	 * @param figure
	 *            the figure
	 * @param position
	 *            the position
	 * @param percentageFromSource
	 *            the percentage from source
	 * @param margin
	 *            the margin
	 * @param isVolatile
	 *            the is volatile
	 * @return the decoration
	 */
	public final IDecoration createDecorationFigure(IDecoratorTarget decoratorTarget, IFigure figure, int percentageFromSource, int margin, boolean isVolatile) {

		final View view = decoratorTarget.getAdapter(View.class);
		org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration decoration = null;
		if (view == null || view.eResource() == null || figure == null) {
			return decoration;
		}
		EditPart editPart = decoratorTarget.getAdapter(EditPart.class);
		if (editPart == null || editPart.getViewer() == null) {
			return decoration;
		}
		if (editPart instanceof GraphicalEditPart) {
			if (view instanceof Edge) {
				decoration = decoratorTarget.addConnectionDecoration(figure, percentageFromSource, isVolatile);
			} else {
				IFigure parentFig = ((GraphicalEditPart) editPart).getFigure();
				margin = MapModeUtil.getMapMode(parentFig).DPtoLP(margin);

				// Locator locator = new MultiIconTopRightLocator(parentFig, margin);

				// decoration = decoratorTarget.addDecoration(figure, locator, isVolatile);
				decoration = decoratorTarget.addShapeDecoration(figure, IDecoratorTarget.Direction.NORTH_EAST, margin, isVolatile);
			}

		}
		return decoration;
	}

	/**
	 * A locator that places elements to the upper right corner with a "margin" distance to the left.
	 *
	 * @See org.eclipse.gmf.runtime.gef.ui.internal.figures.RelativetoBorderLocator
	 * @author ansgar
	 *
	 */
	public class MultiIconTopRightLocator implements Locator {

		public MultiIconTopRightLocator(IFigure parentFig, int rightMargin) {
			this.reference = parentFig;
			if (rightMargin < 0) {
				// avoid negative right margin. This could imply that the decorator enlarges the reference figure
				// which in turn might re-position the decorator to the right and thus causing an enlargement in
				// an endless loop
				rightMargin = 0;
			}
			this.rightMargin = rightMargin;
		}

		@Override
		public void relocate(IFigure target) {
			Rectangle bounds;
			if (reference instanceof HandleBounds) {
				bounds = new PrecisionRectangle(((HandleBounds) reference).getHandleBounds());
			} else {
				bounds = new PrecisionRectangle(reference.getBounds());
				// authorized rectangle without handle bounds is smaller: if the icon would be exactly on one of the corners,
				// it would trigger an enlargement of the figure, followed by a reposition, i.e. an endless loop.
				bounds.x++;
				bounds.y++;
				bounds.width -= 2;
				bounds.height -= 2;
			}

			reference.translateToAbsolute(bounds);
			target.translateToRelative(bounds);

			Point pTR = bounds.getTopRight();
			Point pTL = bounds.getTopLeft();
			Point pDecoration = new Point(pTR);

			int decorationX = pTR.x - target.getSize().width - rightMargin;
			if (decorationX > pTL.x) {
				// only set position, if it is inside the figure, i.e. bigger than left margin
				pDecoration.setX(decorationX);
			} else {
				pDecoration.setX(pTL.x);
			}
			target.setLocation(pDecoration);
		}

		protected IFigure reference;

		protected int rightMargin;
	};
}

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

import java.util.Collection;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.sirius.sequence.diagram.utils.ReorderSequenceRegistry;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider;
import org.eclipse.sirius.diagram.description.filter.FilterDescription;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.diagram.ui.internal.refresh.GMFHelper;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.diagram.ui.tools.internal.handler.ChangeFilterActivation;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.sirius.viewpoint.description.DescriptionFactory;

/**
 * The Class RefreshSequenceExtensionProvider.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 * @author Aurélien Didier (Artal Technologies) <aurelien.didier51@gmail.com>
 */
public class RefreshSequenceExtensionProvider implements IRefreshExtensionProvider {

	private final static String observationPointFilterName = "Hide Observation Point";
	private final static String observationPointFilterAnnotationName = "Observation Point Filter Deactivation";
	private final static String diagramID = "SequenceDiagram";
	private final static String diagramEditorID = "org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID";

	/**
	 * Constructor.
	 *
	 */
	public RefreshSequenceExtensionProvider() {
	}

	/**
	 * Provides Refresh Extension for Sequence Diagram.
	 *
	 * @param viewPoint the view point
	 * @return true, if successful
	 * @see org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider#provides(org.eclipse.sirius.diagram.DDiagram)
	 */

	@Override
	public boolean provides(DDiagram viewPoint) {
		return viewPoint.getDescription().getName().equals(diagramID);
	}

	/**
	 * Gets the refresh extension.
	 *
	 * @param viewPoint the view point
	 * @return the refresh extension
	 * @see org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider#getRefreshExtension(org.eclipse.sirius.diagram.DDiagram)
	 */

	@Override
	public IRefreshExtension getRefreshExtension(DDiagram viewPoint) {
		return new IRefreshExtension() {

			@Override
			public void postRefresh(DDiagram dDiagram) {
				ReorderSequenceRegistry.getInstance().clear();
			}

			@Override
			public void beforeRefresh(DDiagram dDiagram) {
				if (!isActivatedOnce(observationPointFilterName, dDiagram)) {
					activateFilterOnFirstOpening(dDiagram);
				}
			}

		};
	}

	/**
	 * Searches the given {@link DDiagram} for a filter of the given name and
	 * returns it.
	 * 
	 * @param diagram    The diagram to search for a tool.
	 * @param filterName The name of the searched filter.
	 * @return The retrieved filter, or <code>null</code> if it cannot be found.
	 */

	public void activateFilterOnFirstOpening(DDiagram obj) {
		if (obj instanceof DDiagram) {
			DDiagram diagram = (DDiagram) obj;
			// Get the filter to activate
			if (!diagram.getDiagramElements().isEmpty()) {
				DDiagramElement elem = diagram.getDiagramElements().get(0);
				View view = SiriusGMFHelper.getGmfView(elem);
				if (view != null) {
					GraphicalEditPart currentEditPart = GMFHelper.getGraphicalEditPart(view).get();
					DDiagramEditor editor = (DDiagramEditor) currentEditPart.getViewer().getProperty(diagramEditorID);

					final FilterDescription filter = getFilter(diagram, observationPointFilterName);
					if (filter != null) {

						// Add annotation
						getAnnotation(observationPointFilterAnnotationName, diagram, true);

						// Activate filter
						final Runnable change = new ChangeFilterActivation((IDiagramWorkbenchPart) editor, diagram,
								filter, true);
						change.run();
					}
				}
			}
		}
	}

	/**
	 * Searches the given {@link DDiagram} for a filter of the given name and
	 * returns it.
	 * 
	 * @param diagram    The diagram to search for a tool.
	 * @param filterName The name of the searched filter.
	 * @return The retrieved filter, or <code>null</code> if it cannot be found.
	 */
	protected final FilterDescription getFilter(final DDiagram diagram, final String filterName) {
		final Collection<FilterDescription> filters = diagram.getDescription().getFilters();

		for (final FilterDescription filter : filters) {
			if (filter.getName().equals(filterName)) {
				return filter;
			}
		}
		return null;
	}

	/**
	 * To know if the filter has been activated once this way.
	 */
	public static boolean isActivatedOnce(String filterId, DDiagram diagram) {
		if (diagram != null) {
			DAnnotation annotation = getAnnotation(observationPointFilterAnnotationName, diagram, false);
			if (annotation != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create a DAnnotation with a given source on a given model element
	 * 
	 * @param source
	 * @param diagram
	 */
	public static DAnnotation createAnnotation(final String source, DDiagram diagram) {
		DAnnotation annotation = DescriptionFactory.eINSTANCE.createDAnnotation();
		annotation.setSource(source);
		diagram.getEAnnotations().add(annotation);
		return annotation;
	}

	/**
	 * Get the first annotation with a given source from a given model element,
	 * optionally creating the annotation if none previously existed.
	 * 
	 * @param source
	 * @param diagram
	 * @param create
	 */
	public static DAnnotation getAnnotation(String source, DDiagram diagram, boolean create) {
		for (DAnnotation annotation : diagram.getEAnnotations()) {
			if (annotation.getSource() != null && annotation.getSource().equals(source)) {
				return annotation;
			}
		}

		if (create) {
			return createAnnotation(source, diagram);
		}

		return null;
	}

	/**
	 * Returns whether the element has the given element
	 * 
	 * @param source
	 * @param representation
	 */
	public static boolean hasAnnotation(String source, DDiagram representation) {
		return getAnnotation(source, representation, false) != null;
	}

}

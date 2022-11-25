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
package org.eclipse.papyrus.sirius.uml.diagram.statemachine;

import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.diagram.ContainerLayout;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider;
import org.eclipse.sirius.diagram.description.DiagramDescription;

/**
 * The Class StateMachineRefreshExtensionProvider allows to implement specific refresh in the StateMachine diagram case.
 */
public class StateMachineRefreshExtensionProvider implements IRefreshExtensionProvider {

	/**
	 * Instantiates a new refresh extension provider 1.
	 */
	public StateMachineRefreshExtensionProvider() {
	}

	/**
	 * Provides.
	 *
	 * @param viewPoint
	 *            the view point
	 * @return true, if successful
	 * @see org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider#provides(org.eclipse.sirius.diagram.DDiagram)
	 */

	@Override
	public boolean provides(DDiagram viewPoint) {
		DiagramDescription description = viewPoint.getDescription();
		String name = description.getName();

		return name.equals("StateMachineDiagram");
	}

	/**
	 * Gets the refresh extension.
	 *
	 * @param viewPoint
	 *            the view point
	 * @return the refresh extension
	 * @see org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtensionProvider#getRefreshExtension(org.eclipse.sirius.diagram.DDiagram)
	 */

	@Override
	public IRefreshExtension getRefreshExtension(DDiagram viewPoint) {

		return new IRefreshExtension() {

			@Override
			public void postRefresh(DDiagram dDiagram) {

				manageRegionPresentation();

				manageStatePresentation(dDiagram);


			}

			@Override
			public void beforeRefresh(DDiagram dDiagram) {
				registeredRegionPresentation(dDiagram);
			}


		};
	}

	/**
	 *
	 * This method allows to get the ContainerLayout values save in memory
	 * and apply them at the Region diagram elements.
	 * In the goal to keep the ContainerLayout values from bedore refresh.
	 */
	private void manageRegionPresentation() {
		for (Entry<DNodeContainer, ContainerLayout> entry : ElementToRefresh.map.entrySet()) {
			DNodeContainer key = entry.getKey();
			ContainerLayout value = entry.getValue();
			key.setChildrenPresentation(value);

		}
		ElementToRefresh.map.clear();
	}

	/**
	 * This method allows to get the ContainerLayout values save in memory
	 * and apply them at the State diagram elements.
	 * In the goal to keep the ContainerLayout values from bedore refresh.
	 *
	 * @param dDiagram
	 *            the d diagram
	 */
	private void manageStatePresentation(DDiagram dDiagram) {
		DSemanticDiagram di = (DSemanticDiagram) dDiagram;
		EList<DDiagramElement> diagramElements = di.getDiagramElements();
		for (DDiagramElement dDiagramElement : diagramElements) {
			if (dDiagramElement instanceof DNodeContainer) {
				if (((DNodeContainer) dDiagramElement).getActualMapping().getName().equals("SMD_State")) {
					if (((DNodeContainer) dDiagramElement).getElements().size() == 0) {
						((DNodeContainer) dDiagramElement).setChildrenPresentation(ContainerLayout.FREE_FORM);
					} else {
						((DNodeContainer) dDiagramElement).setChildrenPresentation(ContainerLayout.VERTICAL_STACK);
					}
				}
			}
		}
	}

	/**
	 * Registered region presentation. This method allows to save in memory the ContainerLayout value of the Diagram element before refresh. (the Sirius generic refresh can modify this value)
	 *
	 * @param dDiagram
	 *            the d diagram
	 */
	private void registeredRegionPresentation(DDiagram dDiagram) {
		EList<DDiagramElement> diagramElements = dDiagram.getDiagramElements();
		for (DDiagramElement dDiagramElement : diagramElements) {
			if (dDiagramElement instanceof DNodeContainer) {
				ContainerLayout childrenPresentation = ((DNodeContainer) dDiagramElement)
						.getChildrenPresentation();
				ElementToRefresh.map.put((DNodeContainer) dDiagramElement, childrenPresentation);
			}
		}
	}
}

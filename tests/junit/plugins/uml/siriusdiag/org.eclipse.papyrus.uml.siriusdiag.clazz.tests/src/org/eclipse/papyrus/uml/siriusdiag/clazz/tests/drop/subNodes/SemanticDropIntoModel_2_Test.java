/*****************************************************************************
 * Copyright (c) 2022 CEA LIST
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Vincent Lorenzo (CEA LIST) <vincent.lorenzo@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramConstants;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.MappingTypes;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.SemanticDropToolsIds;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_ClassCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical.CD_PackageCreationChecker;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.SemanticDropChecker;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DragAndDropTarget;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the semantic drop into a CD_Package
 */
@PluginResource("resources/drop/subNode/dropIntoModel.di")
public class SemanticDropIntoModel_2_Test extends AbstractSubNodeSemanticDropTests {

	private static final String DIAGRAM_NAME = "ClassDiagram"; //$NON-NLS-1$

	private static final String SEMANTIC_OWNER_NAME = "Model";//$NON-NLS-1$

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.drop.subNodes.AbstractSemanticDropSubNodeTests#getSemanticOwner()
	 *
	 * @return
	 */

	// @Override
	protected Model getSemanticOwner() {
		return (org.eclipse.uml2.uml.Model) this.root.getMember(SEMANTIC_OWNER_NAME);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void dropPackageIntoModel() {
		final NamedElement toDrop = getSemanticOwner().getMember("PackageToDrop"); //$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__PACKAGE__TOOL, new CD_PackageCreationChecker(diagram, getTopNodeGraphicalContainer()), new SemanticDropChecker(toDrop), toDrop);
	}

	@Test
	@ActiveDiagram(DIAGRAM_NAME)
	public void dropClassIntoModel() {
		final NamedElement toDrop = getSemanticOwner().getMember("ClassToDrop"); //$NON-NLS-1$
		dropNode(SemanticDropToolsIds.DROP__CLASS__TOOL, new CD_ClassCreationChecker(diagram, getTopNodeGraphicalContainer()), new SemanticDropChecker(toDrop), toDrop);
	}

	/**
	 * @return
	 * 
	 */
	protected DragAndDropTarget getTopNodeGraphicalContainer() {
		final Diagram diagram = getClassDiagram();
		Assert.assertEquals("The root model must have only one node element before dropping the sub node", 1, diagram.getChildren().size()); //$NON-NLS-1$
		final Object firstView = diagram.getChildren().get(0);

		Assert.assertTrue(((View) firstView).getElement() instanceof DNodeContainer);
		final DNodeContainer classNode = (DNodeContainer) ((View) firstView).getElement();
		// only one semantic element must be associated to the classNodeContainer
		Assert.assertEquals(1, classNode.getSemanticElements().size());
		Assert.assertEquals(getSemanticOwner(), classNode.getSemanticElements().get(0));
		for (final DDiagramElement diagramElement : classNode.getOwnedDiagramElements()) {
			if (diagramElement instanceof DragAndDropTarget && MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE.equals(diagramElement.getMapping().getName())) {
				return (DragAndDropTarget) diagramElement;
			}
		}
		return classNode;
	}

	protected DragAndDropTarget getRealGraphicalContainer() {
		return null;// TODO : not yet used
	}


	/**
	 * 
	 * @return
	 *         the active Sirius Class Diagram
	 */
	protected final Diagram getClassDiagram() {
		final DiagramEditPart diagramEditpart = fixture.getActiveDiagram();
		Assert.assertNotNull("The diagram edit part has not been found", diagramEditpart); //$NON-NLS-1$
		final Diagram diagram = diagramEditpart.getDiagramView();

		Assert.assertTrue(diagram.getElement() instanceof DSemanticDiagram);

		// check diagram type
		final DSemanticDiagram ddiagram = (DSemanticDiagram) diagram.getElement();
		DAnnotation dAnnotation = ddiagram.getDAnnotation(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_SOURCE);
		String detail = dAnnotation.getDetails().get(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_KEY);
		Assert.assertEquals("org.eclipse.papyrus.sirius.uml.diagram.class", detail); // TODO : create a constant for this field when the code will be refactored

		return diagram;
	}

}

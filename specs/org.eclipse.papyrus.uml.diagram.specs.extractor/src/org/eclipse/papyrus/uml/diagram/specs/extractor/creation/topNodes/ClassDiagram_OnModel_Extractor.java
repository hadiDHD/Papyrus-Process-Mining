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
 *  Ibtihel Khemir (CEA LIST) <ibtihel.khemir@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.gmfdiag.common.service.visualtype.VisualTypeService;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.diagram.clazz.UmlClassDiagramForMultiEditor;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
@PluginResource("resources/creation/ClassDiagram/ClassDiagram_OnModel.di")
public class ClassDiagram_OnModel_Extractor extends AbtractPapyrusDiagramSpecificationsExtractor {

	// just here to force dependency
	@SuppressWarnings("unused")
	private final UmlClassDiagramForMultiEditor editor = null;

	/**
	 * The URI of the palette definition
	 */
	private final URI PALETTE_DEFINITION = URI.createPlatformPluginURI("org.eclipse.papyrus.uml.diagram.clazz/model/PapyrusUMLClassDiagram.paletteconfiguration", false); //$NON-NLS-1$

	private static final String EMPTY__DIAGRAM_NAME = "ClassDiagram_Empty"; //$NON-NLS-1$

	private static final String ALL_ELEMENTS__DIAGRAM_NAME = "ClassDiagram_AllElements"; //$NON-NLS-1$

	private static final String FILE_NAME = "ClassDiagram_Requirements";//$NON-NLS-1$

	/**
	 * Init method
	 */
	@Before
	public void init() {
		init(PALETTE_DEFINITION, FILE_NAME);
	}

	@Test
	@ActiveDiagram(EMPTY__DIAGRAM_NAME) // the name of the empty diagram
	public void playWithModel() {
		// 1. create top nodes and child nodes
		playWithNodes(); // play with the empty diagram

		fixture.closeDiagram();

		fixture.flushDisplayEvents();

		fixture.openDiagram(fixture.getEditor(), ALL_ELEMENTS__DIAGRAM_NAME);

		// 2. create all possibles edges
		playWithEdges();

		// 3. write the excel file
		writeExcelFile();
	}

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#getMetaclassFromEditPart(org.eclipse.gef.EditPart)
	 *
	 * @param ep
	 * @return
	 */
	@Override
	protected String getMetaclassFromEditPart(EditPart ep) {
		String metaclass = super.getMetaclassFromEditPart(ep);
		if (metaclass == null) {
			// TODO create Named association in the tested model to get all kind of association!
			final View model = (View) ep.getModel();
			final EObject element = model.getElement();

			// to be sure to get all cases, we create and named some association in the model
			if (element instanceof Association && ((NamedElement) element).getName() != null) {
				final Association association = (Association) element;
				final String name = association.getName();
				if (name.startsWith("DirectedAssociation")) { //$NON-NLS-1$
					metaclass = "Association (Directed)"; //$NON-NLS-1$
				} else if (name.startsWith("CompositeDirectedAssociation")) { //$NON-NLS-1$
					metaclass = "Composite Association (Directed)"; //$NON-NLS-1$
				} else if (name.startsWith("SharedDirectedAssociation")) { //$NON-NLS-1$
					metaclass = "Shared Association (Directed)"; //$NON-NLS-1$
				} else if (name.startsWith("Association")) {//$NON-NLS-1$
					metaclass = "Association"; //$NON-NLS-1$
				}
			}
		}
		if (metaclass == null) {
			final View model = (View) ep.getModel();
			// we are not able to find the "metaclass" for these elementTypes
			final IElementType res = VisualTypeService.getInstance().getElementType(fixture.getActiveDiagram().getDiagramView(), model.getType());
			if ("org.eclipse.papyrus.umldi.Association_Edge".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Association"; //$NON-NLS-1$
			} else if ("org.eclipse.papyrus.umldi.AssociationClass_TetherEdge".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Association Class"; //$NON-NLS-1$
			} else if ("org.eclipse.papyrus.umldi.AssociationClass_Shape".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Association Class"; //$NON-NLS-1$
			}
		}
		return metaclass;
	}

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#getTopNodeBackgroundTitle()
	 *
	 * @return
	 */
	@Override
	protected String getTopNodeBackgroundTitle() {
		return "Model/Package as background"; //$NON-NLS-1$
	}

}

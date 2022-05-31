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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.gmfdiag.common.service.visualtype.VisualTypeService;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.ChildConfiguration;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.ToolConfiguration;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.diagram.clazz.UmlClassDiagramForMultiEditor;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
@PluginResource("resources/creation/InnerClassDiagram/InnerClassDiagram_OnModel.di")
public class InnerClassDiagram_OnModel_Extractor extends AbtractPapyrusDiagramSpecificationsExtractor {
	// just here to force dependency
	@SuppressWarnings("unused")
	private final UmlClassDiagramForMultiEditor editor = null;

	/**
	 * The URI of the palette definition
	 */
	private final URI PALETTE_DEFINITION = URI.createPlatformPluginURI("org.eclipse.papyrus.uml.diagram.clazz/model/PapyrusUMLClassDiagram.paletteconfiguration", false); //$NON-NLS-1$

	private static final String EMPTY__DIAGRAM_NAME = "InnerClassDiagram_Empty"; //$NON-NLS-1$

	private static final String ALL_ELEMENTS__DIAGRAM_NAME = "InnerClassDiagram_AllElements"; //$NON-NLS-1$

	private static final String FILE_NAME = "InnerClassDiagram_Requirements";//$NON-NLS-1$

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
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#getTopNodeBackgroundTitle()
	 *
	 * @return
	 */
	@Override
	protected String getTopNodeBackgroundTitle() {
		return "Class as background"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#createPaletteReader(org.eclipse.emf.common.util.URI)
	 *
	 * @param paletteURI
	 * @return
	 */

	@Override
	protected PaletteDescriptionReader createPaletteReader(URI paletteURI) {
		return new CustomPaletteDescriptioReader(paletteURI);
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
			final View model = (View) ep.getModel();
			// we are not able to find the "metaclass" for these elementTypes
			final IElementType res = VisualTypeService.getInstance().getElementType(fixture.getActiveDiagram().getDiagramView(), model.getType());
			if ("org.eclipse.papyrus.umldi.Association_Edge".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Association"; //$NON-NLS-1$
			} else if ("org.eclipse.papyrus.umldi.DurationObservation_Shape".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Duration Observation"; //$NON-NLS-1$
			} else if ("org.eclipse.papyrus.umldi.TimeObservation_Shape".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Time Observation"; //$NON-NLS-1$
			}
		}
		return metaclass;
	}

	private static class CustomPaletteDescriptioReader extends PaletteDescriptionReader {

		private static final Collection<String> ALLOWED_TOOLS = new ArrayList<>();


		private static final String COMMENT_TOOL_ID = "clazz.tool.comment";//$NON-NLS-1$
		private static final String CLASS_ID = "clazz.tool.class";//$NON-NLS-1$
		private static final String ABSTRACTION_ID = "clazz.tool.abstraction";//$NON-NLS-1$
		private static final String INTERFACE_ID = "clazz.tool.interface";//$NON-NLS-1$
		private static final String ASSOCIATION_ID = "clazz.tool.association";//$NON-NLS-1$
		private static final String GENERALIZATION_ID = "clazz.tool.generalization";//$NON-NLS-1$
		private static final String GENERALIZATIONSET_ID = "clazz.tool.generalizationset";//$NON-NLS-1$
		static {
			ALLOWED_TOOLS.add(COMMENT_TOOL_ID);
			ALLOWED_TOOLS.add(CLASS_ID);
			ALLOWED_TOOLS.add(ASSOCIATION_ID);
			ALLOWED_TOOLS.add(ABSTRACTION_ID);
			ALLOWED_TOOLS.add(GENERALIZATION_ID);
			ALLOWED_TOOLS.add(INTERFACE_ID);
			ALLOWED_TOOLS.add(GENERALIZATIONSET_ID);
		}

		/**
		 * 
		 * Constructor.
		 *
		 * @param paletteURI
		 *            The URI of the loaded palette
		 */
		public CustomPaletteDescriptioReader(final URI paletteURI) {
			super(paletteURI);
		}

		/**
		 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader#readPaletteDescription()
		 *
		 */

		@Override
		protected void readPaletteDescription() {
			super.readPaletteDescription();
		}

		/**
		 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader#fillMap(org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.ChildConfiguration, java.util.Map)
		 *
		 * @param childConfiguration
		 * @param map
		 */

		@Override
		protected void fillMap(final ChildConfiguration childConfiguration, final Map<String, Collection<String>> map) {
			if (childConfiguration instanceof ToolConfiguration) {
				final ToolConfiguration tmp = (ToolConfiguration) childConfiguration;
				final String id = tmp.getId();
				if (!ALLOWED_TOOLS.contains(id)) {
					return;
				}
			}
			super.fillMap(childConfiguration, map);
		}

		/**
		 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader#fillMapByPaletteID(org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.ChildConfiguration, java.util.Map)
		 *
		 * @param childConfiguration
		 * @param paletteID
		 */

		@Override
		protected void fillMapByPaletteID(final ChildConfiguration childConfiguration, final Map<String, String> paletteID) {
			if (childConfiguration instanceof ToolConfiguration) {
				final ToolConfiguration tmp = (ToolConfiguration) childConfiguration;
				final String id = tmp.getId();
				if (!ALLOWED_TOOLS.contains(id)) {
					return;
				}
			}
			super.fillMapByPaletteID(childConfiguration, paletteID);
		}
	}
}

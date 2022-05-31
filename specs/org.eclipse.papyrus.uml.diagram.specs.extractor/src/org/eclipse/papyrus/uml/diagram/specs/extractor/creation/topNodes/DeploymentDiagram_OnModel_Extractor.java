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
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.diagram.deployment.UmlDeploymentDiagramForMultiEditor;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
@PluginResource("resources/creation/DeploymentDiagram/DeploymentDiagram_OnModel.di")
public class DeploymentDiagram_OnModel_Extractor extends AbtractPapyrusDiagramSpecificationsExtractor {

	// just here to force dependency
	@SuppressWarnings("unused")
	private final UmlDeploymentDiagramForMultiEditor editor = null;

	/**
	 * The URI of the palette definition
	 * 
	 * the 'e' is missing in Papyrus in PapyrusUMLDeploymentDiagram.paletteconfiguration
	 */
	private final URI PALETTE_DEFINITION = URI.createPlatformPluginURI("org.eclipse.papyrus.uml.diagram.deployment/model/PapyrusUMLDeploymentDiagram.paletteconfiguration", false); //$NON-NLS-1$

	private static final String EMPTY__DIAGRAM_NAME = "DeploymentDiagram_Empty"; //$NON-NLS-1$

	private static final String ALL_ELEMENTS__DIAGRAM_NAME = "DeploymentDiagram_AllElements"; //$NON-NLS-1$

	private static final String FILE_NAME = "DeploymentDiagram_Requirements";//$NON-NLS-1$

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
		return "Model/Package as background"; //$NON-NLS-1$
	}
}

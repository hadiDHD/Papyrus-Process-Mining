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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.diagram.communication.UmlCommunicationDiagramForMultiEditor;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.RequirementsExcelWriter;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
@PluginResource("resources/creation/CommunicationDiagram/CommunicationDiagram_OnModel.di")
public class CommunicationDiagram_OnModel_Extractor extends AbtractPapyrusDiagramSpecificationsExtractor {

	// just here to force dependency
	@SuppressWarnings("unused")
	private final UmlCommunicationDiagramForMultiEditor editor = null;

	/**
	 * The URI of the palette definition
	 */
	private final URI PALETTE_DEFINITION = URI.createPlatformPluginURI("org.eclipse.papyrus.uml.diagram.communication/model/PapyrusUMLCommunicationDiagram.paletteconfiguration", false); //$NON-NLS-1$

	private static final String EMPTY__DIAGRAM_NAME = "CommunicationDiagram_Empty"; //$NON-NLS-1$

	private static final String ALL_ELEMENTS__DIAGRAM_NAME = "CommunicationDiagram_AllElements"; //$NON-NLS-1$

	private static final String FILE_NAME = "CommunicationDiagram_Requirements";//$NON-NLS-1$

	/**
	 * Init method
	 */
	@Before
	public void init() {
		init(PALETTE_DEFINITION, FILE_NAME);
	}

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#createPaletteReader(org.eclipse.emf.common.util.URI)
	 *
	 * @param paletteURI
	 * @return
	 */

	@Override
	protected PaletteDescriptionReader createPaletteReader(URI paletteURI) {
		return new CustomPaletteDescriptionReader(paletteURI);
	}

	/**
	 * A custom Palette reader with he ElementType for Interaction!
	 */
	private class CustomPaletteDescriptionReader extends PaletteDescriptionReader {

		private String INTERACTION_ELEMENT_TYPE = "org.eclipse.papyrus.umldi.Interaction_Shape"; //$NON-NLS-1$

		private String INTERACTION = "Interaction"; //$NON-NLS-1$

		/**
		 * Constructor.
		 *
		 * @param paletteURI
		 */
		public CustomPaletteDescriptionReader(URI paletteURI) {
			super(paletteURI);
			final Collection<String> list = new ArrayList<>();
			list.add(INTERACTION_ELEMENT_TYPE);
			nodesMetaclassesVSelementTypesID.put(INTERACTION, list);

			// nodesByPaletteToolID //not in the palette
			elementTypesVSMetaclass.put(INTERACTION_ELEMENT_TYPE, INTERACTION);

		}



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

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#createRequirementsExcelWriter(java.lang.String, org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader)
	 *
	 * @param filePath
	 * @param paletteReader
	 * @return
	 */

	@Override
	protected RequirementsExcelWriter createRequirementsExcelWriter(String filePath, PaletteDescriptionReader paletteReader) {
		return new RequirementsExcelWriter(filePath, paletteReader) {
			/**
			 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.utils.RequirementsExcelWriter#writePaletteSheet(org.apache.poi.xssf.usermodel.XSSFWorkbook)
			 *
			 * @param workbook
			 * @return
			 */

			@Override
			protected XSSFSheet writePaletteSheet(XSSFWorkbook workbook) {
				// Create a Bold Font
				final XSSFFont boldFont = workbook.createFont();
				boldFont.setFontHeightInPoints((short) 10);
				boldFont.setBold(true);
				boldFont.setFontName(FONT_NAME);
				boldFont.setColor(IndexedColors.RED.getIndex());

				final XSSFCellStyle titleCellStyle = workbook.createCellStyle();
				titleCellStyle.setFont(boldFont);
				titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
				titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				XSSFSheet sheet = super.writePaletteSheet(workbook);


				int nbEdgesElementTypes = 0;
				for (final Collection<IElementType> tmp : getPaletteDescriptionReader().getEdgesElementsTypes().values()) {
					nbEdgesElementTypes += tmp.size();
				}
				int nbNodesElementTypes = 0;
				for (final Collection<IElementType> tmp : getPaletteDescriptionReader().getNodesElementsTypes().values()) {
					nbNodesElementTypes += tmp.size();
				}
				int rowIndex = 1 + nbNodesElementTypes + nbEdgesElementTypes;
				rowIndex++;
				Row additionalRow = sheet.createRow(rowIndex);
				Cell cell = additionalRow.createCell(0);
				cell.setCellValue("Additional Informations"); //$NON-NLS-1$
				cell.setCellStyle(titleCellStyle);

				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));

				rowIndex++;
				additionalRow = sheet.createRow(rowIndex);
				cell = additionalRow.createCell(0);
				cell.setCellValue("The Interaction element is not proposed in this Papyrus Diagram Palette. We add it to get a working specifications extractor!"); //$NON-NLS-1$
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
				return sheet;
			}

		};
	}

}

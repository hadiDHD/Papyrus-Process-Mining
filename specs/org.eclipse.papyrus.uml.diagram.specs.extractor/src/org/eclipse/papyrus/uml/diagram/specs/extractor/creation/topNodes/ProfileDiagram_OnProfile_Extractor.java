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
 *  Ibtihel Khemir (CEA LIST) <ibtihel.khemir@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes;

import java.util.Collection;
import java.util.Collections;

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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.gmfdiag.common.service.visualtype.VisualTypeService;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.diagram.profile.UmlProfileDiagramForMultiEditor;
import org.eclipse.papyrus.uml.diagram.profile.edit.parts.MetaclassEditPart;
import org.eclipse.papyrus.uml.diagram.profile.edit.parts.MetaclassEditPartCN;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.CreateableElementInsideMe;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.RequirementsExcelWriter;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.TopNodeCreationResult;
import org.eclipse.papyrus.uml.diagram.stereotype.edition.editpart.AppliedStereotypeEmptyEditPart;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
@PluginResource("resources/creation/ProfileDiagram/ProfileDiagram_OnProfile.profile.di")
public class ProfileDiagram_OnProfile_Extractor extends AbtractPapyrusDiagramSpecificationsExtractor {

	// just here to force dependency
	@SuppressWarnings("unused")
	private final UmlProfileDiagramForMultiEditor editor = null;

	/**
	 * The URI of the palette definition
	 * 
	 */
	private final URI PALETTE_DEFINITION = URI.createPlatformPluginURI("org.eclipse.papyrus.uml.diagram.profile/model/PapyrusUMLProfileDiagram.paletteconfiguration", false); //$NON-NLS-1$

	private static final String EMPTY__DIAGRAM_NAME = "ProfileDiagram_Empty"; //$NON-NLS-1$

	private static final String ALL_ELEMENTS__DIAGRAM_NAME = "ProfileDiagram_AllElements"; //$NON-NLS-1$

	private static final String FILE_NAME = "ProfileDiagram_Requirements";//$NON-NLS-1$

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
		return "Profile as background"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#createTopNodeEditPart(org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart, org.eclipse.gmf.runtime.emf.type.core.IElementType,
	 *      org.eclipse.draw2d.geometry.Point, org.eclipse.draw2d.geometry.Dimension, org.eclipse.papyrus.uml.diagram.specs.extractor.utils.TopNodeCreationResult)
	 *
	 * @param diagramEditPart
	 * @param elementType
	 * @param location
	 * @param dimension
	 * @param topNodeResult
	 * @return
	 */

	@Override
	protected IGraphicalEditPart createTopNodeEditPart(DiagramEditPart diagramEditPart, IElementType elementType, Point location, Dimension dimension, TopNodeCreationResult topNodeResult) {
		final String IMPORT_METACLASS_ID = "org.eclipse.papyrus.umldi.Class_MetaclassShape"; //$NON-NLS-1$
		if (IMPORT_METACLASS_ID.equals(elementType.getId())) { // open a window
			topNodeResult.registerResult(elementType, true);
			return null;
		}

		return super.createTopNodeEditPart(diagramEditPart, elementType, location, dimension, topNodeResult);
	}

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#createChildNodeOnEditpart(int, org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart,
	 *      org.eclipse.gmf.runtime.emf.type.core.IElementType, org.eclipse.draw2d.geometry.Point, org.eclipse.draw2d.geometry.Dimension, org.eclipse.papyrus.uml.diagram.specs.extractor.utils.CreateableElementInsideMe)
	 *
	 * @param depth
	 * @param parentEditPart
	 * @param elementType
	 * @param location
	 * @param dimension
	 * @param creationResult
	 * @return
	 */

	@Override
	protected Collection<IGraphicalEditPart> createChildNodeOnEditpart(int depth, IGraphicalEditPart parentEditPart, IElementType elementType, Point location, Dimension dimension, CreateableElementInsideMe creationResult) {
		final String IMPORT_METCLASS_CN = "org.eclipse.papyrus.umldi.Class_MetaclassShape_CN"; //$NON-NLS-1$
		final String metaclass = getMetaclassFromEditPart(parentEditPart);
		final String elementTypeID = elementType.getId();

		// all these cases open a Dialog!
		if ("Profile".equals(metaclass) && elementTypeID.equals(IMPORT_METCLASS_CN)) { //$NON-NLS-1$
			final String testedMetaclassCreation = getPaletteDescriptionReader().getMetaclass(elementTypeID);
			creationResult.registerCreatableElement(testedMetaclassCreation, true);
			return Collections.emptyList();
		}


		return super.createChildNodeOnEditpart(depth, parentEditPart, elementType, location, dimension, creationResult);
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
			if (ep instanceof AppliedStereotypeEmptyEditPart) {
				if (ep.getParent() instanceof MetaclassEditPart || ep.getParent() instanceof MetaclassEditPartCN) {
					return "Import Metaclass"; //$NON-NLS-1$
				}
			}
			final View model = (View) ep.getModel();
			// // we are not able to find the "metaclass" for these elementTypes
			final IElementType res = VisualTypeService.getInstance().getElementType(fixture.getActiveDiagram().getDiagramView(), model.getType());
			if ("org.eclipse.papyrus.umldi.Association_Edge".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Association"; //$NON-NLS-1$
			}
		}
		return metaclass;
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
			 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.utils.RequirementsExcelWriter#writeNodesInsideNodes(org.apache.poi.xssf.usermodel.XSSFWorkbook)
			 *
			 * @param workbook
			 */

			@Override
			protected XSSFSheet writeNodesInsideNodes(XSSFWorkbook workbook) {
				final XSSFSheet sheet = super.writeNodesInsideNodes(workbook);

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

				final int rowStart = paletteReader.getMetaclassNodes().size() + 3;
				int rowIndex = rowStart;
				Row additionalRow = sheet.createRow(rowIndex++);
				Cell cell = additionalRow.createCell(0);
				cell.setCellValue("Additional Informations"); //$NON-NLS-1$
				cell.setCellStyle(titleCellStyle);

				additionalRow = sheet.createRow(rowIndex++);
				cell = additionalRow.createCell(0);
				cell.setCellValue("The Import Metaclass tool opens a dialog to select the Metaclass(es) to import."); //$NON-NLS-1$

				rowIndex = rowStart;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));
				rowIndex++;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));

				return sheet;
			}
		};
	}

}

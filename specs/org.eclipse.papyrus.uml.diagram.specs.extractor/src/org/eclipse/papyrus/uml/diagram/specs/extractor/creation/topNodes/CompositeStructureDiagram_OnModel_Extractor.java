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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

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
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.diagram.composite.UmlCompositeDiagramForMultiEditor;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.CreateableElementInsideMe;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.RequirementsExcelWriter;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Before;
import org.junit.Test;

@PluginResource("resources/creation/CompositeStructureDiagram/CompositeStructureDiagram_OnModel.di")
public class CompositeStructureDiagram_OnModel_Extractor extends AbtractPapyrusDiagramSpecificationsExtractor {

	// just here to force dependency
	@SuppressWarnings("unused")
	private final UmlCompositeDiagramForMultiEditor editor = null;

	/**
	 * The URI of the palette definition
	 */
	private final URI PALETTE_DEFINITION = URI.createPlatformPluginURI("org.eclipse.papyrus.uml.diagram.composite/model/CompositeStructure.paletteconfiguration", false); //$NON-NLS-1$

	private static final String EMPTY__DIAGRAM_NAME = "CompositeStructureDiagram_Empty"; //$NON-NLS-1$

	private static final String ALL_ELEMENTS__DIAGRAM_NAME = "CompositeStructureDiagram_AllElements"; //$NON-NLS-1$

	private static final String FILE_NAME = "CompositeStructureDiagram_Requirements";//$NON-NLS-1$

	private static final String PORT_ELEMENT_TYPE_ID = "org.eclipse.papyrus.umldi.Port_Shape"; //$NON-NLS-1$

	private static final String PROPERTY_ELEMENT_TYPE_ID = "org.eclipse.papyrus.umldi.Property_Shape"; //$NON-NLS-1$

	/**
	 * Init method
	 */
	@Before
	public void init() {
		init(this.PALETTE_DEFINITION, FILE_NAME);
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

	private int insidePort = 0;
	private int insideProperty = 0;

	/**
	 * @see org.eclipse.papyrus.uml.diagram.specs.extractor.creation.topNodes.AbtractPapyrusDiagramSpecificationsExtractor#getMaxChildrenDepth()
	 *
	 * @return
	 */

	@Override
	protected int getMaxChildrenDepth() {
		// to keep time, because we observed that with depth=1 and depth=2, the only changes are on Port and Property capabilities
		if (insidePort > getPaletteDescriptionReader().getNodesElementsTypes().size() && insideProperty > getPaletteDescriptionReader().getNodesElementsTypes().size()) {
			return 1;
		}
		return 2;
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
	protected Collection<IGraphicalEditPart> createChildNodeOnEditpart(int depth, final IGraphicalEditPart parentEditPart, final IElementType elementType, final Point location, final Dimension dimension, final CreateableElementInsideMe creationResult) {
		// to allow port creation on port and port creation on property
		final String parentElementType = getElementTypeFromEditPart(parentEditPart).getId();
		if (PORT_ELEMENT_TYPE_ID.equals(parentElementType) || PROPERTY_ELEMENT_TYPE_ID.equals(parentElementType)) {
			final Element el = parentEditPart.getAdapter(Element.class);
			// trick to get time for next generation
			if (el instanceof Port) {
				insidePort++;
			} else if (el instanceof Property) {
				insideProperty++;
			}

			if (el instanceof Property) {
				final Property p = (Property) el;
				if (p.getType() == null) {
					Class type = null;
					final Iterator<PackageableElement> iter = this.fixture.getModel().getPackagedElements().iterator();
					while (iter.hasNext() && type == null) {
						final PackageableElement tmp = iter.next();
						if (tmp instanceof Class) {
							type = (Class) tmp;
						}
					}
					if (type != null) {
						SetCommand cmd = new SetCommand(this.fixture.getEditingDomain(), el, UMLPackage.eINSTANCE.getTypedElement_Type(), type);
						this.fixture.getEditingDomain().getCommandStack().execute(cmd);
						this.fixture.flushDisplayEvents();
					}
				}
			}
		}



		final String parentMetaclass = getMetaclassFromEditPart(parentEditPart);
		// to avoid to open a dialog
		if ("org.eclipse.papyrus.umldi.ConnectableElement_CollaborationRoleShape".equals(elementType.getId()) && "Collaboration".equals(parentMetaclass)) { //$NON-NLS-1$ //$NON-NLS-2$
			final String testedMetaclassCreation = getPaletteDescriptionReader().getMetaclass(elementType.getId());
			creationResult.registerCreatableElement(testedMetaclassCreation, true);
			return Collections.emptyList();
		} else {
			return super.createChildNodeOnEditpart(depth, parentEditPart, elementType, location, dimension, creationResult);
		}
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
	protected RequirementsExcelWriter createRequirementsExcelWriter(final String filePath, final PaletteDescriptionReader paletteReader) {
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
				cell.setCellValue("The CollaborationRole tool opens a dialog to select an existing element."); //$NON-NLS-1$

				additionalRow = sheet.createRow(rowIndex++);
				cell = additionalRow.createCell(0);
				cell.setCellValue("To be able to create a Port on a Port, or a Property or a Port on Property, the semantic parent (Port or Property) must be typed."); //$NON-NLS-1$

				rowIndex = rowStart;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));
				rowIndex++;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));
				rowIndex++;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));


				return sheet;
			}
		};
	}

}

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
package org.eclipse.papyrus.uml.diagram.specs.extractor.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.uml.diagram.specs.extractor.Activator;

public class RequirementsExcelWriter {

	protected static final String NODES_TITLES = "Nodes"; //$NON-NLS-1$

	protected static final String EDGES_TITLES = "Edges"; //$NON-NLS-1$

	/**
	 * Yes value
	 */
	protected static final String YES__VALUE = "YES"; //$NON-NLS-1$

	/**
	 * Yes value for reflexive Link
	 */
	protected static final String YES_R__VALUE = "YES (R) "; //$NON-NLS-1$

	/**
	 * No Value
	 */
	protected static final String NO__VALUE = "NO"; //$NON-NLS-1$

	/**
	 * the name of the font to us
	 */
	protected static final String FONT_NAME = "Arial";//$NON-NLS-1$

	/**
	 * the path of the file to save
	 */
	private final String filePath;

	/**
	 * the description of the palette
	 */
	private PaletteDescriptionReader paletteDescription;

	/**
	 * the result of the node creation as top node
	 */
	private Set<TopNodeCreationResult> topNodeResults;

	/**
	 * the title of the column for nodes on diagram
	 */
	private String topNodeBackgroundTitle;

	/**
	 * the result of the node creation of nodes inside others nodes
	 * key : the metaclass of the parent node, value, the result of the creation tests
	 */
	private Map<String, CreateableElementInsideMe> nodesInsideNodes;

	/**
	 * The map with the result of edge creation from a node EditPart used as source
	 * 
	 * tested Edge Metaclass as key
	 * value : hashmap of sourceEditPart vs result of all others editpart as target
	 */
	private Map<String, Map<String, LinkRepresentationCapability>> edgesWithSourceNodeCreationsResult = new TreeMap<>();

	/**
	 * The map with the result of edge creation from an edge EditPart used as source
	 * 
	 * tested Edge Metaclass as key
	 * value : hashmap of sourceEditPart vs result of all others editpart as target
	 */
	private Map<String, Map<String, LinkRepresentationCapability>> edgesWithSourceEdgeCreationsResult = new TreeMap<>();


	/**
	 * 
	 * Constructor.
	 *
	 * @param filePath
	 *            the path of the file to create
	 * @param paletteDescription
	 *            the description of the palette
	 */
	public RequirementsExcelWriter(final String filePath, final PaletteDescriptionReader paletteDescription) {
		this.filePath = filePath;
		this.paletteDescription = paletteDescription;
	}

	/**
	 * 
	 * @param topNodeBackgroundTitle
	 * @param topNodeResults
	 *            the result of the nodes creation as top nodes (that's to say, on the Diagram background
	 */
	public void setTopNodesCreationResult(final String topNodeBackgroundTitle, final Set<TopNodeCreationResult> topNodeResults) {
		this.topNodeResults = topNodeResults;
		this.topNodeBackgroundTitle = topNodeBackgroundTitle;
	}

	/**
	 * 
	 * @param nodesInsideNodes
	 *            a map representing the result of the creation of nodes insides others nodes
	 */
	public void setChildNodesCreationResult(final Map<String, CreateableElementInsideMe> nodesInsideNodes) {
		this.nodesInsideNodes = nodesInsideNodes;
	}

	/**
	 * The map with the result of edge creation from a node EditPart used as source
	 * 
	 * tested Edge Metaclass as key
	 * value : hashmap of sourceEditPart vs result of all others editpart as target
	 */
	public void setEdgeCreationFromNodeResult(final Map<String, Map<String, LinkRepresentationCapability>> edgesWithSourceNodeCreationsResult) {
		this.edgesWithSourceNodeCreationsResult = edgesWithSourceNodeCreationsResult;
	}

	/**
	 * The map with the result of edge creation from an edge EditPart used as source
	 * 
	 * tested Edge Metaclass as key
	 * value : hashmap of sourceEditPart vs result of all others editpart as target
	 */
	public void setEdgeCreationFromEdgeResult(final Map<String, Map<String, LinkRepresentationCapability>> edgesWithSourceEdgeCreationsResult) {
		this.edgesWithSourceEdgeCreationsResult = edgesWithSourceEdgeCreationsResult;
	}



	public void write() {
		System.out.println("Writing file " + this.filePath); //$NON-NLS-1$
		final File myFile = new File(filePath);
		final XSSFWorkbook workbook = new XSSFWorkbook();
		writeIntroPage(workbook);
		writePaletteSheet(workbook);
		writeNodesOnDiagram(workbook);
		writeNodesInsideNodes(workbook);
		writeAllEdgesSheets(workbook);
		try {
			final FileOutputStream outputStream = new FileOutputStream(myFile);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			Activator.log.error(e);
		} catch (IOException e) {
			Activator.log.error(e);
		}
	}

	protected void writeIntroPage(final XSSFWorkbook workbook) {

		// Create a Bold Font
		final XSSFFont boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBold(true);
		boldFont.setFontName(FONT_NAME);
		boldFont.setColor(IndexedColors.BLACK.getIndex());

		final XSSFCellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(boldFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// style used for the elementType's name in the cell of the second column (index 1)
		final XSSFCellStyle wrappedTextStyle = workbook.createCellStyle();
		wrappedTextStyle.setWrapText(true);

		final XSSFSheet sheet_intro = workbook.createSheet("Intro"); //$NON-NLS-1$
		final Row firstRow = sheet_intro.createRow(0);
		String presentationText = "The initial version of the this document has been generated with Papyrus.\n" //$NON-NLS-1$
				+ "This document gives indications about the combinations of nodes and links inside the GMF Papyrus Diagram.\n" //$NON-NLS-1$
				+ "To get nodes capabilities, from an empty diagram, we explored the creation of nodes until 3 levels of imbrications (nodes on diagrams (depth=0), nodes inside these nodes (depth=1) then nodes inside the previous ndoes (depth=2).\n" //$NON-NLS-1$
				+ "To get links capabilities, from a diagram handly created and owning 2 representations of each nodes and links, we explored all combinations of source/target."; //$NON-NLS-1$

		String warningText = "This document only describes the graphical capabilities.\n" + //$NON-NLS-1$
				"This document does not describe the semantic result of these graphical capabilities.\n" //$NON-NLS-1$
				+ "Some specific cases may be missing.\n" //$NON-NLS-1$
				+ "Some specific cases may be wrong, these errors could come from a bad implementation of the generator or from a bad implementation of the Papyrus diagram.\n" //$NON-NLS-1$
				+ "This file could have been handly edited after its first generation"; //$NON-NLS-1$

		final Cell firstCell = firstRow.createCell(0);
		firstCell.setCellValue("PRESENTATION"); //$NON-NLS-1$
		firstCell.setCellStyle(titleCellStyle);
		final Cell secondCell = firstRow.createCell(1);
		secondCell.setCellValue(presentationText);
		secondCell.setCellStyle(wrappedTextStyle);

		// we keep an empty line
		final Row secondRow = sheet_intro.createRow(2);
		final Cell thirdCell = secondRow.createCell(0);
		thirdCell.setCellValue("WARNING"); //$NON-NLS-1$
		thirdCell.setCellStyle(titleCellStyle);

		final Cell fourthCell = secondRow.createCell(1);
		fourthCell.setCellValue(warningText);
		fourthCell.setCellStyle(wrappedTextStyle);

		sheet_intro.autoSizeColumn(0);
		sheet_intro.autoSizeColumn(1);

	}

	protected XSSFSheet writePaletteSheet(final XSSFWorkbook workbook) {
		// 1. create the required styles

		// Create a Bold Font
		final XSSFFont boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBold(true);
		boldFont.setFontName(FONT_NAME);
		boldFont.setColor(IndexedColors.BLACK.getIndex());

		// Style to display "Nodes" and "Edge" titles in Bold
		final XSSFCellStyle categoryTitleStyle = workbook.createCellStyle();
		categoryTitleStyle.setFont(boldFont);
		categoryTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		categoryTitleStyle.setAlignment(HorizontalAlignment.CENTER);

		// Style for the "title" of the page (first row)
		final XSSFCellStyle styleHeader = workbook.createCellStyle();
		styleHeader.setFont(boldFont);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);

		// style used for the metaclass's name in the cells of the first column (index 1)
		final XSSFCellStyle metaclassCellStyle = workbook.createCellStyle();
		metaclassCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		metaclassCellStyle.setAlignment(HorizontalAlignment.LEFT);
		metaclassCellStyle.setFont(boldFont);

		// style used for the elementType's name in the cell of the second column (index 2)
		final XSSFCellStyle elementTypesStyle = workbook.createCellStyle();
		elementTypesStyle.setWrapText(true);

		// 2. create the sheet
		final XSSFSheet sheet_Palette_Contents = workbook.createSheet("Palette Contents"); //$NON-NLS-1$
		int rowIndex = 0;
		final Row titleRow = sheet_Palette_Contents.createRow(rowIndex++);
		final Cell cell1 = titleRow.createCell(1);
		cell1.setCellValue("Element"); //$NON-NLS-1$
		cell1.setCellStyle(styleHeader);
		final Cell cell2 = titleRow.createCell(2);
		cell2.setCellValue("Graphical Element Types"); //$NON-NLS-1$
		cell2.setCellStyle(styleHeader);

		CellRangeAddress firstRowAdress = new CellRangeAddress(0, 0, 0, 2);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, firstRowAdress, sheet_Palette_Contents);

		int nodeStartIndex = rowIndex;
		// the nodes
		for (final Entry<String, Collection<String>> nodeEntry : this.paletteDescription.getMetaclassNodesVSElementTypesID().entrySet()) {
			// 5.1 create the metaclass cell (name of the metaclass)
			final String nodeMetaclass = nodeEntry.getKey();
			final int initialRowIndex = rowIndex;
			final Row metaclassRow = sheet_Palette_Contents.createRow(rowIndex++);
			if (rowIndex - 1 == nodeStartIndex) { // -1 because it is already incremented
				final Cell nodeTitleCell = metaclassRow.createCell(0);
				nodeTitleCell.setCellValue(NODES_TITLES);
				nodeTitleCell.setCellStyle(categoryTitleStyle);
			}


			final Cell metaclassCell = metaclassRow.createCell(1);
			metaclassCell.setCellValue(nodeMetaclass);
			metaclassCell.setCellStyle(metaclassCellStyle);

			final Iterator<String> elementTypesIter = nodeEntry.getValue().iterator();
			if (elementTypesIter.hasNext()) {
				final Cell cellElementType = metaclassRow.createCell(2);
				cellElementType.setCellValue(elementTypesIter.next());
				cellElementType.setCellStyle(elementTypesStyle);
			}
			while (elementTypesIter.hasNext()) {
				final Row nextTypeRow = sheet_Palette_Contents.createRow(rowIndex++);
				final Cell nextTypeCell = nextTypeRow.createCell(2);
				nextTypeCell.setCellValue(elementTypesIter.next());
				nextTypeCell.setCellStyle(elementTypesStyle);
			}
			if (initialRowIndex != (rowIndex - 1)) {
				sheet_Palette_Contents.addMergedRegion(new CellRangeAddress(initialRowIndex, rowIndex - 1, 1, 1));
			}
			CellRangeAddress lastRow = new CellRangeAddress(rowIndex - 1, rowIndex - 1, 1, 2);
			RegionUtil.setBorderBottom(BorderStyle.MEDIUM, lastRow, sheet_Palette_Contents);
		}

		sheet_Palette_Contents.addMergedRegion(new CellRangeAddress(nodeStartIndex, rowIndex - 1, 0, 0));
		int edgeStartIndex = rowIndex;

		CellRangeAddress lastNodeRow = new CellRangeAddress(edgeStartIndex - 1, edgeStartIndex - 1, 0, 2);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, lastNodeRow, sheet_Palette_Contents);

		for (final Entry<String, Collection<String>> edgeEntry : this.paletteDescription.getMetaclassEdgesVSElementTypesID().entrySet()) {
			// 6.1 create the metaclass cell (name of the metaclass)
			final String edgeMetaclass = edgeEntry.getKey();
			final int initialRowIndex = rowIndex;
			final Row metaclassRow = sheet_Palette_Contents.createRow(rowIndex++);

			if (rowIndex - 1 == edgeStartIndex) {// -1 because it is already incremented
				final Cell nodeTitleCell = metaclassRow.createCell(0);
				nodeTitleCell.setCellValue(EDGES_TITLES);
				nodeTitleCell.setCellStyle(categoryTitleStyle);
			}

			final Cell metaclassCell = metaclassRow.createCell(1);
			metaclassCell.setCellValue(edgeMetaclass);
			metaclassCell.setCellStyle(metaclassCellStyle);


			final Iterator<String> elementTypesIter = edgeEntry.getValue().iterator();
			if (elementTypesIter.hasNext()) {
				final Cell cellElementType = metaclassRow.createCell(2);
				cellElementType.setCellValue(elementTypesIter.next());
				cellElementType.setCellStyle(elementTypesStyle);
			}
			while (elementTypesIter.hasNext()) {
				final Row nextTypeRow = sheet_Palette_Contents.createRow(rowIndex++);
				final Cell nextTypeCell = nextTypeRow.createCell(2);
				nextTypeCell.setCellValue(elementTypesIter.next());
				nextTypeCell.setCellStyle(elementTypesStyle);
			}
			if (initialRowIndex != (rowIndex - 1)) {
				sheet_Palette_Contents.addMergedRegion(new CellRangeAddress(initialRowIndex, rowIndex - 1, 1, 1));
			}

			CellRangeAddress lastRow = new CellRangeAddress(rowIndex - 1, rowIndex - 1, 1, 2);
			RegionUtil.setBorderBottom(BorderStyle.MEDIUM, lastRow, sheet_Palette_Contents);
		}

		sheet_Palette_Contents.addMergedRegion(new CellRangeAddress(edgeStartIndex, rowIndex - 1, 0, 0));


		// int lastRowIndex = paletteDescription.getMetaclassNodes().size() + paletteDescription.getMetaclassEdges().size();
		CellRangeAddress lastRow = new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 2);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, lastRow, sheet_Palette_Contents);


		CellRangeAddress firstColumn = new CellRangeAddress(0, rowIndex - 1, 0, 0);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, firstColumn, sheet_Palette_Contents);
		CellRangeAddress secondColumn = new CellRangeAddress(0, rowIndex - 1, 1, 1);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, secondColumn, sheet_Palette_Contents);
		CellRangeAddress thirdColumn = new CellRangeAddress(0, rowIndex - 1, 2, 2);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, thirdColumn, sheet_Palette_Contents);

		// autosize the width of the column, need to be done at the end of the process
		// (we need to have a cell contents to be able to calculate the best width)
		sheet_Palette_Contents.autoSizeColumn(0);
		sheet_Palette_Contents.autoSizeColumn(1);
		sheet_Palette_Contents.autoSizeColumn(2);
		return sheet_Palette_Contents;
	}

	protected XSSFSheet writeNodesOnDiagram(final XSSFWorkbook workbook) {
		/// filling of "Nodes on Diagram" sheet
		final XSSFSheet sheet_Nodes_on_Diagram = workbook.createSheet("Nodes on Diagram"); //$NON-NLS-1$

		//////////////////////// Style part ////////////////////

		// Create a boolean Font green for YES:
		final XSSFFont boldFontBooleanYes = workbook.createFont();
		boldFontBooleanYes.setFontHeightInPoints((short) 10);
		boldFontBooleanYes.setFontName(FONT_NAME);
		boldFontBooleanYes.setColor(IndexedColors.GREEN.getIndex());

		// Create a boolean Font red for NO:
		final XSSFFont boldFontBooleanNo = workbook.createFont();
		boldFontBooleanNo.setFontHeightInPoints((short) 10);
		boldFontBooleanNo.setFontName(FONT_NAME);
		boldFontBooleanNo.setColor(IndexedColors.RED.getIndex());

		// Style to display for "No"
		final XSSFCellStyle booleanStyle_NO = workbook.createCellStyle();
		booleanStyle_NO.setFont(boldFontBooleanNo);
		booleanStyle_NO.setBorderBottom(BorderStyle.MEDIUM);
		booleanStyle_NO.setBorderLeft(BorderStyle.MEDIUM);
		booleanStyle_NO.setBorderRight(BorderStyle.MEDIUM);
		booleanStyle_NO.setBorderTop(BorderStyle.MEDIUM);
		booleanStyle_NO.setAlignment(HorizontalAlignment.CENTER);

		// Style to display for "Yes"
		final XSSFCellStyle booleanStyle_Yes = workbook.createCellStyle();
		booleanStyle_Yes.setFont(boldFontBooleanYes);
		booleanStyle_Yes.setBorderBottom(BorderStyle.MEDIUM);
		booleanStyle_Yes.setBorderLeft(BorderStyle.MEDIUM);
		booleanStyle_Yes.setBorderRight(BorderStyle.MEDIUM);
		booleanStyle_Yes.setBorderTop(BorderStyle.MEDIUM);
		booleanStyle_Yes.setAlignment(HorizontalAlignment.CENTER);

		// Create a Bold Font

		final XSSFFont boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBold(true);
		boldFont.setFontName(FONT_NAME);
		boldFont.setColor(IndexedColors.BLACK.getIndex());

		// Style to display "Nodes" and "Edge" titles in Bold
		final XSSFCellStyle categoryTitleStyle = workbook.createCellStyle();
		categoryTitleStyle.setFont(boldFont);
		categoryTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		categoryTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		categoryTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		categoryTitleStyle.setBorderTop(BorderStyle.MEDIUM);


		// Style for the "title" of the page (first row)
		final XSSFCellStyle styleHeader = workbook.createCellStyle();
		styleHeader.setFont(boldFont);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);

		// style used for the metaclass's name in the cells of the first column (index 0)
		final XSSFCellStyle metaclassCellStyle = workbook.createCellStyle();
		metaclassCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		metaclassCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		metaclassCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		metaclassCellStyle.setBorderRight(BorderStyle.MEDIUM);
		metaclassCellStyle.setBorderTop(BorderStyle.MEDIUM);

		////////////////////////////////////////////////////////////////////////////////////////////////////////

		int indexRow = 0;
		final Row rowNodes_on_Diagram = sheet_Nodes_on_Diagram.createRow(indexRow++);
		final Cell cellNodes_on_Diagram = rowNodes_on_Diagram.createCell(1);
		cellNodes_on_Diagram.setCellValue(this.topNodeBackgroundTitle);
		cellNodes_on_Diagram.setCellStyle(categoryTitleStyle);

		for (TopNodeCreationResult result : this.topNodeResults) {
			Row row2 = sheet_Nodes_on_Diagram.createRow(indexRow++);
			final Cell metaclassCell = row2.createCell(0);
			metaclassCell.setCellValue(result.getMetaclassName());
			metaclassCell.setCellStyle(metaclassCellStyle);
			final Cell isEnabledCell = row2.createCell(1);

			if (result.isValidTopNode()) {
				isEnabledCell.setCellValue(YES__VALUE);
				isEnabledCell.setCellStyle(booleanStyle_Yes);

			} else {
				isEnabledCell.setCellValue(NO__VALUE);
				isEnabledCell.setCellStyle(booleanStyle_NO);
			}
		}

		sheet_Nodes_on_Diagram.autoSizeColumn(0);
		sheet_Nodes_on_Diagram.autoSizeColumn(1);
		sheet_Nodes_on_Diagram.autoSizeColumn(2);
		return sheet_Nodes_on_Diagram;
	}

	protected XSSFSheet writeNodesInsideNodes(final XSSFWorkbook workbook) {
		final XSSFSheet sheet_Nodes_on_Diagram = workbook.createSheet("Nodes inside nodes"); //$NON-NLS-1$
		//////////////////////// Style part ////////////////////

		// Create a boolean Font green for YES:
		final XSSFFont boldFontBooleanYes = workbook.createFont();
		boldFontBooleanYes.setFontHeightInPoints((short) 10);
		// boldFontBooleanYes.setBold(true);
		boldFontBooleanYes.setFontName(FONT_NAME);
		boldFontBooleanYes.setColor(IndexedColors.GREEN.getIndex());

		// Create a boolean Font red for NO:
		final XSSFFont boldFontBooleanNo = workbook.createFont();
		boldFontBooleanNo.setFontHeightInPoints((short) 10);
		// boldFontBooleanNo.setBold(true);
		boldFontBooleanNo.setFontName(FONT_NAME);
		boldFontBooleanNo.setColor(IndexedColors.RED.getIndex());

		// Style to display for "No"
		final XSSFCellStyle BooleanStyle_NO = workbook.createCellStyle();
		BooleanStyle_NO.setFont(boldFontBooleanNo);
		BooleanStyle_NO.setBorderBottom(BorderStyle.MEDIUM);
		BooleanStyle_NO.setBorderLeft(BorderStyle.MEDIUM);
		BooleanStyle_NO.setBorderRight(BorderStyle.MEDIUM);
		BooleanStyle_NO.setBorderTop(BorderStyle.MEDIUM);
		BooleanStyle_NO.setAlignment(HorizontalAlignment.CENTER);

		// Style to display for "Yes"
		final XSSFCellStyle BooleanStyle_Yes = workbook.createCellStyle();
		BooleanStyle_Yes.setFont(boldFontBooleanYes);
		BooleanStyle_Yes.setBorderBottom(BorderStyle.MEDIUM);
		BooleanStyle_Yes.setBorderLeft(BorderStyle.MEDIUM);
		BooleanStyle_Yes.setBorderRight(BorderStyle.MEDIUM);
		BooleanStyle_Yes.setBorderTop(BorderStyle.MEDIUM);
		BooleanStyle_Yes.setAlignment(HorizontalAlignment.CENTER);

		// Create a Bold Font
		final XSSFFont boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBold(true);
		boldFont.setFontName(FONT_NAME);
		boldFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a reed Bold Font for corner
		final XSSFFont redBoldFont = workbook.createFont();
		redBoldFont.setFontHeightInPoints((short) 10);
		redBoldFont.setBold(true);
		redBoldFont.setFontName(FONT_NAME); // $NON-NLS-1$
		redBoldFont.setColor(IndexedColors.RED.getIndex());

		// Style to display titles in Bold
		final XSSFCellStyle categoryTitleStyle = workbook.createCellStyle();
		categoryTitleStyle.setFont(boldFont);
		categoryTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		categoryTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		categoryTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		categoryTitleStyle.setBorderTop(BorderStyle.MEDIUM);

		// Style for the "title" of the page (first row)
		final XSSFCellStyle cornerStyle = workbook.createCellStyle();
		cornerStyle.setFont(redBoldFont);
		cornerStyle.setAlignment(HorizontalAlignment.CENTER);
		cornerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cornerStyle.setWrapText(true);

		// style used for the metaclass's name in the cells of the first column (index 0)
		final XSSFCellStyle metaclassCellStyle = workbook.createCellStyle();
		metaclassCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		metaclassCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		metaclassCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		metaclassCellStyle.setBorderRight(BorderStyle.MEDIUM);
		metaclassCellStyle.setBorderTop(BorderStyle.MEDIUM);
		////// Set Horizontal Style to the nodes in the first Row
		final XSSFCellStyle horizontalMetaclassTitleStyle = workbook.createCellStyle();
		horizontalMetaclassTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		horizontalMetaclassTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		horizontalMetaclassTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		horizontalMetaclassTitleStyle.setBorderTop(BorderStyle.MEDIUM);
		horizontalMetaclassTitleStyle.setRotation((short) 90);
		////////////////////////////////////////////////////////////////////////////////////////////////////////

		// 0. fill corner cell
		final Row firstRow = sheet_Nodes_on_Diagram.createRow(0);
		final Cell cornerCell = firstRow.createCell(0);
		cornerCell.setCellValue("Can we create column element inside\n row element representation?"); //$NON-NLS-1$
		cornerCell.setCellStyle(cornerStyle);

		// 0. create the column header
		int columnIndex = 1;
		for (final String metaclassNode : this.paletteDescription.getMetaclassNodes()) {
			// create the metaclass cell (name of the metaclass)
			final Row metaclassRow = sheet_Nodes_on_Diagram.getRow(0); // already created with the corner row
			final Cell metaclassCell = metaclassRow.createCell(columnIndex++);
			metaclassCell.setCellValue(metaclassNode);
			metaclassCell.setCellStyle(horizontalMetaclassTitleStyle);

		}

		// 1. create row
		int rowIndex = 1;
		for (final String nodeMetaclass : this.paletteDescription.getMetaclassNodes()) {
			final Row metaclassRow = sheet_Nodes_on_Diagram.createRow(rowIndex++);

			// create the row header
			int cellIndex = 0;
			final Cell metaclassCell = metaclassRow.createCell(cellIndex++);
			metaclassCell.setCellValue(nodeMetaclass);
			metaclassCell.setCellStyle(metaclassCellStyle);

			// fill the row
			final CreateableElementInsideMe creatableInsideMe = this.nodesInsideNodes.get(nodeMetaclass);
			for (final String currentColumMetaclass : this.paletteDescription.getMetaclassNodes()) {
				final Boolean canBeCreated = creatableInsideMe.getCreatableElementsList().get(currentColumMetaclass);
				if (canBeCreated != null && canBeCreated.booleanValue()) {
					final Cell enableCell = metaclassRow.createCell(cellIndex);
					enableCell.setCellValue(YES__VALUE);
					enableCell.setCellStyle(BooleanStyle_Yes);
				} else {
					final Cell enableCell = metaclassRow.createCell(cellIndex);
					enableCell.setCellValue(NO__VALUE);
					enableCell.setCellStyle(BooleanStyle_NO);
				}
				cellIndex++;
			}
		}


		sheet_Nodes_on_Diagram.autoSizeColumn(0);

		while (columnIndex >= 0) {
			sheet_Nodes_on_Diagram.autoSizeColumn(columnIndex);
			columnIndex--;
		}
		return sheet_Nodes_on_Diagram;
	}

	/**
	 * This method is in charge to create one sheet per link and to fill it
	 * 
	 * @param workbook
	 *            the workbook
	 */
	protected void writeAllEdgesSheets(final XSSFWorkbook workbook) {
		for (final String metaclassLink : paletteDescription.getMetaclassEdges()) {
			final XSSFSheet sheet = workbook.createSheet("L-" + metaclassLink); //$NON-NLS-1$ // L- to identify it is a sheet describing a link (following by the link metaclass name)
			fillEdgeSheet(workbook, sheet, metaclassLink, this.edgesWithSourceNodeCreationsResult.get(metaclassLink), this.edgesWithSourceEdgeCreationsResult.get(metaclassLink));
		}
	}

	/**
	 * Fill the sheet for the given edge
	 * 
	 * @param workbook
	 * @param sheet
	 * @param linkMetaclassName
	 *            the edge metaclass name (palette label)
	 * @param edgeCapabilityFromNodes
	 *            the map of edges that can be created from a source node
	 * @param edgeCapabilityFromEdges
	 *            the map of edges that can be created from a source edge
	 */
	protected void fillEdgeSheet(final XSSFWorkbook workbook, final XSSFSheet sheet, final String linkMetaclassName, final Map<String, LinkRepresentationCapability> edgeCapabilityFromNodes,
			final Map<String, LinkRepresentationCapability> edgeCapabilityFromEdges) {
		// Create a Bold Font
		final XSSFFont boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBold(true);
		boldFont.setFontName(FONT_NAME);
		boldFont.setColor(IndexedColors.BLACK.getIndex());

		// Style to display "Nodes" and "Edge" titles on the first row in Bold
		final XSSFCellStyle horizontalCategoryTitleStyle = workbook.createCellStyle();
		horizontalCategoryTitleStyle.setFont(boldFont);
		horizontalCategoryTitleStyle.setAlignment(HorizontalAlignment.CENTER);
		horizontalCategoryTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		horizontalCategoryTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		horizontalCategoryTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		horizontalCategoryTitleStyle.setBorderTop(BorderStyle.MEDIUM);

		final XSSFCellStyle verticalCategoryTitleStyle = workbook.createCellStyle();
		verticalCategoryTitleStyle.setFont(boldFont);
		verticalCategoryTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		verticalCategoryTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		verticalCategoryTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		verticalCategoryTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		verticalCategoryTitleStyle.setBorderTop(BorderStyle.MEDIUM);

		final XSSFCellStyle horizontalMetaclassTitleStyle = workbook.createCellStyle();
		horizontalMetaclassTitleStyle.setFont(boldFont);
		horizontalMetaclassTitleStyle.setRotation((short) 90);
		horizontalMetaclassTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		horizontalMetaclassTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		horizontalMetaclassTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		horizontalMetaclassTitleStyle.setBorderTop(BorderStyle.MEDIUM);

		final XSSFCellStyle verticalMetaclassTitleStyle = workbook.createCellStyle();
		verticalMetaclassTitleStyle.setFont(boldFont);
		verticalMetaclassTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
		verticalMetaclassTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
		verticalMetaclassTitleStyle.setBorderRight(BorderStyle.MEDIUM);
		verticalMetaclassTitleStyle.setBorderTop(BorderStyle.MEDIUM);

		final XSSFFont redBoldFont = workbook.createFont();
		redBoldFont.setFontHeightInPoints((short) 10);
		redBoldFont.setBold(true);
		redBoldFont.setFontName(FONT_NAME); // $NON-NLS-1$
		redBoldFont.setColor(IndexedColors.RED.getIndex());

		final XSSFCellStyle cornerStyle = workbook.createCellStyle();
		cornerStyle.setAlignment(HorizontalAlignment.CENTER);
		cornerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cornerStyle.setWrapText(true);
		cornerStyle.setFont(redBoldFont);

		final XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName(FONT_NAME); // $NON-NLS-1$
		font.setColor(IndexedColors.BLACK.getIndex());


		final XSSFCellStyle standartCellStyle = workbook.createCellStyle();
		standartCellStyle.setAlignment(HorizontalAlignment.CENTER);
		standartCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		standartCellStyle.setFont(font);


		// 1. the first row, with all target metaclasses
		int rowIndex = 0;
		int cellIndex = 0;
		final Row firstRow = sheet.createRow(rowIndex++);
		final Cell cornerCell = firstRow.createCell(cellIndex++);
		cornerCell.setCellValue(NLS.bind("Can we create {0} between the row (source) and the column (target)?", linkMetaclassName)); //$NON-NLS-1$
		cornerCell.setCellStyle(cornerStyle);

		cellIndex++;// there is nothing to write in the second cell of the first row (rowIndex=0, columnIndex=1 must be empty)
		final Cell nodesSourcesRowTitle = firstRow.createCell(cellIndex);// we don't increase cellIndex here
		nodesSourcesRowTitle.setCellValue(NODES_TITLES);
		nodesSourcesRowTitle.setCellStyle(horizontalCategoryTitleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, cellIndex, cellIndex + this.paletteDescription.getMetaclassNodes().size() - 1)); // -1 because we are already on the first cell!

		cellIndex = cellIndex + this.paletteDescription.getMetaclassNodes().size();
		final Cell edgesSourcesRowTitle = firstRow.createCell(cellIndex);// we don't increase cellIndex here
		edgesSourcesRowTitle.setCellValue(EDGES_TITLES);
		edgesSourcesRowTitle.setCellStyle(horizontalCategoryTitleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, cellIndex, cellIndex + this.paletteDescription.getMetaclassEdges().size() - 1));

		// 2. second row : the target metaclass name
		final Row targetMetaclassRow = sheet.createRow(rowIndex++);
		cellIndex = 2; // reset the cell index

		for (final String currentMetaclass : this.paletteDescription.getMetaclassNodes()) {
			final Cell metaclassCell = targetMetaclassRow.createCell(cellIndex++);
			metaclassCell.setCellValue(currentMetaclass);
			metaclassCell.setCellStyle(horizontalMetaclassTitleStyle);
		}
		for (final String currentMetaclass : this.paletteDescription.getMetaclassEdges()) {
			final Cell metaclassCell = targetMetaclassRow.createCell(cellIndex++);
			metaclassCell.setCellValue(currentMetaclass);
			metaclassCell.setCellStyle(horizontalMetaclassTitleStyle);
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1)); // merge the corner


		// 2. we fill the table contents now
		// node header for rows
		// row header will be on the next row
		final int rowNodeTitleIndex = rowIndex;

		for (final String currentNodeMetaclassSource : this.paletteDescription.getMetaclassNodes()) {
			final LinkRepresentationCapability capabilities = edgeCapabilityFromNodes.get(currentNodeMetaclassSource);
			cellIndex = 1;// reset the cell index (source metaclass is inside the 2nd column (index==1)!
			final Row metaclassRow = sheet.createRow(rowIndex);// we increase after the if!
			if (rowIndex == rowNodeTitleIndex) {
				final Cell rowNodeTitle = metaclassRow.createCell(0);
				rowNodeTitle.setCellValue(NODES_TITLES);
				rowNodeTitle.setCellStyle(verticalCategoryTitleStyle);
			}
			rowIndex++;

			final Cell rowMetaclassTitle = metaclassRow.createCell(cellIndex++);
			// we write the name of the source metaclass
			rowMetaclassTitle.setCellValue(currentNodeMetaclassSource);// we write the name of the source metaclass
			rowMetaclassTitle.setCellStyle(verticalMetaclassTitleStyle);

			if (capabilities != null) {
				for (final String currentNodeMetaclassTarget : this.paletteDescription.getMetaclassNodes()) {
					final Boolean isEnable = capabilities.getTargetResult().get(currentNodeMetaclassTarget);
					final Cell cellResult = metaclassRow.createCell(cellIndex++);
					if (isEnable != null && isEnable.booleanValue()) {
						if (currentNodeMetaclassSource.equals(currentNodeMetaclassTarget) && capabilities.isReflexive()) {
							cellResult.setCellValue(YES_R__VALUE);
						} else {
							cellResult.setCellValue(YES__VALUE);
						}
						cellResult.setCellStyle(standartCellStyle);
					}
				}

				for (final String currentEdgeMetaclass : this.paletteDescription.getMetaclassEdges()) {
					final Boolean isEnable = capabilities.getTargetResult().get(currentEdgeMetaclass);
					final Cell cellResult = metaclassRow.createCell(cellIndex++);
					if (isEnable != null && isEnable.booleanValue()) {
						cellResult.setCellValue(YES__VALUE);
						cellResult.setCellStyle(standartCellStyle);
					}
				}
			}

		}
		sheet.addMergedRegion(new CellRangeAddress(rowNodeTitleIndex, rowIndex - 1, 0, 0));
		final int rowEdgeTitleIndex = rowIndex;

		for (final String edgeMetaclassSource : this.paletteDescription.getMetaclassEdges()) {
			final LinkRepresentationCapability capabilities = edgeCapabilityFromEdges.get(edgeMetaclassSource);
			cellIndex = 1;// reset the cell index (source metaclass is inside the 2nd column (index==1)!
			final Row metaclassRow = sheet.createRow(rowIndex);// we increase after the if!
			if (rowIndex == rowEdgeTitleIndex) {
				final Cell rowEdgeTitle = metaclassRow.createCell(0);
				rowEdgeTitle.setCellValue(EDGES_TITLES);
				rowEdgeTitle.setCellStyle(verticalCategoryTitleStyle);
			}
			rowIndex++;

			final Cell rowMetaclassTitle = metaclassRow.createCell(cellIndex++);
			// we write the name of the source metaclass
			rowMetaclassTitle.setCellValue(edgeMetaclassSource);// we write the name of the source metaclass
			rowMetaclassTitle.setCellStyle(verticalMetaclassTitleStyle);

			if (capabilities != null) {
				for (final String currentNodeMetaclass : this.paletteDescription.getMetaclassNodes()) {
					final Boolean isEnable = capabilities.getTargetResult().get(currentNodeMetaclass);
					final Cell cellResult = metaclassRow.createCell(cellIndex++);
					if (isEnable != null && isEnable.booleanValue()) {
						cellResult.setCellValue(YES__VALUE);
						cellResult.setCellStyle(standartCellStyle);

					}
				}

				for (final String currentEdgeMetaclassTarget : this.paletteDescription.getMetaclassEdges()) {
					final Boolean isEnable = capabilities.getTargetResult().get(currentEdgeMetaclassTarget);
					final Cell cellResult = metaclassRow.createCell(cellIndex++);
					if (isEnable != null && isEnable.booleanValue()) {
						if (edgeMetaclassSource.equals(currentEdgeMetaclassTarget) && capabilities.isReflexive()) {
							cellResult.setCellValue(YES_R__VALUE);
						} else {
							cellResult.setCellValue(YES__VALUE);
						}
						cellResult.setCellStyle(standartCellStyle);
					}
				}
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(rowEdgeTitleIndex, rowIndex - 1, 0, 0));

		// autosize the width of the column, need to be done at the end of the process
		// (we need to have a cell contents to be able to calculate the best width)
		final int oneCharWidth = 256;
		int nbCharByCell = 9;
		for (int i = 0; i < (this.paletteDescription.getMetaclassNodes().size() + this.paletteDescription.getMetaclassEdges().size() + 2); i++) {
			if (i == 0 || i == 1) {
				sheet.autoSizeColumn(i);
			} else {
				sheet.setColumnWidth(i, oneCharWidth * nbCharByCell);
			}
		}

		// border definition
		// final Row firstRow = sheet.getRow(0);
		final XSSFCellStyle rowStyle = workbook.createCellStyle();
		rowStyle.setBorderBottom(BorderStyle.MEDIUM);
		rowStyle.setBorderLeft(BorderStyle.MEDIUM);
		rowStyle.setBorderRight(BorderStyle.MEDIUM);
		rowStyle.setBorderTop(BorderStyle.MEDIUM);

		CellRangeAddress firstRowHeaderAdress = new CellRangeAddress(0, 0, 0, this.paletteDescription.getMetaclassNodes().size() + this.paletteDescription.getMetaclassEdges().size() + 1);
		CellRangeAddress secondRowHeaderAdress = new CellRangeAddress(1, 1, 0, this.paletteDescription.getMetaclassNodes().size() + this.paletteDescription.getMetaclassEdges().size() + 1);


		RegionUtil.setBorderTop(BorderStyle.MEDIUM, firstRowHeaderAdress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, firstRowHeaderAdress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, firstRowHeaderAdress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, firstRowHeaderAdress, sheet);

		RegionUtil.setBorderTop(BorderStyle.MEDIUM, secondRowHeaderAdress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, secondRowHeaderAdress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, secondRowHeaderAdress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, secondRowHeaderAdress, sheet);

		CellRangeAddress firstColumnHeaderAdress = new CellRangeAddress(0, this.paletteDescription.getMetaclassNodes().size() + this.paletteDescription.getMetaclassEdges().size() + 1, 0, 0);
		CellRangeAddress secondColumnHeaderAdress = new CellRangeAddress(0, this.paletteDescription.getMetaclassNodes().size() + this.paletteDescription.getMetaclassEdges().size() + 1, 1, 1);

		RegionUtil.setBorderTop(BorderStyle.MEDIUM, firstColumnHeaderAdress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, firstColumnHeaderAdress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, firstColumnHeaderAdress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, firstColumnHeaderAdress, sheet);

		RegionUtil.setBorderTop(BorderStyle.MEDIUM, secondColumnHeaderAdress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, secondColumnHeaderAdress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, secondColumnHeaderAdress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, secondColumnHeaderAdress, sheet);

		int totalRow = this.paletteDescription.getMetaclassNodes().size() + this.paletteDescription.getMetaclassEdges().size() + 1;
		int toalCoumn = totalRow;
		CellRangeAddress bottomRowRange = new CellRangeAddress(totalRow, totalRow, 0, toalCoumn);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, bottomRowRange, sheet);

		CellRangeAddress leftColumnRange = new CellRangeAddress(0, totalRow, toalCoumn, toalCoumn);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, leftColumnRange, sheet);

		CellRangeAddress verticalNodeEdgeSeparator = new CellRangeAddress(0, totalRow, 1 + this.paletteDescription.getMetaclassNodes().size(), 1 + this.paletteDescription.getMetaclassNodes().size());
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, verticalNodeEdgeSeparator, sheet);

		CellRangeAddress HorizontalNodeEdgeSeparator = new CellRangeAddress(1 + this.paletteDescription.getMetaclassNodes().size(), 1 + this.paletteDescription.getMetaclassNodes().size(), 0, toalCoumn);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, HorizontalNodeEdgeSeparator, sheet);
	}

}

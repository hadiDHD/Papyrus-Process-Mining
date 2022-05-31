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

import java.time.Duration;
import java.util.ArrayList;
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
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.gmfdiag.common.service.visualtype.VisualTypeService;
import org.eclipse.papyrus.junit.utils.rules.ActiveDiagram;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.eclipse.papyrus.uml.diagram.activity.UmlActivityDiagramForMultiEditor;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.CreateableElementInsideMe;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.RequirementsExcelWriter;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
@PluginResource("resources/creation/ActivityDiagram/ActivityDiagram_OnModel.di")
public class ActivityDiagram_OnModel_Extractor extends AbtractPapyrusDiagramSpecificationsExtractor {

	// just here to force dependency
	@SuppressWarnings("unused")
	private final UmlActivityDiagramForMultiEditor editor = null;

	/**
	 * The URI of the palette definition
	 */
	private final URI PALETTE_DEFINITION = URI.createPlatformPluginURI("org.eclipse.papyrus.uml.diagram.activity/model/PapyrusUMLActivityDiagram.paletteconfiguration", false); //$NON-NLS-1$

	private static final String EMPTY__DIAGRAM_NAME = "ActivityDiagram_Empty"; //$NON-NLS-1$

	private static final String ALL_ELEMENTS__DIAGRAM_NAME = "ActivityDiagram_AllElements"; //$NON-NLS-1$

	private static final String FILE_NAME = "ActivityDiagram_Requirements";//$NON-NLS-1$

	private static final Collection<String> NPE_ON_SEQUENCE_NODE = new ArrayList<>();
	static {
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.CentralBufferNode_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ClearStructuralFeatureAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ConditionalNode_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.CreateLinkAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.CreateLinkObjectAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.CreateObjectAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.DataStoreNode_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.DestroyLinkAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.DestroyObjectAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ExpansionRegion_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.LoopNode_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.OpaqueAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReadExtentAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReadIsClassifiedObjectAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReadLinkAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReadStructuralFeatureAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReadVariableAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReadSelfAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReclassifyObjectAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ReduceAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.SendObjectAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.SendSignalAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.SequenceNode_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.StartClassifierBehaviorAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.StartObjectBehaviorAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.StructuredActivityNode_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.TestIdentityAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.UnmarshallAction_Shape"); //$NON-NLS-1$
		NPE_ON_SEQUENCE_NODE.add("org.eclipse.papyrus.umldi.ValueSpecificationAction_Shape"); //$NON-NLS-1$
	}

	private static final String CONDITIONAL_NODE = "Conditional Node"; //$NON-NLS-1$
	private static final String ACTIVITY = "Activity"; //$NON-NLS-1$
	private static final String EXPANSION_REGION = "Expansion Region";//$NON-NLS-1$
	private static final String LOOP_NODE = "Loop Node"; //$NON-NLS-1$
	private static final String SEQUENCE_NODE = "Sequence Node";//$NON-NLS-1$
	private static final String STRUCTURED_ACTIVITY_NODE = "Structured Activity Node";//$NON-NLS-1$
	private static final String CALL_BEHAVIOR_ACTION = "org.eclipse.papyrus.umldi.CallBehaviorAction_Shape"; //$NON-NLS-1$
	private static final String SEND_SIGNAL_ACTION = "org.eclipse.papyrus.umldi.SendSignalAction_Shape"; //$NON-NLS-1$
	private static final String CALL_OPERATION_ACTION = "org.eclipse.papyrus.umldi.CallOperationAction_Shape"; //$NON-NLS-1$

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
		long before = System.currentTimeMillis();
		// 1. create top nodes and child nodes
		playWithNodes(); // play with the empty diagram

		fixture.closeDiagram();

		fixture.flushDisplayEvents();

		fixture.openDiagram(fixture.getEditor(), ALL_ELEMENTS__DIAGRAM_NAME);

		// 2. create all possibles edges
		playWithEdges();

		// 3. write the excel file
		writeExcelFile();

		long after = System.currentTimeMillis();
		long duration = after - before;
		Duration time = Duration.ofMillis(duration);
		long minutes = time.toMinutes();
		System.out.println(NLS.bind("{0} took {1} minutes", getClass().getCanonicalName(), Long.toString(minutes))); //$NON-NLS-1$
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
		final String metaclass = getMetaclassFromEditPart(parentEditPart);
		final String elementTypeID = elementType.getId();
		final String testedMetaclassCreation = getPaletteDescriptionReader().getMetaclass(elementTypeID);

		// all these cases open a Dialog!
		if (ACTIVITY.equals(metaclass)
				|| CONDITIONAL_NODE.equals(metaclass)
				|| EXPANSION_REGION.equals(metaclass)
				|| LOOP_NODE.equals(metaclass)
				|| STRUCTURED_ACTIVITY_NODE.equals(metaclass)
				|| SEQUENCE_NODE.equals(metaclass)) {
			switch (elementTypeID) {
			case CALL_BEHAVIOR_ACTION:
			case CALL_OPERATION_ACTION:
			case SEND_SIGNAL_ACTION:
				creationResult.registerCreatableElement(testedMetaclassCreation, true);
				return Collections.emptyList();
			default:
				// nothing to do
			}
		}
		if (SEQUENCE_NODE.equals(metaclass) && NPE_ON_SEQUENCE_NODE.contains(elementTypeID)) {
			// TODO in reality, I have not yet an idea, but all these cases generated NPE
			creationResult.registerCreatableElement(testedMetaclassCreation, false);
			return Collections.emptyList();
		}
		return super.createChildNodeOnEditpart(depth, parentEditPart, elementType, location, dimension, creationResult);
	}




	// NPE dans NamedElementInitializerHelperAdvice (ligne 61 et 64, quand le eContainer() est null) avec org.eclipse.papyrus.umldi.DataStoreNode_Shape": par exemple
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
			if ("org.eclipse.papyrus.umldi.InputPin_CallOperationActionTargetShape".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Input Pin"; //$NON-NLS-1$
			} else if ("org.eclipse.papyrus.umldi.InputPin_SendSignalActionTargetShape".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Input Pin"; //$NON-NLS-1$
			} else if ("org.eclipse.papyrus.umldi.OutputPin_CreateObjectActionResultShape".equals(res.getId())) { //$NON-NLS-1$
				metaclass = "Output Pin"; //$NON-NLS-1$
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
				cell.setCellValue("The Call Operation Action tool opens a dialog to select an existing element."); //$NON-NLS-1$

				additionalRow = sheet.createRow(rowIndex++);
				cell = additionalRow.createCell(0);
				cell.setCellValue("The Call Behavior Action tool opens a dialog to select an existing element."); //$NON-NLS-1$

				additionalRow = sheet.createRow(rowIndex++);
				cell = additionalRow.createCell(0);
				cell.setCellValue("The Send Signal Action tool opens a dialog to select an existing element."); //$NON-NLS-1$

				rowIndex = rowStart;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));
				rowIndex++;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));
				rowIndex++;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));
				rowIndex++;
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));

				rowIndex++; // empty line
				additionalRow = sheet.createRow(rowIndex++);
				cell = additionalRow.createCell(0);
				cell.setCellValue("Papyrus Generates NPEs when we try to create these elements on " + SEQUENCE_NODE); //$NON-NLS-1$
				cell.setCellStyle(titleCellStyle);
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, paletteReader.getMetaclassNodes().size()));

				for (final String current : NPE_ON_SEQUENCE_NODE) {
					additionalRow = sheet.createRow(rowIndex);
					cell = additionalRow.createCell(0);
					cell.setCellValue(current);
					sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paletteReader.getMetaclassNodes().size()));
					rowIndex++;
				}

				return sheet;
			}
		};
	}


}

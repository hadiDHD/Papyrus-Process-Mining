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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.gmfdiag.common.preferences.PreferencesConstantsHelper;
import org.eclipse.papyrus.infra.gmfdiag.common.service.visualtype.VisualTypeService;
import org.eclipse.papyrus.infra.gmfdiag.common.utils.DiagramEditPartsUtil;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture;
import org.eclipse.papyrus.uml.diagram.specs.extractor.Activator;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.CreateableElementInsideMe;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.CustomPapyrusEditorFixture;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.LinkRepresentationCapability;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.PaletteDescriptionReader;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.RequirementsExcelWriter;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.TopNodeCreationResult;
import org.eclipse.papyrus.uml.diagram.specs.extractor.utils.TopNodeCreationResultComparator;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.junit.Rule;

/**
 * Abstract class to extend to extract specification from Papyrus GMF Diagram
 */
public abstract class AbtractPapyrusDiagramSpecificationsExtractor extends AbstractPapyrusTest {

	/**
	 * EXCEL extension file
	 */
	protected static final String EXCEL_FILE_EXTENSION = "xlsx"; //$NON-NLS-1$

	/**
	 * the name of the file where the specifications must be saved
	 */
	private String fileName;

	/**
	 * allows to access to the palette elements
	 */
	private PaletteDescriptionReader paletteReader;

	/**
	 * the fixture used to open Papyrus models
	 */
	@Rule
	public final CustomPapyrusEditorFixture fixture = new CustomPapyrusEditorFixture();

	/**
	 * The folder where the generated files will be saved
	 */
	private String outputFolder;

	/**
	 * The result of the topNodeCreation
	 */
	private final Set<TopNodeCreationResult> topNodeResults = new TreeSet<>(new TopNodeCreationResultComparator());

	/**
	 * The result of node creation inside top nodes
	 */
	private final Map<String, CreateableElementInsideMe> childrenResult = new TreeMap<>();

	/**
	 * The map with the result of edge creation from a node EditPart used as source
	 * 
	 * tested Edge Metaclass as key
	 * value : hashmap of sourceEditPart vs result of all others editpart as target
	 */
	private final Map<String, Map<String, LinkRepresentationCapability>> edgesWithSourceNodeCreationsResult = new TreeMap<>();

	/**
	 * The map with the result of edge creation from an edge EditPart used as source
	 * 
	 * tested Edge Metaclass as key
	 * value : hashmap of sourceEditPart vs result of all others editpart as target
	 */
	private final Map<String, Map<String, LinkRepresentationCapability>> edgesWithSourceEdgeCreationsResult = new TreeMap<>();

	/**
	 * 
	 * @param paletteURI
	 *            the URi of the palette of the diagram
	 */
	protected void init(final URI paletteURI, final String fileName) {
		this.paletteReader = createPaletteReader(paletteURI);
		this.fileName = fileName;
		this.outputFolder = getOutputFolder();
	}

	protected PaletteDescriptionReader createPaletteReader(final URI paletteURI) {
		return new PaletteDescriptionReader(paletteURI);
	}

	/**
	 * 
	 * @return
	 *         the {@link PaletteDescriptionReader}
	 */
	protected final PaletteDescriptionReader getPaletteDescriptionReader() {
		return this.paletteReader;
	}



	private static final int offset = 15;

	/**
	 * This method allows to tests the node creation on an empty diagram
	 * 
	 * This method tests the creation on the diagram background and inside created nodes, in the same time
	 */
	protected void playWithNodes() {

		final Map<String, Collection<IElementType>> nodesElementsTypes = this.paletteReader.getNodesElementsTypes();

		int x_OnTopNode = 10;
		int y_OnTopNode = 10;
		int topNodeSize = 200;
		for (final Entry<String, Collection<IElementType>> entry : nodesElementsTypes.entrySet()) {
			final String metaclassName = entry.getKey();

			final TopNodeCreationResult topNodeResult = new TopNodeCreationResult(metaclassName);
			this.topNodeResults.add(topNodeResult);

			for (final IElementType current : entry.getValue()) {

				final IGraphicalEditPart topNodeEditPart = createTopNodeEditPart(this.fixture.getActiveDiagram(), current, PapyrusEditorFixture.at(x_OnTopNode, y_OnTopNode), PapyrusEditorFixture.sized(topNodeSize, topNodeSize), topNodeResult);



				CreateableElementInsideMe creationResult = childrenResult.get(metaclassName);
				if (creationResult == null) {
					creationResult = new CreateableElementInsideMe(metaclassName, this.paletteReader.getMetaclassNodes());
					childrenResult.put(metaclassName, creationResult);
				}

				if (topNodeEditPart != null) {
					x_OnTopNode += topNodeSize + offset;
					if (x_OnTopNode >= 1200) {
						x_OnTopNode = 10;
						y_OnTopNode += topNodeSize + offset;
					}

					int depth = 1;
					createChildren(depth, topNodeEditPart, creationResult);
				}
			}
		}
	}

	protected int getMaxChildrenDepth() {
		return 2;
	}

	protected IGraphicalEditPart createTopNodeEditPart(final DiagramEditPart diagramEditPart, final IElementType elementType, final Point location, final Dimension dimension, final TopNodeCreationResult topNodeResult) {
		final IGraphicalEditPart createdEditPart = this.fixture.createShape(diagramEditPart, elementType, location, dimension);
		topNodeResult.registerResult(elementType, createdEditPart != null);
		if (topNodeResult != null) {
			this.fixture.flushDisplayEvents();
		}
		return createdEditPart;
	}

	protected void createChildren(final int depth, final IGraphicalEditPart parentEditPart, final CreateableElementInsideMe creationResult) {
		if (depth > getMaxChildrenDepth()) {
			return;
		}
		final Collection<IGraphicalEditPart> allCreatedSubEP = new ArrayList<>();
		int x_onSubNode = offset;
		int y_onSubNode = offset + 50;
		int subNodeSize = 60;

		for (final Entry<String, Collection<IElementType>> subEntry : this.paletteReader.getNodesElementsTypes().entrySet()) {
			for (final IElementType subNodeTestedElementType : subEntry.getValue()) {
				final Collection<IGraphicalEditPart> createdEP = createChildNodeOnEditpart(depth, parentEditPart, subNodeTestedElementType, PapyrusEditorFixture.at(x_onSubNode, y_onSubNode, parentEditPart),
						PapyrusEditorFixture.sized(subNodeSize, subNodeSize),
						creationResult);
				if (createdEP.size() > 0) {
					x_onSubNode += offset;
					y_onSubNode += offset;
				}
				allCreatedSubEP.addAll(createdEP);
			}
		}

		int nextDepth = depth + 1;
		if (nextDepth <= getMaxChildrenDepth()) {
			for (final IGraphicalEditPart subEP : allCreatedSubEP) {
				final String parentMetaclass = getMetaclassFromEditPart(subEP);
				CreateableElementInsideMe subCreationResult = childrenResult.get(parentMetaclass);
				if (subCreationResult == null) {
					subCreationResult = new CreateableElementInsideMe(parentMetaclass, this.paletteReader.getMetaclassNodes());
					childrenResult.put(parentMetaclass, subCreationResult);
				}
				createChildren(depth + 1, subEP, subCreationResult);
			}
		}
	}


	protected Collection<IGraphicalEditPart> createChildNodeOnEditpart(final int depth, final IGraphicalEditPart parentEditPart, final IElementType elementType, final Point location, final Dimension dimension, final CreateableElementInsideMe creationResult) {
		final Collection<IGraphicalEditPart> createdEditparts = new ArrayList<>();
		// 0. get the metaclass of the element type
		final String createdMetaclass = this.paletteReader.getMetaclass(elementType);

		// 1. we try to create the element directly on the edit part
		IGraphicalEditPart subNodeEditPart = null;
		try {
			subNodeEditPart = fixture.createShape(parentEditPart, elementType, location, dimension);
		} catch (Exception e) {
			Activator.log.error(e);
		}
		creationResult.registerCreatableElement(createdMetaclass, subNodeEditPart != null);
		if (subNodeEditPart != null) {
			createdEditparts.add(subNodeEditPart);
			this.fixture.flushDisplayEvents();
		}

		// 2. we try to create the element inside compartment
		for (Object compartment : parentEditPart.getChildren()) {
			if (compartment instanceof CompartmentEditPart) {
				IGraphicalEditPart createdEditPart = null;
				try {
					createdEditPart = this.fixture.createShape((IGraphicalEditPart) compartment, elementType, location, dimension);
				} catch (Exception e) {
					Activator.log.error(e);
				}
				creationResult.registerCreatableElement(createdMetaclass, createdEditPart != null);
				if (createdEditPart != null) {
					createdEditparts.add(createdEditPart);
					this.fixture.flushDisplayEvents();
				}
			}
		}
		return createdEditparts;
	}

	/**
	 * This methods tests the creation of edges between editparts existing the open diagrams
	 */
	protected void playWithEdges() {
		// 0. to avoid to have a dialog opening creating existing link!
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(PreferencesConstantsHelper.getPapyrusEditorConstant(PreferencesConstantsHelper.RESTORE_LINK_ELEMENT), false);

		int i = 0;
		int total = 0;

		// 1. get all nodes on the diagrams
		final DiagramEditPart diagramEP = this.fixture.getActiveDiagram();
		final Iterator<EditPart> iter = DiagramEditPartsUtil.getAllContents(diagramEP, false);

		final Map<EditPart, Boolean> allEditparts = new HashMap<>();
		final Set<String> topEPName = new TreeSet<>();
		while (iter.hasNext()) {
			final EditPart current = iter.next();
			final EditPart top = DiagramEditPartsUtil.getTopSemanticEditPart(current);
			if (top instanceof ConnectionEditPart) {
				allEditparts.put(top, Boolean.TRUE);
			} else {
				allEditparts.put(top, Boolean.FALSE);
			}
			topEPName.add(top.getClass().getCanonicalName());
		}

		final Map<String, String> edgesByPalettesToolsIDs = this.paletteReader.getEdgesToodIdsByMetaclass();
		System.out.println("nb source/target = " + allEditparts.size()); //$NON-NLS-1$
		System.out.println("nb links = " + edgesByPalettesToolsIDs.size()); //$NON-NLS-1$
		int nbCases = edgesByPalettesToolsIDs.size() * allEditparts.size() * allEditparts.size();
		System.out.println(NLS.bind("There are around {0} case to check.", Integer.valueOf(nbCases))); //$NON-NLS-1$


		for (final Entry<String, String> currentEntry : edgesByPalettesToolsIDs.entrySet()) {
			final String edgeMetaclassName = currentEntry.getKey();

			/*
			 * key : metaclass source,
			 * value : result of creation for all target EditPart, identified by the target metaclass
			 */
			final Map<String, LinkRepresentationCapability> linkCreationFromNodeSourceResult = new TreeMap<>();
			edgesWithSourceNodeCreationsResult.put(edgeMetaclassName, linkCreationFromNodeSourceResult);

			final Map<String, LinkRepresentationCapability> linkCreationFromEdgeSourceResult = new TreeMap<>();
			edgesWithSourceEdgeCreationsResult.put(edgeMetaclassName, linkCreationFromEdgeSourceResult);

			final Iterator<Entry<EditPart, Boolean>> sourceIter = allEditparts.entrySet().iterator();
			while (sourceIter.hasNext()) {
				// some user information!
				if (i > 100) {
					total += i;
					i = 0;
					System.out.println("nb tested CASE: " + total + "/" + nbCases); //$NON-NLS-1$ //$NON-NLS-2$
				}

				final Entry<EditPart, Boolean> currentEditPartEntry = sourceIter.next();
				final EditPart sourceEP = currentEditPartEntry.getKey();
				final String sourceMetaclass = getMetaclassFromEditPart(sourceEP);



				final Iterator<Entry<EditPart, Boolean>> targetIter = allEditparts.entrySet().iterator();

				// maybe we already tested a such metaclass (one time from topnode and now from child node for example)
				// yes, it is not really efficient, but at least, I sure of the result!
				LinkRepresentationCapability linkRep = null;
				if (currentEditPartEntry.getValue().booleanValue()) {
					// it is an edge
					linkRep = linkCreationFromEdgeSourceResult.get(sourceMetaclass);
					if (linkRep == null) {
						linkRep = new LinkRepresentationCapability(edgeMetaclassName, sourceMetaclass);
						linkCreationFromEdgeSourceResult.put(sourceMetaclass, linkRep);
					}
				} else {
					// it is a node
					linkRep = linkCreationFromNodeSourceResult.get(sourceMetaclass);
					if (linkRep == null) {
						linkRep = new LinkRepresentationCapability(edgeMetaclassName, sourceMetaclass);
						linkCreationFromNodeSourceResult.put(sourceMetaclass, linkRep);
					}
				}

				boolean result = false;

				while (targetIter.hasNext()) {
					final Entry<EditPart, Boolean> targetEntry = targetIter.next();
					final EditPart targetEP = targetEntry.getKey();
					final String targetMetaclass = getMetaclassFromEditPart(targetEP);
					i++;

					try {
						result = this.fixture.canCreateLink(currentEntry.getValue(), sourceEP, targetEP, PapyrusEditorFixture.at(0, 0), PapyrusEditorFixture.at(0, 0));
						if (sourceEP == targetEP) {
							linkRep.setReflexive(true);
						}
						linkRep.addTargetResult(targetMetaclass, result);
					} catch (Exception e) {
						org.eclipse.papyrus.uml.diagram.specs.extractor.Activator.log.error(e);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 *         the output folder path to use
	 */
	protected final String getOutputFolder() {
		String outputPath = Activator.getDefault().getPreferenceStore().getString(Activator.OUTPUT_PATH_DIRECTORY_PREFERENCE);
		while (outputPath == null || "".equals(outputPath)) { //$NON-NLS-1$
			DirectoryDialog dirDialog = new DirectoryDialog(Display.getDefault().getActiveShell());
			dirDialog.setFilterPath(null);

			dirDialog.setText("Select the output directory."); //$NON-NLS-1$
			dirDialog.setMessage("We are looking for the output directory to use for the generated files."); //$NON-NLS-1$

			outputPath = dirDialog.open();
		}
		Activator.getDefault().getPreferenceStore().setValue(Activator.OUTPUT_PATH_DIRECTORY_PREFERENCE, outputPath);

		return outputPath;
	}

	/**
	 * 
	 * @param ep
	 *            an edit part
	 * @return
	 *         the metaclass (tool palette label) for it, or null when not found
	 */
	protected String getMetaclassFromEditPart(final EditPart ep) {
		final IElementType elementType = getElementTypeFromEditPart(ep);
		if (elementType != null) {
			return this.paletteReader.getMetaclass(elementType);
		}
		return null;
	}

	/**
	 * 
	 * @param ep
	 *            an edit part
	 * @return
	 *         the elementType (tool palette label) for it, or null when not found
	 */
	protected IElementType getElementTypeFromEditPart(final EditPart ep) {
		final Object model = ep.getModel();
		IElementType result = null;
		if (model instanceof View) {
			result = VisualTypeService.getInstance().getElementType(this.fixture.getActiveDiagram().getDiagramView(), ((View) model).getType());
		}
		return result;
	}

	/**
	 * This method is in charge to write the output Excel file
	 */
	protected void writeExcelFile() {
		URI uri = URI.createFileURI(this.outputFolder);
		uri = uri.appendSegment(this.fileName);
		uri = uri.appendFileExtension(EXCEL_FILE_EXTENSION);
		final RequirementsExcelWriter writer = createRequirementsExcelWriter(uri.toFileString(), paletteReader);
		writer.setTopNodesCreationResult(getTopNodeBackgroundTitle(), this.topNodeResults);
		writer.setChildNodesCreationResult(this.childrenResult);
		writer.setEdgeCreationFromNodeResult(this.edgesWithSourceNodeCreationsResult);
		writer.setEdgeCreationFromEdgeResult(this.edgesWithSourceEdgeCreationsResult);
		writer.write();
	}

	/**
	 * 
	 * @param filePath
	 *            the path of the file to generate
	 * @param paletteReader
	 *            the palette reader
	 * @return
	 *         the Excel document writer
	 */
	protected RequirementsExcelWriter createRequirementsExcelWriter(final String filePath, final PaletteDescriptionReader paletteReader) {
		return new RequirementsExcelWriter(filePath, paletteReader);
	}

	/**
	 * 
	 * @return
	 *         the title of the column indicating the background element represented by the diagram
	 */
	protected abstract String getTopNodeBackgroundTitle();
}

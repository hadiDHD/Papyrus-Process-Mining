/******************************************************************************
 * Copyright (c) 2021 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.common.refresh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.draw2d.AbstractPointListShape;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.DisplayLabelSwitch;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.FilterService;
import org.eclipse.papyrus.sirius.uml.diagram.common.services.DiagramServices;
import org.eclipse.papyrus.sirius.uml.diagram.common.utils.ODesignConstant;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.util.RefreshIdsHolder;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DragAndDropTarget;
import org.eclipse.sirius.diagram.ResizeKind;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManagerRegistry;
import org.eclipse.sirius.diagram.business.api.refresh.IRefreshExtension;
import org.eclipse.sirius.diagram.business.internal.sync.DDiagramElementSynchronizer;
import org.eclipse.sirius.diagram.business.internal.sync.DDiagramSynchronizer;
import org.eclipse.sirius.diagram.business.internal.sync.DNodeCandidate;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusLayoutDataManager;
import org.eclipse.sirius.diagram.ui.business.internal.view.RootLayoutData;
import org.eclipse.sirius.diagram.ui.internal.refresh.GMFHelper;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.tools.api.SiriusPlugin;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;

/**
 * 
 * This class add the common bendpoints on sirius diagram
 *
 */
public class CommonDiagramRefreshExtension implements IRefreshExtension {

	private int bendpointDiameter = 7;


	/**
	 * @see org.eclipse.sirius.business.api.refresh.IRefreshExtension#beforeRefresh(org.eclipse.sirius.DDiagram)
	 */
	@Override
	public void beforeRefresh(DDiagram diagram) {
		long showStereotype = diagram.getActivatedLayers().stream().filter(layer -> ODesignConstant.APPLIED_STEREOTYPE_LAYER_ID.equals(layer.getName())).count(); //$NON-NLS-1$
		DisplayLabelSwitch.setStereotypeFilter(showStereotype == 1);
		
		long showQualifiedName = diagram.getActivatedLayers().stream().filter(layer -> ODesignConstant.QUALIFIED_NAMED_LAYER_ID.equals(layer.getName())).count(); //$NON-NLS-1$
		DisplayLabelSwitch.setQualifiedNameFilter(showQualifiedName == 1);
	}

	/**
	 * @see org.eclipse.sirius.business.api.refresh.IRefreshExtension#postRefresh(org.eclipse.sirius.DDiagram)
	 */
	@Override
	public void postRefresh(DDiagram diagram) {
		List<EObject> list = new ArrayList<>();
		diagram.eAllContents().forEachRemaining(list::add);
		// ResetStyleHelper.resetStyle(list);
		if (FilterService.INSTANCE.isBenpointFilterActivated(diagram)) {
			Collection<Point> bendpointsToDraw = getCommonBenpointsToDraw(diagram);
			Diagram gmfDiagram = SiriusGMFHelper.getGmfDiagram(diagram);
			Option<GraphicalEditPart> gef = GMFHelper.getGraphicalEditPart(gmfDiagram);
			if (gef.some()) {
				if (bendpointsToDraw.size() > 0) {
					drawCommonBendpoints((DSemanticDiagram) diagram, (IGraphicalEditPart) gef.get(), bendpointsToDraw);
				}
			}
		}
		// Not this one : DiagramHelper.refresh(gmfDiagram.get,true);
	}

	/**
	 *
	 * @param diagram
	 *
	 */
	protected Collection<Point> getCommonBenpointsToDraw(DDiagram diagram) {
		final Collection<Point> commonBendpointsToDraw = new HashSet<>();

		Diagram gmfDiagram = SiriusGMFHelper.getGmfDiagram(diagram);
		EList<Edge> edges = gmfDiagram.getEdges();
		for (Edge edge : edges) {
			// get the figure
			GraphicalEditPart currentEditPart = GMFHelper.getGraphicalEditPart(edge).get();
			IFigure currentEdgeFigure = ((IGraphicalEditPart) currentEditPart).getFigure();
			if (currentEdgeFigure instanceof AbstractPointListShape) {
				PointList bendPoints = ((AbstractPointListShape) currentEdgeFigure).getPoints();
				EClass eClass = null;
				if (currentEditPart instanceof IGraphicalEditPart) {
					final EObject el = ((IGraphicalEditPart) currentEditPart).resolveSemanticElement();
					if (el != null) {
						eClass = el.eClass();

						// 2. find all connections editpart with same source AND/OR same target than the
						// current one
						if (currentEditPart instanceof ConnectionEditPart && eClass != null) {
							final EditPart sourceEP = ((ConnectionEditPart) currentEditPart).getSource();
							final EditPart targetEP = ((ConnectionEditPart) currentEditPart).getTarget();
							final Set<Object> allConnectionsEP = new HashSet<>();
							// 2.1. get all potential editpart connections
							if (sourceEP instanceof AbstractGraphicalEditPart
									&& targetEP instanceof AbstractGraphicalEditPart) {
								allConnectionsEP.addAll(((AbstractGraphicalEditPart) sourceEP).getSourceConnections());
								allConnectionsEP.addAll(((AbstractGraphicalEditPart) targetEP).getSourceConnections());
								allConnectionsEP.addAll(((AbstractGraphicalEditPart) sourceEP).getTargetConnections());
								allConnectionsEP.addAll(((AbstractGraphicalEditPart) targetEP).getTargetConnections());
								allConnectionsEP.remove(currentEditPart);
							}

							// 2.2 get the figure for these connections and keep only figure when its
							// editpart has the same kind than the current one
							final Set<Connection> allConnections = new HashSet<>();
							if (allConnectionsEP.size() > 0) {
								for (final Object current : allConnectionsEP) {
									// the editpart be instance of the same class
									if (current.getClass().isInstance(currentEditPart)
											&& currentEditPart.getClass().isInstance(current)
											&& current instanceof IGraphicalEditPart) {
										final EObject resolvedElement = ((IGraphicalEditPart) current)
												.resolveSemanticElement();
										if (resolvedElement != null && eClass != null
												&& resolvedElement.eClass() != eClass) {
											continue;// we draw ben point only for elements which have the same eClass,
														// when
														// this
														// eClass is not null
										}
										final IFigure currentFig = ((IGraphicalEditPart) current).getFigure();
										if (currentFig instanceof Connection) {
											allConnections.add((Connection) currentFig);
										}
									}
								}
							}

							// 3. Create the list of the LineSeg of the current figure
							final List<LineSeg> refs = new ArrayList<>();
							for (int i = 0; i < bendPoints.size() - 1; i++) {
								LineSeg seg = new LineSeg(bendPoints.getPoint(i), bendPoints.getPoint(i + 1));
								refs.add(seg);
							}

							// 4. find common segments between the current figure and each others link
							// we need to associate each common segment to the concerned link
							final Map<Connection, Map<LineSeg, List<LineSeg>>> segs = new HashMap<>();
							for (Connection currentConn : allConnections) {
								final PointList currentPoints = currentConn.getPoints();
								final Map<LineSeg, List<LineSeg>> mapSegs = new HashMap<>();
								segs.put(currentConn, mapSegs);
								for (LineSeg refSeg : refs) {
									final List<LineSeg> commonSubSegs = new ArrayList<>();
									mapSegs.put(refSeg, commonSubSegs);
									for (int i = 0; i < currentPoints.size() - 1; i++) {
										LineSeg tmp = new LineSeg(currentPoints.getPoint(i),
												currentPoints.getPoint(i + 1));
										PointList intersection = getCommonSegment(refSeg, tmp);
										if (intersection.size() == 2) {
											if (!intersection.getFirstPoint().equals(intersection.getLastPoint())) {
												double distanceFromFirst = refSeg.getOrigin()
														.getDistance(intersection.getFirstPoint());
												double distanceFromSecond = refSeg.getOrigin()
														.getDistance(intersection.getLastPoint());
												final LineSeg commonSeg;
												// we arrange the 2 points in order to have the first point nearest of
												// the
												// start
												// of the current segment
												if (distanceFromFirst < distanceFromSecond) {
													commonSeg = new LineSeg(intersection.getFirstPoint(),
															intersection.getLastPoint());
												} else {
													commonSeg = new LineSeg(intersection.getLastPoint(),
															intersection.getFirstPoint());
												}
												commonSubSegs.add(commonSeg);
											}
										}
									}
								}
							}

							// 5. we look for the bendpoints crossing existing link on the model, then
							// crossing their common segment with the current figure, to find bendpoints to
							// draw
							for (Entry<Connection, Map<LineSeg, List<LineSeg>>> entry : segs.entrySet()) {
								final Map<LineSeg, List<LineSeg>> commonSegMap = entry.getValue();
								for (int i = 0; i < refs.size(); i++) { // we iterate on the segments of the current
																		// figure

									// 5.1 find required values to find bendpoints to draw
									final LineSeg currentFigureSeg = refs.get(i);
									final List<LineSeg> currentCommonSegs = commonSegMap.get(currentFigureSeg);

									final LineSeg previousSeg;
									final List<LineSeg> previousCommonSegs;

									final LineSeg nextSeg;
									final List<LineSeg> nextCommonSegs;

									// obtain previous segs of the current figure
									if (i != 0) {
										previousSeg = refs.get(i - 1);
										previousCommonSegs = commonSegMap.get(previousSeg);
									} else {
										previousSeg = null;
										previousCommonSegs = null;
									}

									// obtain next segs of the current figure
									if (i != refs.size() - 1) {
										nextSeg = refs.get(i + 1);
										nextCommonSegs = commonSegMap.get(nextSeg);
									} else {
										nextSeg = null;
										nextCommonSegs = null;
									}

									LineSeg previousCommonSeg = null;
									LineSeg nextCommonSeg = null;

									// we iterate on the common subsegment shared with others link with the current
									// figure
									for (int a = 0; a < currentCommonSegs.size(); a++) {
										final LineSeg curr = currentCommonSegs.get(a);
										final Point first = curr.getOrigin();
										final Point second = curr.getTerminus();
										// obtain previous common seg
										if (a == 0) {
											if (previousCommonSegs != null && previousCommonSegs.size() > 0) {
												previousCommonSeg = previousCommonSegs
														.get(previousCommonSegs.size() - 1);
											} else {
												previousCommonSeg = null;
											}
										} else {
											previousCommonSeg = currentCommonSegs.get(a - 1);
										}

										// obtain next common seg
										if (a == currentCommonSegs.size() - 1) {
											if (nextCommonSegs != null && nextCommonSegs.size() > 0) {
												nextCommonSeg = nextCommonSegs.get(0);
											} else {
												nextCommonSeg = null;
											}
										} else {
											nextCommonSeg = currentCommonSegs.get(a + 1);
										}

										// 5.2 calculates bendpoints visibility

										// determining if we draw first point :
										if (previousCommonSeg == null) {
											if (i == 0) {// first segment of the figure
												if (!bendPoints.getFirstPoint().equals(first)) {
													// we draw the point when it is not the first anchor of the figure
													commonBendpointsToDraw.add(first);
												}
											} else {
												commonBendpointsToDraw.add(first);
											}
										} else if (!previousCommonSeg.getTerminus().equals(first)) {
											// the previous common seg doesn't share this point with the current segment
											// we draw the first point
											commonBendpointsToDraw.add(first);
										}

										// determining if we draw the second point
										if (nextCommonSeg == null) {
											if (i == refs.size() - 1) {
												if (!bendPoints.getLastPoint().equals(second)) {
													// we draw the point when it is not the first anchor of the figure
													commonBendpointsToDraw.add(second);
												}
											} else {
												commonBendpointsToDraw.add(second);
											}
										} else if (!nextCommonSeg.getOrigin().equals(second)) {
											// the next common seg doesn't share this point with the current segment
											// we draw the second point
											commonBendpointsToDraw.add(second);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return commonBendpointsToDraw;
	}

	/**
	 *
	 * @param seg1
	 *            the first segment
	 * @param seg2
	 *            the secong segment
	 * @return
	 */
	public static final PointList getCommonSegment(final LineSeg seg1, final LineSeg seg2) {
		final List<Point> list = new ArrayList<>();
		list.add(seg1.getOrigin());
		list.add(seg2.getOrigin());
		list.add(seg1.getTerminus());
		list.add(seg2.getTerminus());

		List<Point> commonPoints = new ArrayList<>();
		for (Point point : list) {
			if (!commonPoints.contains(point)) {
				if (seg1.containsPoint(point, 0) && seg2.containsPoint(point, 0)) {
					commonPoints.add(point);
				}
			}
		}

		final PointList result = new PointList();
		for (final Point point : commonPoints) {
			result.addPoint(point);
		}
		return result;
	}

	/**
	 *
	 * @return <code>true</code> according to the preference store
	 */
	protected boolean getDrawBendpointPreferenceValue() {
		return true;
	}

	/**
	 * Calculate the best diameter and set the diameter value
	 *
	 * @param diameter
	 *            the diameter of the bendpoints (if diameter<=1, we set the
	 *            diameter to 0)
	 */
	public void setBendPointDiameter(final int diameter) {
		if (diameter <= 1) {
			this.bendpointDiameter = 0;
		}
		if (diameter % 2 != 0) {
			this.bendpointDiameter = diameter;
		} else {
			setBendPointDiameter(diameter + 1);
		}
	}

	/**
	 *
	 * @return the bendpoint diameter
	 */
	protected final int getBendPointDiameter() {
		return bendpointDiameter;
	}

	/**
	 *
	 * @param figure
	 *            graphics
	 * @param pointsToDraw
	 *            the list of the points to draw
	 */
	public void drawCommonBendpoints(DSemanticDiagram diagram, final IGraphicalEditPart gep,
			final Collection<Point> bendPoints) {

		// See PapyrusEdgeFigure for the initial algorithm.
		final int diameter = getBendPointDiameter();
		for (final Point point : bendPoints) {

			Point adjustedPoint = new Point((double) point.x - (double) bendpointDiameter / 2,
					(double) point.y - (double) bendpointDiameter / 2);
			NodeMapping mapping = null;

			// Before we were using
			// NodeMapping mapping= (NodeMapping)
			// DiagramServices.getDiagramServices().getMappingByName(diagram.getDescription(),
			// "Bendpoint");
			// but in papyrus context, description is not initialized as expected so we have
			// to use DiagramMappingsManager
			DiagramMappingsManager dmm = getMappingManager(diagram);
			for (NodeMapping map : dmm.getNodeMappings()) {
				if (ODesignConstant.BENDPOINT_MAPPING.equals(map.getName())) {
					mapping = map;
				}
			}
			// DNodeCandidate nodeCandidate = new DNodeCandidate(mapping, diagram.getTarget(), diagram, rId);
			if (mapping != null) { // some diagram doesn't have this mapping
				DNode node = createNode(mapping, diagram.getTarget(), diagram, diagram);
				node.setResizeKind(ResizeKind.NONE_LITERAL);
				// find how to set color
				Dimension dim = new Dimension(bendpointDiameter, bendpointDiameter);
				RootLayoutData layoutData = new RootLayoutData(node, adjustedPoint, dim);
				SiriusLayoutDataManager.INSTANCE.addData(layoutData);
			}
		}
	}

	public DNode createNode(NodeMapping mapping, EObject modelElement, DragAndDropTarget container, DDiagram diagram) {
		final DDiagram diag = diagram;

		ModelAccessor accessor = SiriusPlugin.getDefault().getModelAccessorRegistry().getModelAccessor(modelElement);
		IInterpreter interpreter = SiriusPlugin.getDefault().getInterpreterRegistry().getInterpreter(modelElement);
		final DDiagramSynchronizer diagramSync = new DDiagramSynchronizer(interpreter, diag.getDescription(), accessor);
		diagramSync.setDiagram((DSemanticDiagram) diagram);
		final DDiagramElementSynchronizer elementSync = diagramSync.getElementSynchronizer();
		RefreshIdsHolder rId = RefreshIdsHolder.getOrCreateHolder(diagram);

		DNodeCandidate nodeCandidate = new DNodeCandidate(mapping, modelElement, container, rId);
		return (DNode) elementSync.createNewNode(getMappingManager((DSemanticDiagram) diag), nodeCandidate, false);
	}

	private DiagramMappingsManager getMappingManager(final DSemanticDiagram diagram) {
		Session session = SessionManager.INSTANCE.getSession(diagram.getTarget());
		return DiagramMappingsManagerRegistry.INSTANCE.getDiagramMappingsManager(session, diagram);
	}

	/**
	 * @param targetDescription
	 * @param targetMappingName
	 * @return
	 */
	public DiagramElementMapping getMappingByName(RepresentationDescription targetDescription,
			String targetMappingName) {
		DiagramElementMapping mapping = null;

		if ((targetMappingName != null) && (targetDescription != null)
				&& (targetDescription instanceof DiagramDescription)) {
			mapping = DiagramServices.getDiagramServices()
					.getAbstractNodeMapping((DiagramDescription) targetDescription, targetMappingName);
			if (mapping == null) {
				mapping = DiagramServices.getDiagramServices().getEdgeMapping((DiagramDescription) targetDescription,
						targetMappingName);
			}
		}

		return mapping;
	}

}

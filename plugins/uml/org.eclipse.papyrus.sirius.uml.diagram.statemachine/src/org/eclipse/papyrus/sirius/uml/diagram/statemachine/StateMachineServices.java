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
 *    Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.statemachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.papyrus.editor.PapyrusMultiDiagramEditor;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationUtils;
import org.eclipse.papyrus.sirius.uml.diagram.statemachine.constants.SMD_MappingTypes;
import org.eclipse.papyrus.sirius.uml.diagram.statemachine.custom.StateMachineRegionPolicy;
import org.eclipse.papyrus.sirius.uml.diagram.statemachine.custom.StateMachineRegionPolicy.Zone;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.parsers.Messages;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.parsers.OpaqueBehaviorViewUtil;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.preferences.CSSOptionsConstants;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.preferences.PreferenceConstants;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.util.TriggerUtil;
import org.eclipse.papyrus.uml.diagram.statemachine.part.UMLDiagramEditorPlugin;
import org.eclipse.papyrus.uml.internationalization.utils.utils.UMLLabelInternationalization;
import org.eclipse.papyrus.uml.tools.utils.ValueSpecificationUtil;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.ContainerLayout;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.impl.DNodeContainerImpl;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusLayoutDataManager;
import org.eclipse.sirius.diagram.ui.business.internal.view.RootLayoutData;
import org.eclipse.sirius.diagram.ui.internal.refresh.GMFHelper;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.ChangeEvent;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.TimeEvent;
import org.eclipse.uml2.uml.TimeExpression;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * The services class used by VSM.
 */
@SuppressWarnings("restriction")
public class StateMachineServices {


	/**
	 * Checks if is initial pseudo state.
	 *
	 * @param object
	 *            the object
	 * @return true, if is initial pseudo state
	 */
	public boolean isInitialPseudoState(EObject object) {
		if (object instanceof Pseudostate) {
			if (((Pseudostate) object).getKind() == PseudostateKind.INITIAL_LITERAL) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check dropable element.
	 *
	 * @param dropable
	 *            the dropable
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	public boolean checkDropableElement(EObject dropable, EObject containerView) {
		if (dropable instanceof Region) {
			return checkDropableRegion(dropable, containerView);
		}
		if (dropable instanceof State) {
			return checkDropableState(dropable, containerView);
		}
		if (dropable instanceof StateMachine) {
			return checkDropableStateMachine(dropable, containerView);
		}
		if (dropable instanceof Pseudostate) {
			return checkDropablePseudostate(dropable, containerView);
		}
		if (dropable instanceof Constraint) {
			return checkDropableConstraint(dropable, containerView);
		}
		if (dropable instanceof Comment) {
			return checkDropableComment(dropable, containerView);
		}
		return false;
	}

	/**
	 * Check dropable comment.
	 *
	 * @param dropable
	 *            the dropable
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	private boolean checkDropableComment(EObject dropable, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DNodeContainer nodeContainerView = (DNodeContainer) containerView;
			EObject target = nodeContainerView.getTarget();
			if (target instanceof Region) {
				if (((Region) target).getOwnedComments().contains(dropable)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check dropable constraint.
	 *
	 * @param dropable
	 *            the dropable
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	private boolean checkDropableConstraint(EObject dropable, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DNodeContainer nodeContainerView = (DNodeContainer) containerView;
			EObject target = nodeContainerView.getTarget();
			if (target instanceof Region) {
				EObject stateParent = getStateParent(target);
				if (((State) stateParent).getOwnedRules().contains(dropable)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check dropable pseudostate.
	 *
	 * @param dropable
	 *            the dropable
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	private boolean checkDropablePseudostate(EObject dropable, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DNodeContainer nodeContainerView = (DNodeContainer) containerView;
			EObject target = nodeContainerView.getTarget();
			if (target instanceof Region) {
				if (((Region) target).getSubvertices().contains(dropable)) {
					return true;
				}
			}
			if (target instanceof State) {
				if (((State) target).getConnectionPoints().contains(dropable)) {
					return true;
				}
			}
			if (target instanceof StateMachine) {
				if (((StateMachine) target).getConnectionPoints().contains(dropable)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check dropable state.
	 *
	 * @param dropable
	 *            the dropable
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	private boolean checkDropableState(EObject dropable, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DNodeContainer nodeContainerView = (DNodeContainer) containerView;
			EObject target = nodeContainerView.getTarget();
			if (target instanceof Region) {
				if (((Region) target).getSubvertices().contains(dropable)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check dropable state machine.
	 *
	 * @param dropable
	 *            the dropable
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	private boolean checkDropableStateMachine(EObject dropable, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DNodeContainer nodeContainerView = (DNodeContainer) containerView;
			EObject target = nodeContainerView.getTarget();
			if (target instanceof Model) {
				if (((Model) target).getPackagedElements().contains(dropable)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check dropable region.
	 *
	 * @param dropable
	 *            the dropable
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	private boolean checkDropableRegion(EObject dropable, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DNodeContainer nodeContainerView = (DNodeContainer) containerView;
			EObject target = nodeContainerView.getTarget();
			if (target instanceof StateMachine) {
				if (((StateMachine) target).getRegions().contains(dropable)) {
					return true;
				}
			}
			if (target instanceof State) {
				if (((State) target).getRegions().contains(dropable)) {
					return true;
				}
			}
			if (target instanceof Region) {
				EObject eContainer = target.eContainer();
				if (eContainer instanceof StateMachine) {
					if (((StateMachine) eContainer).getRegions().contains(dropable)) {
						return true;
					}
				}
				if (eContainer instanceof State) {
					if (((State) eContainer).getRegions().contains(dropable)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the region parent.
	 *
	 * @param object
	 *            the object
	 * @return the region parent
	 */
	public EObject getRegionParent(EObject object) {
		if (object instanceof Region) {
			return object;
		}
		if (object instanceof StateMachine) {
			return ((StateMachine) object).getRegions().get(0);
		}
		if (object instanceof State) {
			return getRegionParent(object.eContainer());
		}
		if (object instanceof Pseudostate) {
			return getRegionParent(object.eContainer());
		}
		return null;
	}



	/**
	 * Gets the state parent.
	 *
	 * @param context
	 *            the context
	 * @return the state parent
	 */
	public EObject getStateParent(EObject context) {
		if (context instanceof State || context instanceof StateMachine) {
			return context;
		}
		if (context instanceof Region) {
			return getStateParent(context.eContainer());
		}
		return null;
	}

	/**
	 * Gets the features.
	 *
	 * @param object
	 *            the object
	 * @return the features
	 */
	public Collection<?> getFeatures(EObject object) {
		if (object instanceof Region) {
			return getFeatures(object.eContainer());
		}
		if (object instanceof State) {
			return ((State) object).getRegions();
		}
		if (object instanceof StateMachine) {
			return ((StateMachine) object).getRegions();
		}
		return null;

	}

	/**
	 * Gets the state view parent.
	 *
	 * @param context
	 *            the context
	 * @return the state view parent
	 */
	public EObject getStateViewParent(EObject context) {
		if (context instanceof DNodeContainer) {
			if (((DNodeContainer) context).getTarget() instanceof State) {
				return context;
			}
			if (((DNodeContainer) context).getTarget() instanceof StateMachine) {
				return context;
			}
			if (((DNodeContainer) context).getTarget() instanceof Region) {
				if (((DNodeContainer) context).getActualMapping().getName().contains("Horizontal") || ((DNodeContainer) context).getActualMapping().getName().contains("Vertical")) {
					return context;
				}
				return getStateViewParent(context.eContainer());
			}
		}
		return null;
	}


	/**
	 * Gets the state view parent.
	 *
	 * @param context
	 *            the context
	 * @return the state view parent
	 */
	private EObject getStateView(EObject context) {
		if (context instanceof DNodeContainer) {
			if (((DNodeContainer) context).getTarget() instanceof State) {
				return context;
			}
			if (((DNodeContainer) context).getTarget() instanceof Region) {
				return getStateView(context.eContainer());
			}
		}
		return null;
	}

	/**
	 * Sets the child representation.
	 *
	 * @param context
	 *            the context
	 * @param value
	 *            the value
	 * @return the e object
	 */
	public EObject setChildRepresentation(EObject context, String value) {
		if (context instanceof DNodeContainer) {
			if (value.equals("H")) {
				((DNodeContainer) context).setChildrenPresentation(ContainerLayout.HORIZONTAL_STACK);
			}
			if (value.equals("V")) {
				((DNodeContainer) context).setChildrenPresentation(ContainerLayout.VERTICAL_STACK);
			}
			if (value.equals("F")) {
				((DNodeContainer) context).setChildrenPresentation(ContainerLayout.FREE_FORM);
			}
		}
		return context;
	}





	/**
	 * Gets the container mapping.
	 *
	 * @param context
	 *            the context
	 * @param position
	 *            the position
	 * @return the container mapping
	 */
	public ContainerMapping getContainerMapping(DNodeContainer context, String position) {
		DNodeContainer stateViewParent = (DNodeContainer) getStateView(context);
		EList<ContainerMapping> subContainerMappings = stateViewParent.getActualMapping().getSubContainerMappings();
		for (ContainerMapping containerMapping : subContainerMappings) {
			if (containerMapping.getName().equals(position)) {
				return containerMapping;
			}
		}
		return null;
	}

	/**
	 * Sort element.
	 *
	 * @param object
	 *            the object
	 * @param containerView
	 *            the container view
	 * @return the e object
	 */
	public EObject sortElement(EObject object, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DNodeContainer container = (DNodeContainer) containerView;
			container.getActualMapping().setChildrenPresentation(null);
		}
		return object;
	}

	/**
	 * Checks if is vertical.
	 *
	 * @param object
	 *            the object
	 * @param containerView
	 *            the container view
	 * @return true, if is vertical
	 */
	public boolean isVertical(EObject object, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			View view = SiriusGMFHelper.getGmfView((DNodeContainer) containerView);
			Node gmfnode = (Node) view;
			String dropLocation = getDropLocation(gmfnode);
			if (Zone.isTop(dropLocation)) {
				return false;
			} else if (Zone.isLeft(dropLocation)) {
				return true;
			} else if (Zone.isRight(dropLocation)) {
				return true;
			} else if (Zone.isBottom(dropLocation)) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks if is horizontal.
	 *
	 * @param object
	 *            the object
	 * @param containerView
	 *            the container view
	 * @return true, if is vertical
	 */
	public boolean isHorizontal(EObject object, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			View view = SiriusGMFHelper.getGmfView((DNodeContainer) containerView);
			Node gmfnode = (Node) view;
			String dropLocation = getDropLocation(gmfnode);
			if (Zone.isTop(dropLocation)) {
				return true;
			} else if (Zone.isLeft(dropLocation)) {
				return false;
			} else if (Zone.isRight(dropLocation)) {
				return false;
			} else if (Zone.isBottom(dropLocation)) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Gets the drop location.
	 *
	 * @param object
	 *            the object
	 * @param containerView
	 *            the container view
	 * @return the drop location
	 */
	public String getDropLocation(EObject object, EObject containerView) {
		String result = "";
		if (containerView instanceof DNodeContainer) {
			View view = SiriusGMFHelper.getGmfView((DNodeContainer) containerView);
			Node gmfnode = (Node) view;
			result = getDropLocation(gmfnode);
		}
		return result;
	}


	/**
	 * Gets the freeform viewport.
	 *
	 * @param element
	 *            the element
	 * @return the freeform viewport
	 */
	private Rectangle getFreeformViewport(View element) {

		IEditorPart editor = EclipseUIUtil.getActiveEditor();
		if (editor instanceof PapyrusMultiDiagramEditor) {
			IEditorPart activeEditor = ((PapyrusMultiDiagramEditor) editor).getActiveEditor();

			final Map<?, ?> editPartRegistry = ((DiagramEditor) activeEditor).getDiagramGraphicalViewer().getEditPartRegistry();
			final Object editPart = editPartRegistry.get(element);
			if (editPart instanceof IGraphicalEditPart) {

				IGraphicalEditPart result = (IGraphicalEditPart) editPart;
				return result.getContentPane().getParent().getBounds();
			}
		}
		return null;

	}

	/**
	 * Gets the drop location.
	 *
	 * @param element
	 *            the element
	 * @return the drop location
	 */
	private String getDropLocation(View element) {

		Option<GraphicalEditPart> editPart = GMFHelper.getGraphicalEditPart(element);
		if (editPart.some()) {
			editPart.get();
			List<?> children = editPart.get().getChildren();
			for (Object object : children) {
				if (object instanceof EditPart) {
					EditPart part = (EditPart) object;
					EditPolicy editPolicy = part.getEditPolicy(EditPolicyRoles.CREATION_ROLE);
					if (editPolicy instanceof StateMachineRegionPolicy) {
						return ((StateMachineRegionPolicy) editPolicy).getDropLocation();
					}
				}
			}
		}
		return null;
	}


	/**
	 * Gets the size.
	 *
	 * @param object
	 *            the object
	 * @param object2
	 *            the object 2
	 * @param containerView
	 *            the container view
	 * @return the size
	 */
	public String getHeight(EObject object, EObject object2, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			View view = SiriusGMFHelper.getGmfView((DNodeContainer) containerView);
			Node gmfnode = (Node) view;
			Rectangle bounds = getFreeformViewport(gmfnode);
			return ((bounds.height + 1) / 10) + "";
		}
		return "-1";
	}

	/**
	 * Gets the width.
	 *
	 * @param object
	 *            the object
	 * @param object2
	 *            the object 2
	 * @param containerView
	 *            the container view
	 * @return the width
	 */
	public String getWidth(EObject object, EObject object2, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			View view = SiriusGMFHelper.getGmfView((DNodeContainer) containerView);
			Node gmfnode = (Node) view;
			Rectangle bounds = getFreeformViewport(gmfnode);
			return ((bounds.width + 1) / 10) + "";
		}

		return "-1";
	}


	/**
	 * Move children view.
	 *
	 * @param containerView
	 *            the container view
	 * @param newView
	 *            the new view
	 * @return the e object
	 */
	public EObject moveChildrenView(EObject containerView, EObject newView) {

		if (newView instanceof DNodeContainerImpl) {
			Collection<?> children = (Collection<?>) ((DNodeContainerImpl) containerView).eGet(DiagramPackage.DNODE_CONTAINER__OWNED_DIAGRAM_ELEMENTS, false, false);
			for (Object object : children) {
				if (object instanceof AbstractDNode) {
					View view = SiriusGMFHelper.getGmfView((AbstractDNode) object);
					Rectangle bounds = getFreeformViewport(view);
					ElementToRefresh.addPositionElement((AbstractDNode) object, bounds);
				}
			}

			List<Object> list = new ArrayList<>();
			Iterator<?> iterator = children.iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (next instanceof DNodeContainer && ((DNodeContainer) next).getTarget() instanceof Region) {
					continue;
				}
				if (!next.equals(containerView)) {
					list.add(next);
				}
			}

			((DNodeContainerImpl) newView).eSet(DiagramPackage.DNODE_CONTAINER__OWNED_DIAGRAM_ELEMENTS, list);
		}
		return newView;
	}


	/**
	 * Apply layout data.
	 *
	 * @param object
	 *            the object
	 * @param containerView
	 *            the container view
	 */
	public void applyLayoutData(EObject object, EObject containerView) {
		if (containerView instanceof DNodeContainer) {
			DDiagram parentDiagram = ((DNodeContainer) containerView).getParentDiagram();
			EList<DDiagramElement> diagramElements = parentDiagram.getDiagramElements();
			for (DDiagramElement dDiagramElement : diagramElements) {
				if ((dDiagramElement instanceof DNodeContainer) && ((DNodeContainer) dDiagramElement).getActualMapping().getName().equals("SMD_RegionCompartment")) {
					continue;
				}
				if (ElementToRefresh.toReposition.containsKey(dDiagramElement)) {
					Rectangle rectangle = ElementToRefresh.toReposition.get(dDiagramElement);
					Point point = new Point(rectangle.x, rectangle.y);
					Dimension dim = new Dimension(rectangle.width, rectangle.height);
					RootLayoutData layoutData = new RootLayoutData(dDiagramElement, point, dim);
					manageSubLayoutData(dDiagramElement);
					SiriusLayoutDataManager.INSTANCE.addData(layoutData);

				}
			}
		}
		ElementToRefresh.toReposition.clear();
	}


	/**
	 * Manage sub layout data.
	 *
	 * @param element
	 *            the element
	 */
	private void manageSubLayoutData(DDiagramElement element) {
		if (element instanceof DNodeContainer) {
			List<DDiagramElement> ownedDiagramElements = new ArrayList<>(((DNodeContainer) element).getOwnedDiagramElements());
			EList<DNode> ownedBorderedNodes = ((DNodeContainer) element).getOwnedBorderedNodes();
			ownedDiagramElements.addAll(ownedBorderedNodes);

			for (DDiagramElement dDiagramElement : ownedDiagramElements) {
				// if ((dDiagramElement instanceof DNodeContainer) && ((DNodeContainer) dDiagramElement).getActualMapping().getName().equals("SMD_RegionCompartment")) {
				// manageSubLayoutData(dDiagramElement);
				// continue;
				// }
				View view = SiriusGMFHelper.getGmfView(dDiagramElement);
				Rectangle rectangle = getFreeformViewport(view);
				Point point = new Point(rectangle.x, rectangle.y);
				Dimension dim = new Dimension(rectangle.width, rectangle.height);
				RootLayoutData subLayout = new RootLayoutData(dDiagramElement, point, dim);
				manageSubLayoutData(dDiagramElement);
				SiriusLayoutDataManager.INSTANCE.addData(subLayout);
			}
		}
	}



	/**
	 * Gets the children view.
	 *
	 * @param object
	 *            the object
	 * @return the children view
	 */
	public List<DDiagramElement> getChildrenView(EObject object) {

		if (object instanceof DNodeContainer) {
			DDiagramElement f;
			List<DDiagramElement> ownedDiagramElements = ((DNodeContainer) object).getOwnedDiagramElements().stream().filter(d -> (d instanceof DNodeContainer)).map(d -> (DNodeContainer) d)
					.filter(d -> d.getActualMapping().getName().equals(SMD_MappingTypes.REGION_COMPARTMENT))
					.collect(Collectors.toList());
			return ownedDiagramElements;
		}
		return null;
	}

	/**
	 * Gets the freeform viewport.
	 *
	 * @param containerView
	 *            the new bounds
	 * @return the freeform viewport
	 */
	public void setBounds(EObject containerView) {

		if (containerView instanceof DNodeContainer) {

			View view = SiriusGMFHelper.getGmfView((DNodeContainer) containerView);

			IEditorPart editor = EclipseUIUtil.getActiveEditor();
			if (editor instanceof DiagramEditor) {

				final Map<?, ?> editPartRegistry = ((DiagramEditor) editor).getDiagramGraphicalViewer().getEditPartRegistry();
				final AbstractGraphicalEditPart editPart = (AbstractGraphicalEditPart) editPartRegistry.get(view);
				EditPart parent = editPart.getParent();
				if (parent instanceof AbstractGraphicalEditPart) {
					IFigure figure = ((AbstractGraphicalEditPart) parent).getFigure();
					if (figure != null) {
						Rectangle bounds = figure.getBounds();
						Point point = new Point(bounds.x, bounds.y);
						Dimension dim = new Dimension(bounds.width, bounds.height);
						SiriusLayoutDataManager.INSTANCE.addData(new RootLayoutData(containerView, point, dim));
					}
				}



			}
		}
	}

	/**
	 * Return all constriantedElement for given Constraint <b> Exception for PartDeploymentLink (return its
	 * DeployedElement) <b>.
	 *
	 * @param object
	 *            the object
	 * @return the list
	 */
	public List<?> targeFinderExpressionForConstraint(EObject object) {
		List<EObject> result = new ArrayList<>();
		if (object instanceof Comment) {
			Comment comment = (Comment) object;

			EList<Element> annotatedElements = comment.getAnnotatedElements();
			for (Element element : annotatedElements) {
				if (element instanceof Region) {
					EObject stateParent = getStateParent(element);
					result.add(stateParent);
				} else {
					result.add(element);
				}
			}
		}
		if (object instanceof Constraint) {
			Constraint constraint = (Constraint) object;
			EList<Element> constrainedElements = constraint.getConstrainedElements();
			result.addAll(constrainedElements);
		}
		return result;
	}

	/**
	 * Gets the constraint label.
	 *
	 * @param object
	 *            the object
	 * @return the constraint label
	 */
	public String getConstraintLabel(EObject object) {

		if (object instanceof Constraint) {

			String name = ((Constraint) object).getName();
			EList<String> languages = null;
			EList<String> bodies = null;
			ValueSpecification specification = ((Constraint) object).getSpecification();
			if (specification instanceof OpaqueExpression) {
				languages = ((OpaqueExpression) specification).getLanguages();
				bodies = ((OpaqueExpression) specification).getBodies();
			}
			String langage = "";
			if (languages != null && !languages.isEmpty()) {
				langage = languages.get(0);
			}
			String body = "";
			if (bodies != null && !bodies.isEmpty()) {
				body = bodies.get(0);
			}
			String result = name + "\n" + "{{" + langage + "}" + body + "}";
			return result;
		}
		return "";
	}


	/**
	 * Sets the body.
	 *
	 * @param context
	 *            the context
	 * @param value
	 *            the value
	 * @return the e object
	 */
	public EObject setBody(EObject context, String value) {
		if (context instanceof Constraint) {
			ValueSpecification specification = ((Constraint) context).getSpecification();
			if (specification instanceof OpaqueExpression) {
				EList<String> bodies = ((OpaqueExpression) specification).getBodies();
				bodies.remove(0);
				bodies.add(0, value);
			}
		}
		return context;
	}



	/**
	 * Gets the body from constraint.
	 *
	 * @param object
	 *            the object
	 * @return the body from constraint
	 */
	public String getBodyFromConstraint(EObject object) {

		if (object instanceof Constraint) {

			EList<String> bodies = null;
			ValueSpecification specification = ((Constraint) object).getSpecification();
			if (specification instanceof OpaqueExpression) {
				bodies = ((OpaqueExpression) specification).getBodies();
			}
			String body = "";
			if (bodies != null && !bodies.isEmpty()) {
				body = bodies.get(0);
			}

			String result = body;
			return result;

		}
		return "";
	}

	/**
	 * Gets the context parent.
	 *
	 * @param element
	 *            the element
	 * @return the context parent
	 */
	public EObject getContextParent(EObject element) {

		if (element instanceof Constraint) {
			EObject eContainer = element.eContainer();
			return eContainer;
		}
		return null;
	}

	/**
	 * Check container view context.
	 *
	 * @param element
	 *            the element
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	public boolean checkContainerViewContext(EObject element, EObject containerView) {

		if (containerView instanceof DNode) {

			EObject eContainer = containerView.eContainer();
			if (eContainer instanceof DNodeContainer) {
				DNodeContainer stateViewParent = (DNodeContainer) getStateViewParent(eContainer);
				if (stateViewParent.getTarget().equals(element.eContainer())) {
					return false;
				}
			}

		}
		return true;

	}

	/**
	 * Gets the context parent view.
	 *
	 * @param object
	 *            the object
	 * @return the context parent view
	 */
	public EObject getContextParentView(EObject object) {

		if (object instanceof DNodeContainer) {
			return getStateViewParent(object);
		}
		if (object instanceof DNode) {
			return getContextParentView(object.eContainer());
		}
		return null;

	}

	/**
	 * Gets the pre condition.
	 *
	 * @param object
	 *            the object
	 * @return the pre condition
	 */
	public boolean getPreCondition(EObject object) {
		if (object instanceof Package) {
			return true;
		}
		if (object instanceof StateMachine) {
			return true;
		}
		if (object instanceof State) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the root.
	 *
	 * @param object
	 *            the object
	 * @return the root
	 */
	public EObject getRoot(EObject object) {

		if (object instanceof Model) {
			return object;
		} else if (object instanceof StateMachine) {
			return ((StateMachine) object).getModel();
		}
		return null;

	}

	/**
	 * Compute border line size.
	 *
	 * @param context
	 *            the context
	 * @param view
	 *            the view
	 * @return the int
	 */
	public int computeBorderLineSize(EObject context, EObject view) {
		if (view instanceof DNodeContainer) {
			if (((DNodeContainer) view).getActualMapping().getName().equals(SMD_MappingTypes.REGION_COMPARTMENT)) {
				if (((DNodeContainer) view).getElements().size() > 0) {
					return 0;
				}
			}

		}
		return 1;
	}


	/**
	 * Gets the other region.
	 *
	 * @param containerView
	 *            the container view
	 * @param object
	 *            the object
	 * @return the other region
	 */
	public EObject getOtherRegion(DNodeContainer containerView, EObject object) {

		EList<DDiagramElement> ownedDiagramElements = containerView.getOwnedDiagramElements();
		for (DDiagramElement dDiagramElement : ownedDiagramElements) {
			if (!dDiagramElement.getTarget().equals(object)) {
				return dDiagramElement;
			}
		}

		return null;

	}

	/**
	 * Save initial state machine.
	 *
	 * @param object
	 *            the object
	 */
	public void saveInitialStateMachine(EObject object) {
		if (object instanceof StateMachine) {
			ElementToRefresh.saveStateMachine((StateMachine) object);
		}
	}


	/**
	 * Gets the state element.
	 *
	 * @param container
	 *            the container
	 * @param containerView
	 *            the container view
	 * @return the state element
	 */
	public List<?> getStateElement(EObject container, EObject containerView) {

		if (containerView instanceof DSemanticDiagram) {
			EObject target = ((DSemanticDiagram) containerView).getTarget();
			if (target instanceof State) {
				List<EObject> list = new ArrayList<>();
				list.add(target);
				return list;
			}
		} else {
			if (containerView instanceof DNodeContainer) {
				EObject target = ((DNodeContainer) containerView).getTarget();
				if (target instanceof Region) {
					return ((Region) target).getSubvertices();
				}
			}
		}

		return null;
	}
	// aql:'/entry '+ self.getTypeName() + self.getName()

	/**
	 * Gets the type name.
	 *
	 * @param object
	 *            the object
	 * @return the type name
	 */
	public String getTypeName(EObject object) {

		String name = object.eClass().getName();

		return name;
	}

	/**
	 * Gets the name.
	 *
	 * @param object
	 *            the object
	 * @return the name
	 */
	public String getName(EObject object) {

		if (object instanceof NamedElement) {
			return ((NamedElement) object).getName();
		}

		return "";
	}

	/**
	 * get the text concerning guard.
	 *
	 * @param trans
	 *            the trans
	 * @return the text for guard
	 */
	public String getTextForGuard(Transition trans) {
		Constraint constraint = trans.getGuard();
		if (constraint != null) {
			String value;
			if (constraint.getSpecification() != null) {
				value = ValueSpecificationUtil.getConstraintnValue(constraint);
			} else {
				String name = UMLLabelInternationalization.getInstance().getLabel(constraint);
				if (name == null) {
					name = "<undef>"; //$NON-NLS-1$
				}
				value = String.format("%s (no spec)", name); //$NON-NLS-1$
			}
			if (value != null) {
				return String.format("[%s]", value); //$NON-NLS-1$
			}
		}
		return "";
	}


	/**
	 * Gets the value string.
	 *
	 * @param trans
	 *            the trans
	 * @param containerView
	 *            the container view
	 * @return the value string
	 */
	public String getValueString(Transition trans, EObject containerView) {
		StringBuilder result = new StringBuilder();
		View view = SiriusGMFHelper.getGmfView((DDiagramElement) containerView);
		String textForTrigger = getTextForTrigger(view, trans);
		if (textForTrigger != null && !"".equals(textForTrigger)) {
			result.append(textForTrigger);
		}
		result.append(getTextForGuard(trans));
		String textForEffect = getTextForEffect(view, trans);
		if (textForEffect != null && !"".equals(textForEffect)) {
			result.append("/"); //$NON-NLS-1$
			if (lineBreakBeforeEffect(view)) {
				result.append("\n"); //$NON-NLS-1$
			}
			result.append(textForEffect);
		}
		return result.toString();
	}

	/**
	 * Line break before effect.
	 *
	 * @param view
	 *            the view
	 * @return true, if the presence of parameters should be indicated by (...)
	 */
	public boolean lineBreakBeforeEffect(View view) {
		IPreferenceStore preferenceStore = UMLDiagramEditorPlugin.getInstance().getPreferenceStore();
		boolean prefValue = preferenceStore.getBoolean(PreferenceConstants.LINEBREAK_BEFORE_EFFECT);
		return NotationUtils.getBooleanValue(view, CSSOptionsConstants.LINEBREAK_BEFORE_EFFECT, prefValue);
	}

	/**
	 * get the text concerning Effects.
	 *
	 * @param view
	 *            the view
	 * @param trans
	 *            the trans
	 * @return the text for effect
	 */
	protected String getTextForEffect(View view, Transition trans) {
		StringBuilder result = new StringBuilder();
		Behavior effect = trans.getEffect();
		if (effect != null) {
			EClass eClass = effect.eClass();
			if (effect instanceof OpaqueBehavior) {
				OpaqueBehavior ob = (OpaqueBehavior) effect;
				if (ob.getBodies().size() > 0) {
					// return body of behavior (only handle case of a single body)
					result.append(OpaqueBehaviorViewUtil.retrieveBody(view, ob));
					return result.toString();
				}
			}
			if (eClass != null) {
				result.append(eClass.getName()).append(": ").append(UMLLabelInternationalization.getInstance().getLabel(effect)); //$NON-NLS-1$
			}
		}
		return result.toString();
	}

	/**
	 * Get the text concerning Trigger.
	 *
	 * @param view
	 *            the view
	 * @param trans
	 *            the trans
	 * @return the text for trigger
	 */
	protected String getTextForTrigger(View view, Transition trans) {
		StringBuilder result = new StringBuilder();
		boolean isFirstTrigger = true;
		for (Trigger t : trans.getTriggers()) {
			if (t != null) {
				if (!isFirstTrigger) {
					result.append(", "); //$NON-NLS-1$
				} else {
					isFirstTrigger = false;
				}
				Event e = t.getEvent();
				if (e instanceof CallEvent) {
					Operation op = ((CallEvent) e).getOperation();
					if (op != null) {
						result.append(UMLLabelInternationalization.getInstance().getLabel(op));
						if ((op.getOwnedParameters().size() > 0) && OpaqueBehaviorViewUtil.displayParamDots(view)) {
							result.append(OpaqueBehaviorViewUtil.PARAM_DOTS);
						}
					} else {
						result.append(UMLLabelInternationalization.getInstance().getLabel((e)));
					}
				} else if (e instanceof SignalEvent) {
					Signal signal = ((SignalEvent) e).getSignal();
					if (signal != null) {
						result.append(UMLLabelInternationalization.getInstance().getLabel(signal));
						if ((signal.getAttributes().size() > 0) && OpaqueBehaviorViewUtil.displayParamDots(view)) {
							result.append(OpaqueBehaviorViewUtil.PARAM_DOTS);
						}
					} else {
						result.append(UMLLabelInternationalization.getInstance().getLabel((e)));
					}
				} else if (e instanceof ChangeEvent) {
					ValueSpecification vs = ((ChangeEvent) e).getChangeExpression();
					String value;
					if (vs instanceof OpaqueExpression) {
						value = OpaqueBehaviorViewUtil.retrieveBody(view, (OpaqueExpression) vs);
					} else {
						value = vs.stringValue();
					}
					result.append(value);
				} else if (e instanceof TimeEvent) {
					TimeEvent timeEvent = (TimeEvent) e;
					// absRelPrefix
					result.append(timeEvent.isRelative() ? "after " : "at "); //$NON-NLS-1$ //$NON-NLS-2$
					// body
					TimeExpression te = timeEvent.getWhen();
					String value;
					if (te != null) {
						ValueSpecification vs = te.getExpr();
						if (vs instanceof OpaqueExpression) {
							value = OpaqueBehaviorViewUtil.retrieveBody(view, (OpaqueExpression) vs);
						} else {
							value = vs.stringValue();
						}
					} else {
						value = "undefined"; //$NON-NLS-1$
					}
					result.append(value);
				} else { // any receive event
					result.append("all"); //$NON-NLS-1$
				}
			}
		}
		return result.toString();
	}


	/**
	 * Gets the text for trigger.
	 *
	 * @param view
	 *            the view
	 * @param trigger
	 *            the trigger
	 * @return the text for trigger
	 */
	public String getTextForTrigger(View view, Trigger trigger) {
		StringBuilder result = new StringBuilder();
		if (trigger != null) {
			Event e = trigger.getEvent();
			if (e instanceof CallEvent) {
				Operation op = ((CallEvent) e).getOperation();
				if (op != null) {
					result.append(UMLLabelInternationalization.getInstance().getLabel(op));
					if ((op.getOwnedParameters().size() > 0) && OpaqueBehaviorViewUtil.displayParamDots(view)) {
						result.append(OpaqueBehaviorViewUtil.PARAM_DOTS);
					}
				} else {
					result.append(UMLLabelInternationalization.getInstance().getLabel((e)));
				}
			} else if (e instanceof SignalEvent) {
				Signal signal = ((SignalEvent) e).getSignal();
				if (signal != null) {
					result.append(UMLLabelInternationalization.getInstance().getLabel(signal));
					if ((signal.getAttributes().size() > 0) && OpaqueBehaviorViewUtil.displayParamDots(view)) {
						result.append(OpaqueBehaviorViewUtil.PARAM_DOTS);
					}
				} else {
					result.append(UMLLabelInternationalization.getInstance().getLabel((e)));
				}
			} else if (e instanceof ChangeEvent) {
				ValueSpecification vs = ((ChangeEvent) e).getChangeExpression();
				String value;
				if (vs instanceof OpaqueExpression) {
					value = OpaqueBehaviorViewUtil.retrieveBody(view, (OpaqueExpression) vs);
				} else {
					value = vs.stringValue();
				}
				result.append(value);
			} else if (e instanceof TimeEvent) {
				TimeEvent timeEvent = (TimeEvent) e;
				// absRelPrefix
				result.append(timeEvent.isRelative() ? "after " : "at "); //$NON-NLS-1$ //$NON-NLS-2$
				// body
				TimeExpression te = timeEvent.getWhen();
				String value;
				if (te != null) {
					ValueSpecification vs = te.getExpr();
					if (vs instanceof OpaqueExpression) {
						value = OpaqueBehaviorViewUtil.retrieveBody(view, (OpaqueExpression) vs);
					} else {
						value = vs.stringValue();
					}
				} else {
					value = "undefined"; //$NON-NLS-1$
				}
				result.append(value);
			} else { // any receive event
				result.append("all"); //$NON-NLS-1$
			}
		}
		return result.toString();
	}

	/**
	 * Gets the text trigger.
	 *
	 * @param trigger
	 *            the trigger
	 * @param containerView
	 *            the container view
	 * @return the text trigger
	 */
	public String getTextTrigger(Trigger trigger, EObject containerView) {
		StringBuilder result = new StringBuilder();
		View view = SiriusGMFHelper.getGmfView((DDiagramElement) containerView);
		if (trigger.getEvent() != null) {
			result.append(TriggerUtil.getTextForTrigger(view, trigger));
		}
		result.append(" ");
		result.append(Messages.DeferrableTriggerParser_DEFER_KEYWORD);
		return result.toString();
	}

	/**
	 * Checks for graphical region.
	 *
	 * @param containerView
	 *            the container view
	 * @return true, if successful
	 */
	public boolean hasGraphicalRegion(DNodeContainer containerView) {
		EList<DDiagramElement> ownedDiagramElements = containerView.getOwnedDiagramElements();
		for (DDiagramElement dDiagramElement : ownedDiagramElements) {
			if (dDiagramElement instanceof DNodeContainer) {
				if (dDiagramElement.getTarget() instanceof Region) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Dnd border pseudo state.
	 *
	 * @param context
	 *            the context
	 * @param newContainer
	 *            the new container
	 */
	public void dndBorderPseudoState(EObject context, EObject newContainer) {
		if (context instanceof Pseudostate) {
			if (newContainer instanceof State) {
				((State) newContainer).getConnectionPoints().add((Pseudostate) context);
			}
			if (newContainer instanceof Region) {
				EObject stateParent = getStateParent(context);
				((State) stateParent).getConnectionPoints().add((Pseudostate) context);

			}
		}

	}

}

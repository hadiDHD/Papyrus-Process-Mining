/******************************************************************************
 * Copyright (c) 2009, 2022 Obeo Designer, CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Obeo - initial API and implementation
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - adaptation to integrate in Papyrus
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.clazz.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.Activator;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.ui.dialog.AssociationSelectionDialog;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.utils.InstanceSpecificationLinkUtils;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.AssociationServices;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.DirectEditLabelSwitch;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.DisplayLabelSwitch;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.EditLabelSwitch;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.ElementServices;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.ILabelConstants;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.LabelServices;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.NodeInverseRefsServices;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.OperationServices;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.StereotypeServices;
import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.UIServices;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeContainerSpec;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeListSpec;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.FontFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Feature;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.InformationFlow;
import org.eclipse.uml2.uml.InformationItem;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PackageMerge;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;

import com.google.common.collect.Lists;

/**
 * Services to handle typed Element concerns.
 *
 */
public class ClassDiagramServices {
	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final ClassDiagramServices INSTANCE = new ClassDiagramServices();

	/** Dialog CANCEL button label */
	private static final String CANCEL_LABEL = "Cancel";

	/** Dialog OK button label */
	private static final String OK_LABEL = "OK";

	/** Annotation generic source name */
	private static final String ANNOTATION_GENERIC_SOURCE = "org.eclipse.papyrus";

	/** Annotation InstanceEnd source name */
	private static final String ANNOTATION_INSTANCE_END_SOURCE = InstanceSpecificationLinkUtils.INSTANCE_END;

	/**
	 * index used to find the source of an {@link InstanceSpecification} link
	 */
	private static final int INSTANCE_SPECIFICATION_LINK__SOURCE_INDEX = 0;

	/**
	 * index used to find the target of an {@link InstanceSpecification} link
	 */
	private static final int INSTANCE_SPECIFICATION_LINK__TARGET_INDEX = 1;

	/** Annotation detail key */
	private static final String ANNOTATION_DETAIL_KEY = "nature";

	/** Annotation detail value */
	private static final String ANNOTATION_DETAIL_VALUE = "UML_Nature";

	/** underscore separator */
	private static final String UNDERSCORE = "_";

	/**
	 * index used to find the source of the {@link GeneralizationSet}
	 */
	private static final int GENERALIZATION_SET__SOURCE_INDEX = 0;

	/**
	 * index used to find the target of the {@link GeneralizationSet}
	 */
	private static final int GENERALIZATION_SET__TARGET_INDEX = 1;

	/**
	 * Move the given Element
	 * 
	 * @param semanticObjectToDrop
	 *            semantic element to drop
	 * @param targetContainerView
	 *            the target container view
	 * @return the element
	 */
	public EObject dndElement(EObject semanticObjectToDrop, EObject targetContainerView) {
		if ((targetContainerView instanceof DSemanticDecorator) && (semanticObjectToDrop instanceof PackageableElement)) {
			Element element = (PackageableElement) semanticObjectToDrop;
			EObject target = ((DSemanticDecorator) targetContainerView).getTarget();

			Model targetModel = null;
			if (target instanceof Model) {
				targetModel = (Model) target;
			}
			if (targetModel != null) {
				targetModel.getPackagedElements().add((PackageableElement) element);
			}

		}
		return semanticObjectToDrop;
	}

	/**
	 * Get diagram root.
	 * 
	 * @param context
	 *            the current context (view)
	 * @return the root diagram owning the element
	 */
	private static EObject getDiagramRoot(EObject sourceView) {
		DDiagramElement diagramElement = (DDiagramElement) sourceView;
		DSemanticDiagram diagram = (DSemanticDiagram) diagramElement.getParentDiagram();
		EObject root = diagram.getTarget();
		return root;
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Abstraction}
	 * @return
	 *         {@link Abstraction} available in the context
	 */
	public Collection<Abstraction> abstraction_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package pack = (Package) semanticContext;
			return getAllAbstractions(pack);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param pack
	 *            a UML {@link Package}
	 * @return
	 *         all {@link Abstraction} recursively
	 */
	private static final Collection<Abstraction> getAllAbstractions(final Package pack) {
		final Collection<Abstraction> abstractions = new HashSet<Abstraction>();
		final Iterator<NamedElement> iter = pack.getMembers().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Package) {
				abstractions.addAll(getAllAbstractions((Package) current));
			}
			if (current instanceof Abstraction) {
				abstractions.add((Abstraction) current);
			}
		}
		return abstractions;
	}

	/**
	 * Service used to determinate if the selected {@link Abstraction} source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean abstraction_canReconnectSource(final Element context, final Element newSource) {
		return newSource instanceof Class
				|| newSource instanceof Enumeration
				|| newSource instanceof Interface
				|| newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected {@link Abstraction} target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean abstraction_canReconnectTarget(final Element context, final Element newTarget) {
		// same conditions for source and target
		return abstraction_canReconnectSource(context, newTarget);
	}

	/**
	 * Service used to reconnect a {@link Abstraction} source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void abstraction_reconnectSource(final Element context, final Element oldSource, final Element newSource) {
		dependency_reconnectSource(context, oldSource, newSource);
	}

	/**
	 * Service used to reconnect a {@link Abstraction} target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void abstraction_reconnectTarget(final Element context, final Element oldTarget, final Element newTarget) {
		dependency_reconnectTarget(context, oldTarget, newTarget);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Constraint}
	 * @param diagram
	 *            the current diagram
	 * @return
	 *         the Constraints available in the context
	 */
	public Collection<EObject> constraint_getSemanticCandidates(final EObject semanticContext, final DDiagram diagram) {
		final Collection<EObject> constraints = new HashSet<EObject>();

		// we show in the diagram all Constraint owned directly by the diagram
		if (semanticContext instanceof Namespace) {
			constraints.addAll(((Namespace) semanticContext).getOwnedRules());
		}
		if (semanticContext instanceof Package) {
			final Package pack = (Package) semanticContext;
			constraints.addAll(pack.getPackagedElements().stream().filter(Constraint.class::isInstance).collect(Collectors.toList()));
		}

		// we show in the diagram all Constraint owned by a element represented in the diagram
		// this behavior avoid to make disappear the constraint of the diagram when the user create a ContextLink between the Constraint and a Namespace
		for (final DDiagramElement diagramElement : diagram.getOwnedDiagramElements()) {
			final EObject current = diagramElement.getTarget();

			if (current instanceof Namespace) {
				// a context link has been created
				constraints.addAll(((Namespace) current).getOwnedRules());
			}
			if (current instanceof Package) {
				// a context link has not yet been created Contraint#context==null
				constraints.addAll(((Package) current).getPackagedElements().stream().filter(Constraint.class::isInstance).collect(Collectors.toList()));
			}
		}

		return constraints;
	}

	/**
	 * This method is in charge to move semantically the target element inside the source element
	 * 
	 * @param context
	 *            the current context
	 * @param source
	 *            the source element
	 * @param target
	 *            the target element
	 */
	public static void containmentLink_creation(final EObject context, final Element source, final Element target) {
		if (source instanceof Package && target instanceof PackageableElement) {
			((Package) source).getPackagedElements().add((PackageableElement) target);
		} else if (source instanceof Class && target instanceof Classifier) {
			((Class) source).getNestedClassifiers().add((Classifier) target);
		}
	}

	/**
	 * Check if the source and target are valid for a ContainmentLink
	 * 
	 * @param context
	 *            the current context
	 * @param sourceView
	 *            the source view
	 * @param targetView
	 *            the target view
	 * @param source
	 *            the semantic source element
	 * @param target
	 *            the semantic target element
	 * @return true if the source and target are valid
	 */
	public boolean containmentLink_isValidSourceAndTarget(final EObject context, final EObject sourceView, final EObject targetView, final Element source, final Element target) {
		boolean isValid = false;
		if (source == target) {
			// 1. we forbid reflexive ContainmentLink of course
			return false;
		}

		// 2. semantic condition
		if (source instanceof Package) {
			isValid = target instanceof PackageableElement;
		} else if (source instanceof Class) {
			isValid = target instanceof Classifier;
		}

		if (isValid) {
			// 3. to avoid a loop of containment
			isValid = !((Namespace) source).allNamespaces().contains(target);
		}

		// 4. we only allow to target an element on the diagram background (to ease Diagram development)
		final EObject graphicalParent = ((DNodeContainer) targetView).eContainer();
		isValid = graphicalParent instanceof DSemanticDiagram;

		return isValid;

	}

	/**
	 * Get the target elements for the containment link.
	 * 
	 * @param context
	 *            the current context
	 * @return containment link lists
	 */
	public static List<?> containmentLink_getTarget(final Element source) {
		if (source instanceof Class) {
			return ((Class) source).getNestedClassifiers();
		} else if (source instanceof Package) {
			return ((Package) source).getPackagedElements();
		}
		return null;
	}

	/**
	 * This is the reconnect source precondition method.
	 * 
	 * @param oldSource
	 *            the old source element
	 * @param newSourceView
	 *            the new source view
	 * @param newSource
	 *            the new source element
	 * @param otherEnd
	 *            the other end view (the target View)
	 * @return
	 *         <code>true</code> if we can change the source of the ContainmentLink
	 */
	public boolean containmentLink_canReconnectSource(final Element oldSource, final DNodeContainer newSourceView, final Element newSource, final DNodeContainer otherEnd) {
		final Element target = (Element) otherEnd.getTarget();
		return containmentLink_isValidSourceAndTarget(null, null, otherEnd, newSource, target);
	}

	/**
	 * This is the reconnect target precondition method.
	 * 
	 * @param oldTarget
	 *            the old target element
	 * @param newTargetView
	 *            the new target view
	 * @param newTarget
	 *            the new target element
	 * @param otherEnd
	 *            the other end view (the source View)
	 * @return
	 *         <code>true</code> if we can change the target of the ContainmentLink
	 */
	public boolean containmentLink_canReconnectTarget(final Element oldTarget, final DNodeContainer newTargetView, final Element newTarget, final DNodeContainer otherEnd) {
		final Element source = (Element) otherEnd.getTarget();
		return containmentLink_isValidSourceAndTarget(null, null, newTargetView, source, newTarget);
	}

	/**
	 * Service used to reconnect a Containment Link source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param edgeView
	 *            Represents the graphical new edge
	 * @param oldSourceView
	 *            Represents the graphical element pointed by the edge before reconnecting
	 * @param newSourceView
	 *            Represents the graphical element pointed by the edge after reconnecting
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void containmentLink_reconnectSource(final Element context, final DEdge edgeView, final EdgeTarget oldSourceView, final EdgeTarget newSourceView, final Element oldSource, final Element newSource) {
		// 1. get the target element of the ContainmentLink and move it into its new container
		final Element targetElement = (Element) ((DNodeContainer) edgeView.getTargetNode()).getTarget();
		// 2. move the element in its new owner
		containmentLink_creation(null, newSource, targetElement);
	}

	/**
	 * Service used to reconnect a Containment Link target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param edgeView
	 *            Represents the graphical new edge
	 * @param oldTargetView
	 *            Represents the graphical element pointed by the edge before reconnecting
	 * @param newTargetView
	 *            Represents the graphical element pointed by the edge after reconnecting
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void containmentLink_reconnectTarget(final Element context, final DEdge edgeView, final EdgeTarget oldTargetView, final EdgeTarget newTargetView, final Element oldTarget, final Element newTarget) {
		// 1. reparent the old target element at the semantic level to get a consistent diagram
		final EObject graphicalParent = ((DNodeContainer) oldTargetView).eContainer();
		Assert.isTrue(graphicalParent instanceof DSemanticDiagram); // always true with the current configuration
		final EObject semanticGraphicalParent = ((DSemanticDiagram) graphicalParent).getTarget();
		Assert.isTrue(semanticGraphicalParent instanceof Package && oldTarget instanceof PackageableElement); // always true in with current odesign configuration
		((Package) semanticGraphicalParent).getPackagedElements().add((PackageableElement) oldTarget);

		// 2. move the element in its new parent
		containmentLink_creation(null, context, newTarget);
	}

	/**
	 * Check if the source and target are valid for a ContextLink
	 * 
	 * @param context
	 *            the current context
	 * @param sourceView
	 *            the source view
	 * @param targetView
	 *            the target view
	 * @param source
	 *            the semantic source element
	 * @param target
	 *            the semantic target element
	 * @return true if the source and target are valid
	 */
	public boolean contextLink_isValidSourceAndTarget(final EObject context, final EObject sourceView, final EObject targetView, final Element source, final Element target) {
		boolean isValid = false;
		if (source == target) {
			// 1. we forbid reflexive Context of course
			return false;
		}

		// 2. semantic condition
		if (source instanceof Constraint) {
			isValid = target instanceof Namespace;
		}

		return isValid;
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Dependency}
	 * @return
	 *         all {@link Dependency} available in the context
	 */
	public Collection<Dependency> dependency_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package pack = (Package) semanticContext;
			return getAllDependencies(pack);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param pack
	 *            a UML {@link Package}
	 * @return
	 *         all owned {@link Dependency} recursively
	 */
	private static final Collection<Dependency> getAllDependencies(final Package pack) {
		final Collection<Dependency> dependencies = new HashSet<Dependency>();
		final Iterator<NamedElement> iter = pack.getMembers().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Package) {
				dependencies.addAll(getAllDependencies((Package) current));
			}
			if (current instanceof Dependency) {
				dependencies.add((Dependency) current);
			}
		}
		return dependencies;
	}

	/**
	 * Service used to determinate if the selected {@link Dependency} source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean dependency_canReconnectSource(final Element context, final Element newSource) {
		return newSource instanceof Class
				|| newSource instanceof Enumeration
				|| newSource instanceof Interface
				|| newSource instanceof Package
				|| newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected {@link Dependency} target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean dependency_canReconnectTarget(final Element context, final Element newTarget) {
		// same condition than for source
		return dependency_canReconnectSource(context, newTarget);
	}

	/**
	 * Service used to reconnect a {@link Dependency} source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void dependency_reconnectSource(final Element context, final Element oldSource, final Element newSource) {
		final Dependency dependency = (Dependency) context;
		// 1. change the client of the dependency
		dependency.getClients().remove(oldSource);
		dependency.getClients().add((NamedElement) newSource);

		// 2. the dependency is owned by the nearest package of the source
		final Package newOwner = newSource.getNearestPackage();
		if (dependency.getOwner() != newOwner) {
			newOwner.getPackagedElements().add(dependency);
		}
	}

	/**
	 * Service used to reconnect a Containment target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void dependency_reconnectTarget(final Element context, final Element oldTarget, final Element newTarget) {
		final Dependency dependencyEdge = (Dependency) context;
		dependencyEdge.getSuppliers().remove(oldTarget);
		dependencyEdge.getSuppliers().add((NamedElement) newTarget);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link ElementImport}
	 * @return
	 *         {@link ElementImport} available in the context
	 */
	public Collection<ElementImport> elementImport_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Namespace) {
			final Namespace namespace = (Namespace) semanticContext;
			return getAllElementImports(namespace);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param namespace
	 *            a UML {@link Namespace}
	 * @return
	 *         all {@link ElementImport} recursively
	 */
	private static final Collection<ElementImport> getAllElementImports(final Namespace namespace) {
		final Collection<ElementImport> elementImports = new HashSet<ElementImport>();
		elementImports.addAll(namespace.getElementImports());
		final Iterator<NamedElement> iter = namespace.getOwnedMembers().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Namespace) {
				elementImports.addAll(getAllElementImports((Namespace) current));
			}
		}
		return elementImports;
	}

	/**
	 * Service used to determine if the selected ElementImport source edge target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean elementImport_canReconnectSource(Element context, Element newSource) {
		return newSource instanceof Class
				|| newSource instanceof Enumeration
				|| newSource instanceof Interface
				|| newSource instanceof Package
				|| newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected ElementImport target edge target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean elementImport_canReconnectTarget(Element context, Element newTarget) {
		// same conditions for source and target
		return elementImport_canReconnectSource(context, newTarget);
	}

	/**
	 * Service used to reconnect an ElementImport edge source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 */
	public void elementImport_reconnectSource(final Element context, final Element oldSource, final Element newSource) {
		final ElementImport elementImport = (ElementImport) context;
		((Namespace) oldSource).getElementImports().remove(elementImport);
		((Namespace) newSource).getElementImports().add(elementImport);
	}

	/**
	 * Service used to reconnect an ElementImport edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge reconnecting
	 * @param oldTarget
	 *            Represents the semantic element pointer by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void elementImport_reconnectTarget(final Element context, final Element oldTarget, final Element newTarget) {
		final ElementImport elementImportEdge = (ElementImport) context;
		elementImportEdge.setImportedElement((PackageableElement) newTarget);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Generalization}
	 * @return
	 *         {@link Generalization} available in the context
	 */
	public Collection<Generalization> generalization_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Namespace) {
			final Namespace namespace = (Namespace) semanticContext;
			return getAllGeneralizations(namespace);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param namespace
	 *            a UML {@link Namespace}
	 * @return
	 *         all {@link Generalization} recursively
	 */
	private static final Collection<Generalization> getAllGeneralizations(final Namespace namespace) {
		final Collection<Generalization> generalizations = new HashSet<Generalization>();
		final Iterator<NamedElement> iter = namespace.getOwnedMembers().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Classifier) {
				generalizations.addAll(((Classifier) current).getGeneralizations());
			}
			if (current instanceof Namespace) {
				generalizations.addAll(getAllGeneralizations((Namespace) current));
			}
		}
		return generalizations;
	}

	/**
	 * Service used to determine if the selected Generalization edge source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean generalization_canReconnectSource(final Element context, final Element newSource) {
		return (newSource instanceof Class
				|| newSource instanceof Interface
				|| newSource instanceof Enumeration
				|| newSource instanceof PrimitiveType);
	}

	/**
	 * Service used to determine if the selected Generalization edge target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean generalization_canReconnectTarget(final Element context, final Element newTarget) {
		// same condition for source and target
		return generalization_canReconnectSource(context, newTarget);
	}

	/**
	 * Service used to reconnect a Generalization source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void generalization_reconnectSource(final Element context, final Element oldSource, final Element newSource) {
		final Generalization generalization = (Generalization) context;
		((Classifier) oldSource).getGeneralizations().remove(generalization);
		((Classifier) newSource).getGeneralizations().add(generalization);
	}

	/**
	 * Service used to reconnect a Generalization target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void generalization_reconnectTarget(final Element context, final Element oldTarget, final Element newTarget) {
		final Generalization generalizationEdge = (Generalization) context;
		generalizationEdge.setGeneral((Classifier) newTarget);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Generalization}
	 * @return
	 *         {@link Generalization} available in the context
	 */
	public Collection<GeneralizationSet> generalizationSet_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package pack = (Package) semanticContext;
			return getAllGeneralizationSets(pack);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param pack
	 *            a UML {@link Package}
	 * @return
	 *         all {@link GeneralizationSet} recursively
	 */
	private static final Collection<GeneralizationSet> getAllGeneralizationSets(final Package pack) {
		final Collection<GeneralizationSet> generalizationSets = new HashSet<GeneralizationSet>();
		final Iterator<PackageableElement> iter = pack.getPackagedElements().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof GeneralizationSet) {
				generalizationSets.add(((GeneralizationSet) current));
			}
			if (current instanceof Package) {
				generalizationSets.addAll(getAllGeneralizationSets((Package) current));
			}
		}
		return generalizationSets;
	}

	/**
	 * 
	 * @param generalizationSet
	 *            a {@link GeneralizationSet}
	 * @return
	 *         the {@link Generalization} to use as source or <code>null</code>
	 */
	public Generalization generalizationSet_getSource(final GeneralizationSet generalizationSet) {
		if (generalizationSet.getGeneralizations().size() >= 1) {
			return generalizationSet.getGeneralizations().get(GENERALIZATION_SET__SOURCE_INDEX);
		}
		return null;
	}

	/**
	 * 
	 * @param generalizationSet
	 *            a {@link GeneralizationSet}
	 * @return
	 *         the {@link Generalization} to use as target or <code>null</code>
	 */
	public Generalization generalizationSet_getTarget(final GeneralizationSet generalizationSet) {
		if (generalizationSet.getGeneralizations().size() >= 2) {
			return generalizationSet.getGeneralizations().get(GENERALIZATION_SET__TARGET_INDEX);
		}
		return null;
	}

	/**
	 * Service used to reconnect a GeneralizationSet edge source.
	 *
	 * @param generalizationSet
	 *            {@link GeneralizationSet} attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void generalizationSet_reconnectSource(final GeneralizationSet generalizationSet, final Generalization oldSource, final Generalization newSource) {
		// 1. update the source
		generalizationSet.getGeneralizations().remove(oldSource);
		generalizationSet.getGeneralizations().add(GENERALIZATION_SET__SOURCE_INDEX, newSource);

		// 2. update the parent
		newSource.getNearestPackage().getPackagedElements().add(generalizationSet);

		// 3. build new name for the generalization set
		final String oldSourceName = oldSource.getGeneral().getName();
		final String newSourceName = newSource.getGeneral().getName();

		final String newName = generalizationSet.getName().replace(oldSourceName, newSourceName);
		generalizationSet.setName(newName);
	}

	/**
	 * Service used to reconnect a GeneralizationSet edge target.
	 *
	 * @param generalizationSet
	 *            {@link GeneralizationSet} attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void generalizationSet_reconnectTarget(final GeneralizationSet generalizationSet, final Generalization oldTarget, final Generalization newTarget) {
		// 1. update the target
		generalizationSet.getGeneralizations().remove(oldTarget);
		generalizationSet.getGeneralizations().add(GENERALIZATION_SET__TARGET_INDEX, newTarget);

		// 2. build new name for the generalization set
		final String oldTargetName = oldTarget.getGeneral().getName();
		final String newTargetName = newTarget.getGeneral().getName();

		final String newName = generalizationSet.getName().replace(oldTargetName, newTargetName);
		generalizationSet.setName(newName);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Generalization}
	 * @return
	 *         all {@link InformationFlow} available in the context
	 */
	public Collection<InformationFlow> informationFlow_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package namespace = (Package) semanticContext;
			return getAllInformationFlows(namespace);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param namespace
	 *            a UML {@link Namespace}
	 * @return
	 *         all {@link InformationFlow} recursively
	 */
	private static final Collection<InformationFlow> getAllInformationFlows(final Package namespace) {
		final Collection<InformationFlow> informationFlows = new HashSet<InformationFlow>();
		final Iterator<PackageableElement> iter = namespace.getPackagedElements().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof InformationFlow) {
				informationFlows.add((InformationFlow) current);
			}
			if (current instanceof Package) {
				informationFlows.addAll(getAllInformationFlows((Package) current));
			}
		}
		return informationFlows;
	}

	/**
	 * Service used to determine if the selected InformationFlow source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * 
	 * @return true if the edge could be reconnected
	 */
	public boolean informationFlow_canReconnectSource(final InformationFlow context, final Element newSource) {
		return newSource instanceof Class
				|| newSource instanceof Enumeration
				|| newSource instanceof Interface
				|| newSource instanceof Package
				|| newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected InformationFlow target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * 
	 * @return true if the edge could be reconnected
	 */
	public boolean informationFlow_canReconnectTarget(final InformationFlow context, final Element newTarget) {
		// same condition for source and target
		return informationFlow_canReconnectSource(context, newTarget);
	}

	/**
	 * Service used to reconnect an InformationFlow source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void informationFlow_reconnectSource(final InformationFlow context, final NamedElement oldSource, final NamedElement newSource) {
		// 1. update source
		context.getInformationSources().remove(oldSource);
		context.getInformationSources().add(newSource);

		// 2. update container
		final Package owner = newSource.getNearestPackage();
		owner.getPackagedElements().add(context);
	}

	/**
	 * Service used to reconnect an informationFlow target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void informationFlow_reconnectTarget(final InformationFlow context, final NamedElement oldTarget, final NamedElement newTarget) {
		context.getInformationTargets().remove(oldTarget);
		context.getInformationTargets().add(newTarget);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Generalization}
	 * @return
	 *         All {@link InstanceSpecification} available in the context
	 */
	public Collection<InstanceSpecification> instanceSpecificationLink_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package namespace = (Package) semanticContext;
			return getAllInstanceSpecificationLinks(namespace);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param pack
	 *            a UML {@link Namespace}
	 * @return
	 *         all {@link InstanceSpecification} recursively
	 */
	private final Collection<InstanceSpecification> getAllInstanceSpecificationLinks(final Package pack) {
		final Collection<InstanceSpecification> instanceSpecifications = new HashSet<InstanceSpecification>();
		final Iterator<PackageableElement> iter = pack.getPackagedElements().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (instanceSpecification_isLink(current)) {
				instanceSpecifications.add((InstanceSpecification) current);
			}
			if (current instanceof Package) {
				instanceSpecifications.addAll(getAllInstanceSpecificationLinks((Package) current));
			}
		}
		return instanceSpecifications;
	}

	/**
	 * Check if the current element is instance of instance specification Link or
	 * Class.
	 * 
	 * @param elem
	 *            the current element to be checked
	 * @return true if instance specification Class, otherwise return false
	 */
	public boolean instanceSpecification_isLink(final EObject elem) {
		if (elem instanceof InstanceSpecification) {
			final InstanceSpecification instanceSpecification = (InstanceSpecification) elem;
			return instanceSpecification.getEAnnotation(ANNOTATION_INSTANCE_END_SOURCE) != null;
		}
		return false;
	}

	/**
	 * Check if the current element is instance of instance specification Link or
	 * Class.
	 * 
	 * @param elem
	 *            the current element to be checked
	 * @return true if instance specification Class, otherwise return false
	 */
	public boolean instanceSpecification_isNode(Element elem) {
		return !instanceSpecification_isLink(elem);
	}

	/**
	 * Create a new Instance Specification link.
	 * This code is adapted/duplicated from the Papyrus Service Edit
	 * see org.eclipse.papyrus.uml.service.types.internal.ui.commands.InstanceSpecificationLinkCreateCommand and
	 * see org.eclipse.papyrus.uml.service.types.internal.ui.advice.InstanceSpecificationLinkEditHelperAdvice
	 * 
	 * @param context
	 *            the context element
	 * @param sourceView
	 *            the source view
	 * @param source
	 *            the semantic source element
	 * @param target
	 *            the semantic target element
	 * @return the created {@link InstanceSpecification}
	 */
	public InstanceSpecification instanceSpecificationLink_createInstanceSpecificationLink(EObject context, EObject sourceView, InstanceSpecification source, InstanceSpecification target) {
		final ServicesRegistry registry = getServiceRegistry(context);
		InstanceSpecification instanceSpecification = null;
		if (registry != null) {
			final AssociationSelectionDialog dialog = new AssociationSelectionDialog(Display.getCurrent().getActiveShell(), SWT.NATIVE, InstanceSpecificationLinkUtils.getModelAssociations(source, target), registry);
			dialog.open();
			final Association selectedAssociation = dialog.getSelectedAssociation();
			if (selectedAssociation != null && !dialog.isCanceled()) {
				instanceSpecification = UMLFactory.eINSTANCE.createInstanceSpecification();
				Set<Classifier> sourceSpecificationClassifiersSet = InstanceSpecificationLinkUtils.getSpecificationClassifier(source);
				Set<Classifier> targetSpecificationClassifiersSet = InstanceSpecificationLinkUtils.getSpecificationClassifier(target);
				boolean revertEnds = false;
				if (selectedAssociation != null) {
					instanceSpecification.getClassifiers().add(selectedAssociation);
					Type sourceType = selectedAssociation.getMemberEnds().get(0).getType();
					revertEnds = false == sourceSpecificationClassifiersSet.contains(sourceType);
				}
				if (revertEnds) {
					InstanceSpecificationLinkUtils.addEnd(instanceSpecification, target);
					InstanceSpecificationLinkUtils.addEnd(instanceSpecification, source);
				} else {
					InstanceSpecificationLinkUtils.addEnd(instanceSpecification, source);
					InstanceSpecificationLinkUtils.addEnd(instanceSpecification, target);
				}
				InstanceSpecificationLinkUtils.setupSlots(selectedAssociation, instanceSpecification, source, target, sourceSpecificationClassifiersSet, targetSpecificationClassifiersSet);

			}
		}

		// add the created instance specification into the nearest package of the source
		source.getNearestPackage().getPackagedElements().add(instanceSpecification);

		return instanceSpecification;
	}

	/**
	 * Service used to determine if the selected {@link InstanceSpecification} source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean instanceSpecificationLink_canReconnectSource(final Element context, final Element newSource) {
		return newSource instanceof InstanceSpecification;
	}

	/**
	 * Service used to determine if the selected {@link InstanceSpecification} target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean instanceSpecificationlink_canReconnectSource(final Element context, final Element newTarget) {
		return newTarget instanceof InstanceSpecification;
	}

	/**
	 * Service used to reconnect an InstanceSpecifictaion source
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void instanceSpecificationLink_reconnectSource(final InstanceSpecification context, final Element oldSource, final Element newSource) {
		final EAnnotation eAnnotation = context.getEAnnotation(ANNOTATION_INSTANCE_END_SOURCE);
		eAnnotation.getReferences().remove(oldSource);
		eAnnotation.getReferences().add(INSTANCE_SPECIFICATION_LINK__SOURCE_INDEX, (InstanceSpecification) newSource);

		final Package newOwner = newSource.getNearestPackage();
		newOwner.getPackagedElements().add(context);
	}

	/**
	 * Service used to reconnect an InstanceSpecifictaion target
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void instanceSpecificationLink_reconnectTarget(final InstanceSpecification context, final Element oldTarget, final Element newTarget) {
		final EAnnotation eAnnotation = context.getEAnnotation(ANNOTATION_INSTANCE_END_SOURCE);
		eAnnotation.getReferences().remove(oldTarget);
		eAnnotation.getReferences().add(INSTANCE_SPECIFICATION_LINK__TARGET_INDEX, (InstanceSpecification) newTarget);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link InterfaceRealization}
	 * @return
	 *         all {@link InterfaceRealization} available in the context
	 */
	public Collection<InterfaceRealization> interfaceRealization_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package pack = (Package) semanticContext;
			return getAllInterfaceRealizations(pack);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param pack
	 *            a UML {@link Package}
	 * @return
	 *         all {@link InterfaceRealization} recursively
	 */
	private final Collection<InterfaceRealization> getAllInterfaceRealizations(final Package pack) {
		final Collection<InterfaceRealization> interfaceRealizations = new HashSet<InterfaceRealization>();
		final Iterator<PackageableElement> iter = pack.getPackagedElements().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Package) {
				interfaceRealizations.addAll(getAllInterfaceRealizations((Package) current));
			}
			if (current instanceof BehavioredClassifier) {
				interfaceRealizations.addAll(((BehavioredClassifier) current).getInterfaceRealizations());
			}
		}
		return interfaceRealizations;
	}

	/**
	 * Create a new {@link InterfaceRealization}
	 * 
	 * @param context
	 *            the context element
	 * @param sourceView
	 *            the source view
	 * @param source
	 *            the source element
	 * @param target
	 *            the target element
	 * @return
	 *         the created {@link InterfaceRealization}
	 */
	public InterfaceRealization interfaceRealization_createInterfaceRealization(final EObject context, final EObject sourceView, final Element source, final Element target) {
		InterfaceRealization interfaceRealization = null;
		if (sourceView instanceof DDiagramElement) {
			if (source instanceof BehavioredClassifier && target instanceof Interface) {
				final BehavioredClassifier sourceElement = (BehavioredClassifier) source;
				final Interface targetElement = (Interface) target;
				interfaceRealization = UMLFactory.eINSTANCE.createInterfaceRealization();
				interfaceRealization.getClients().add(sourceElement);
				interfaceRealization.getSuppliers().add(targetElement);
				interfaceRealization.setContract(targetElement);
				sourceElement.getInterfaceRealizations().add(interfaceRealization);
			}
		}
		return interfaceRealization;
	}

	/**
	 * Service used to determine if the selected {@link InterfaceRealization} source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean interfaceRealization_canReconnectSource(Element context, Element newSource) {
		return newSource instanceof BehavioredClassifier;
	}

	/**
	 * Service used to determine if the selected {@link InterfaceRealization} target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the target element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean interfaceRealization_canReconnectTarget(final Element context, final Element newTarget) {
		return newTarget instanceof Interface;
	}

	/**
	 * Service used to reconnect a {@link InterfaceRealization} source.
	 *
	 * @param interfaceRealization
	 *            {@link InterfaceRealization} attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void interfaceRealization_reconnectSource(final InterfaceRealization interfaceRealization, final BehavioredClassifier oldSource, final BehavioredClassifier newSource) {
		interfaceRealization.getClients().remove(oldSource);
		interfaceRealization.getClients().add((NamedElement) newSource);

		oldSource.getInterfaceRealizations().remove(interfaceRealization);
		newSource.getInterfaceRealizations().add(interfaceRealization);
	}

	/**
	 * Service used to reconnect a {@link InterfaceRealization} target.
	 *
	 * @param interfaceRealization
	 *            {@link InterfaceRealization} attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void interfaceRealization_reconnectTarget(final InterfaceRealization interfaceRealization, final Interface oldTarget, final Interface newTarget) {
		interfaceRealization.getSuppliers().remove(oldTarget);
		interfaceRealization.getSuppliers().add((NamedElement) newTarget);
		interfaceRealization.setContract(newTarget);
	}

	/**
	 * Create a new Link link.
	 * 
	 * @param sourceView
	 *            the source view
	 * @param source
	 *            the semantic source element
	 * @param target
	 *            the semantic target element
	 */
	public void link_creation(EObject context, EObject sourceView, Element source, Element target) {
		if (source instanceof Comment) {
			((Comment) source).getAnnotatedElements().add(target);
		} else if (source instanceof Constraint) {
			((Constraint) source).getConstrainedElements().add(target);
		}
	}

	/**
	 * Get the target element of the Link.
	 * 
	 * @param source
	 *            the element ({@link Comment} or {@link Constraint}
	 * @return
	 *         the list of annotated elements if source if a {@link Comment} and the list of constrained elements if the source is a {@link Constraint} and <code>null</code> in other cases
	 */
	public static Collection<Element> link_getTarget(final Element source) {
		if (source instanceof Constraint) {
			final Constraint sourceElement = (Constraint) source;
			return sourceElement.getConstrainedElements();
		} else if (source instanceof Comment) {
			final Comment sourceElement = (Comment) source;
			return sourceElement.getAnnotatedElements();
		}
		return null;
	}

	/**
	 * Service used to determine if the selected Link edge source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean link_canReconnectSource(final Element context, final Element newSource) {
		// we want to avoid to change the semantic of a link : either is works on Constraint, either it works on Comment
		if (context instanceof Constraint && newSource instanceof Constraint) {
			return true;
		}
		if (context instanceof Comment && newSource instanceof Comment) {
			return true;
		}
		return false;
	}

	/**
	 * Service used to determine if the selected Link edge target could be
	 * reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean link_canReconnectTarget(Element context, Element newSource) {
		return newSource instanceof Class
				|| newSource instanceof Comment
				|| newSource instanceof Constraint
				|| newSource instanceof Enumeration
				|| newSource instanceof Interface
				|| newSource instanceof Package
				|| newSource instanceof PrimitiveType;
	}



	/**
	 * Service used to reconnect a Link source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldsource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @param otherEnd
	 *            Represents the view attached to the target of the link
	 */
	public void link_reconnectSource(final Element context, final Element oldSource, final Element newSource, final EObject otherEnd) {
		Element target = null;
		if (otherEnd instanceof DSemanticDecorator) {
			target = (Element) ((DSemanticDecorator) otherEnd).getTarget();
		}

		// remove the target from the old source
		if (oldSource instanceof Comment) {
			((Comment) oldSource).getAnnotatedElements().remove(target);
		} else if (oldSource instanceof Constraint) {
			((Constraint) oldSource).getConstrainedElements().remove(target);
		}

		// add the target to the new source
		if (newSource instanceof Comment) {
			((Comment) newSource).getAnnotatedElements().add(target);
		} else if (newSource instanceof Constraint) {
			((Constraint) newSource).getConstrainedElements().add(target);
		}
	}

	/**
	 * Service used to reconnect a Link edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @param otherEnd
	 *            Represents the view attached to the source of the link
	 */
	public void link_reconnectTarget(final Element context, final Element oldTarget, final Element newTarget, final EObject otherEnd) {
		Element source = null;
		if (otherEnd instanceof DSemanticDecorator) {
			source = (Element) ((DSemanticDecorator) otherEnd).getTarget();
		}

		if (source instanceof Comment) {
			((Comment) source).getAnnotatedElements().remove(oldTarget);
			((Comment) source).getAnnotatedElements().add(newTarget);
		} else if (source instanceof Constraint) {
			((Constraint) source).getConstrainedElements().remove(oldTarget);
			((Constraint) source).getConstrainedElements().add(newTarget);
		}
	}

	/**
	 * This method returns all {@link PackageImport} found in the context
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link PackageImport}
	 * @return
	 *         all {@link PackageImport} available in the context
	 */
	public Collection<PackageImport> packageImport_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package pack = (Package) semanticContext;
			return getAllPackageImports(pack);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param pack
	 *            a UML {@link Package}
	 * @return
	 *         all {@link PackageImport} recursively
	 */
	private final Collection<PackageImport> getAllPackageImports(final Package pack) {
		final Collection<PackageImport> packageImports = new HashSet<PackageImport>();
		packageImports.addAll(pack.getPackageImports());
		final Iterator<PackageableElement> iter = pack.getPackagedElements().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Package) {
				packageImports.addAll(getAllPackageImports((Package) current));
			}
		}
		return packageImports;
	}

	/**
	 * Service used to determine if the selected {@link PackageImport} source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean packageImport_canReconnectSource(final Element context, final Element newSource) {
		if (context instanceof PackageImport && newSource instanceof Package) {
			final PackageImport pImport = (PackageImport) context;
			final Package newOwner = (Package) newSource;
			final Package importedPackage = pImport.getImportedPackage();
			return newOwner != null && newOwner != importedPackage;
		}
		return false;
	}

	/**
	 * Service used to determine if the selected {@link PackageImport} target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the target element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean packageImport_canReconnectTarget(final Element context, final Element newTarget) {
		if (context instanceof PackageImport && newTarget instanceof Package) {
			final PackageImport pImport = (PackageImport) context;
			final Package owner = (Package) pImport.getOwner();
			return newTarget != null && newTarget != owner;
		}
		return false;
	}

	/**
	 * Service used to reconnect an PackageImport edge source.
	 *
	 * @param packageImport
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void packageImport_reconnectSource(final PackageImport packageImport, final Package oldSource, final Package newSource) {
		oldSource.getPackageImports().remove(packageImport);
		newSource.getPackageImports().add(packageImport);
	}

	/**
	 * Service used to reconnect a PackageImport edge target.
	 *
	 * @param packageImport
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void packageImport_reconnectTarget(final PackageImport packageImport, final Package oldTarget, final Package newTarget) {
		packageImport.setImportedPackage(newTarget);
	}

	/**
	 * This method returns all {@link PackageMerge} found in the context
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link PackageMerge}
	 * @return
	 *         all {@link PackageMerge} available in the context
	 */
	public Collection<PackageMerge> packageMerge_getSemanticCandidates(final EObject semanticContext) {
		if (semanticContext instanceof Package) {
			final Package pack = (Package) semanticContext;
			return getAllPackageMerges(pack);
		}
		return Collections.emptyList();
	}

	/**
	 * 
	 * @param pack
	 *            a UML {@link Package}
	 * @return
	 *         all {@link PackageMerge} recursively
	 */
	private final Collection<PackageMerge> getAllPackageMerges(final Package pack) {
		final Collection<PackageMerge> packageImports = new HashSet<PackageMerge>();
		packageImports.addAll(pack.getPackageMerges());
		final Iterator<PackageableElement> iter = pack.getPackagedElements().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Package) {
				packageImports.addAll(getAllPackageMerges((Package) current));
			}
		}
		return packageImports;
	}

	/**
	 * Service used to determine if the selected {@link PackageMerge} edge source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean packageMerge_canReconnectSource(final Element context, final Element newSource) {
		if (context instanceof PackageMerge && newSource instanceof Package) {
			final PackageMerge pMerge = (PackageMerge) context;
			final Package newOwner = (Package) newSource;
			final Package mergedPackage = pMerge.getMergedPackage();
			return newOwner != null && newOwner != mergedPackage;
		}
		return false;
	}

	/**
	 * Service used to determine if the selected {@link PackageMerge} edge target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the target element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean packageMerge_canReconnectTarget(final Element context, final Element newTarget) {
		if (context instanceof PackageMerge && newTarget instanceof Package) {
			final PackageMerge pImport = (PackageMerge) context;
			final Package owner = (Package) pImport.getOwner();
			return newTarget != null && newTarget != owner;
		}
		return false;
	}

	/**
	 * Service used to reconnect a PackageMerge edge source.
	 *
	 * @param packageMerge
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void packageMerge_reconnectSource(final PackageMerge packageMerge, final Package oldSource, final Package newSource) {
		oldSource.getPackageMerges().remove(packageMerge);
		newSource.getPackageMerges().add(packageMerge);
	}

	/**
	 * Service used to reconnect a PackageMerge edge target.
	 *
	 * @param packageMerge
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void packageMerge_reconnectTarget(final PackageMerge packageMerge, final Package oldTarget, final Package newTarget) {
		packageMerge.setMergedPackage(newTarget);
	}

	/**
	 * Precondition test if sirius diagram or not.
	 * 
	 * @param context
	 *            the current context
	 * @return true if context is a DDiagram
	 */
	public boolean isDDiagram(EObject self) {
		return self instanceof DDiagram;
	}

	/**
	 * Precondition check is data type.
	 * 
	 * @param context
	 *            the current context
	 * @return true is DataType and not enumeration or PrimitiveType
	 */
	public boolean isDataType(EObject context) {
		return !(context instanceof Enumeration || context instanceof PrimitiveType);
	}

	/**
	 * Get the feature to contain the element
	 * 
	 * @param context
	 *            the current context
	 * @return the target feature
	 */
	public String getTypeTarget(EObject context) {
		if (context instanceof Class || context instanceof Interface) {
			return "nestedClassifier";
		}

		return "packagedElement";
	}

	/**
	 * Get the feature to contain the element
	 * 
	 * @param context
	 *            the current context
	 * @param newContainerView
	 *            the new container view
	 * @return the target name
	 */
	public String getTypeTarget(EObject context, EObject newContainerView) {
		if (newContainerView instanceof Class || newContainerView instanceof Interface) {
			return "nestedClassifier";
		}

		return "packagedElement";
	}

	/**
	 * Create a new Association Class Link.
	 * 
	 * @param sourceView
	 *            the source view
	 * @param target
	 *            the semantic target element
	 * @param targetView
	 *            the target view
	 */
	public AssociationClass createAssociationClassLink(EObject context, EObject sourceView, Element source,
			Element target) {
		AssociationClass associationClass = null;
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				associationClass = UMLFactory.eINSTANCE.createAssociationClass();
				model.getPackagedElements().add(associationClass);
				associationClass.setName(LabelServices.INSTANCE.computeDefaultName(associationClass));

				final Property end1 = AssociationServices.INSTANCE.createAssociationClassEnd((Type) source);
				associationClass.getMemberEnds().add(end1);
				final Property end2 = AssociationServices.INSTANCE.createAssociationClassEnd((Type) target);
				associationClass.getMemberEnds().add(end2);

				end1.setAssociation(associationClass);
				end2.setAssociation(associationClass);
				associationClass.getOwnedEnds().add(end1);
				associationClass.getOwnedEnds().add(end2);

				EAnnotation eAnnotation = associationClass.createEAnnotation(ANNOTATION_GENERIC_SOURCE);
				eAnnotation.getDetails().put(ANNOTATION_DETAIL_KEY, ANNOTATION_DETAIL_VALUE);
				associationClass.getEAnnotations().add(eAnnotation);
			}
		}
		return associationClass;
	}

	/**
	 * Check if the current link type is corresponding to the linkTypeName.
	 */
	public boolean isCurrentLinkType(EObject elem, String linkTypeName) {
		return elem.getClass().getSimpleName().equalsIgnoreCase(linkTypeName);
	}

	/**
	 * Create a new Realization link.
	 * 
	 * @param sourceView
	 *            the source view
	 * @param target
	 *            the semantic target element
	 * @param targetView
	 *            the target view
	 */
	public void createRealizationLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				Realization realization = UMLFactory.eINSTANCE.createRealization();
				realization.getClients().add((NamedElement) source);
				realization.getSuppliers().add((NamedElement) target);

				if (source instanceof Package) {
					((Package) source).getPackagedElements().add(realization);
				} else {
					model.getPackagedElements().add(realization);
				}
			}
		}
	}

	/**
	 * Create a new Substitution link.
	 * 
	 * @param sourceView
	 *            the source view
	 * @param target
	 *            the semantic target element
	 * @param targetView
	 *            the target view
	 */
	public void createSubstitutionLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Classifier sourceElement = (Classifier) source;
				Classifier targetElement = (Classifier) target;
				Substitution substitution = UMLFactory.eINSTANCE.createSubstitution();
				substitution.getClients().add(sourceElement);
				substitution.getSuppliers().add(targetElement);
				substitution.setContract(targetElement);
				((Classifier) sourceElement).getSubstitutions().add(substitution);
			}
		}
	}

	/**
	 * Create a new Usage link.
	 * 
	 * @param sourceView
	 *            the source view
	 * @param target
	 *            the semantic target element
	 * @param targetView
	 *            the target view
	 */
	public void createUsageLink(EObject context, EObject sourceView, Element source, Element target) {
		if (sourceView instanceof DDiagramElement) {
			EObject root = getDiagramRoot(sourceView);
			if (root instanceof Package) {
				Package model = (Package) root;
				NamedElement sourceElement = (NamedElement) source;
				NamedElement targetElement = (NamedElement) target;
				Usage usage = UMLFactory.eINSTANCE.createUsage();
				usage.getClients().add(sourceElement);
				usage.getSuppliers().add(targetElement);

				if (source instanceof Package) {
					((Package) source).getPackagedElements().add(usage);
				} else {
					model.getPackagedElements().add(usage);
				}
			}
		}

	}

	/**
	 * Get the Constraint label.
	 */
	public String getConstraintLabel(Element elem) {
		StringBuilder constLabel = new StringBuilder();
		String body = "";
		String lang = "";
		if (elem instanceof Constraint) {
			Constraint constraint = ((Constraint) elem);
			ValueSpecification valueSpec = constraint.getSpecification();
			if (valueSpec instanceof OpaqueExpression) {
				OpaqueExpression opaqueEsp = (OpaqueExpression) valueSpec;
				if (!opaqueEsp.getBodies().isEmpty()) {
					body = opaqueEsp.getBodies().get(0);
				}
				if (!opaqueEsp.getLanguages().isEmpty()) {
					lang = opaqueEsp.getLanguages().get(0);
				}

				List<Stereotype> appliedStereoTypes = constraint.getAppliedStereotypes();
				if (!appliedStereoTypes.isEmpty()) {
					constLabel.append(ILabelConstants.ST_LEFT);
				}
				for (int i = 0; i < constraint.getAppliedStereotypes().size(); i++) {
					Stereotype stereoType = appliedStereoTypes.get(i);
					constLabel.append(stereoType.getName());
					if (i + 1 == appliedStereoTypes.size()) {
						constLabel.append(ILabelConstants.ST_RIGHT);
					} else {
						constLabel.append(","); //$NON-NLS-1$
					}
				}
				constLabel.append(constraint.getName());
				constLabel.append(ILabelConstants.NL);
				constLabel.append("{{" + lang + "} " + body + "}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}

		return constLabel.toString();
	}


	/**
	 * Get the Constraint text body.
	 */
	public String getBody(EObject elem) {
		Constraint constraint = ((Constraint) elem);
		ValueSpecification valueSpec = constraint.getSpecification();
		if (valueSpec instanceof OpaqueExpression) {
			OpaqueExpression opaqueEsp = (OpaqueExpression) valueSpec;
			if (!opaqueEsp.getBodies().isEmpty()) {
				return opaqueEsp.getBodies().get(0);
			}
		}
		return "";
	}

	/**
	 * Compute label for InformationItem
	 * 
	 * @param elem
	 *            the current element
	 * @return the label of element
	 */
	public String buildLabel(Element element) {
		StringBuilder labelBuilder = new StringBuilder();
		String name = LabelServices.INSTANCE.computeUmlLabel(element);
		String prefix;
		if (element instanceof InformationItem) {
			prefix = "Information"; //$NON-NLS-1$
		} else if (element instanceof PrimitiveType) {
			prefix = "Primitive"; //$NON-NLS-1$
		} else {
			prefix = element.eClass().getName();
		}
		prefix = ILabelConstants.ST_LEFT + prefix + ILabelConstants.ST_RIGHT;

		if (!name.startsWith(prefix)) {
			labelBuilder.append(prefix);
			labelBuilder.append(ILabelConstants.NL);
			labelBuilder.append(name);
			return labelBuilder.toString();
		}

		return name;
	}

	/**
	 * Set the Constraint body.
	 */
	public void setConstraintBody(Element elem, String bodyValue) {
		if (elem instanceof Constraint) {
			ValueSpecification valueSpec = ((Constraint) elem).getSpecification();
			if (valueSpec instanceof OpaqueExpression) {
				OpaqueExpression opaqueEsp = (OpaqueExpression) valueSpec;

				if (!opaqueEsp.getBodies().isEmpty()) {
					opaqueEsp.getBodies().remove(0);
					opaqueEsp.getBodies().add(0, bodyValue);
				}
			}
		}
	}

	/**
	 * 
	 * @param eobject
	 *            an eobject
	 * @return
	 *         the Papyrus {@link ServicesRegistry} associated to the current eobject
	 */
	private final ServicesRegistry getServiceRegistry(final EObject eobject) {
		ServicesRegistry registry = null;
		try {
			registry = ServiceUtilsForEObject.getInstance().getServiceRegistry(eobject);
		} catch (ServiceException e) {
			Activator.log.error(e);
		}
		return registry;
	}

	/**
	 * Service used to determine if the selected Realization edge target could be
	 * reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after
	 *            reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectRealizationLinkPrecondition(Element context, Element newSource) {
		return newSource instanceof Class || newSource instanceof Interface || newSource instanceof Enumeration
				|| newSource instanceof PrimitiveType || newSource instanceof Package
				|| newSource instanceof InstanceSpecification;
	}

	/**
	 * Service used to determine if the selected Substitution edge target could be
	 * reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after
	 *            reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectSubstitutionLinkPrecondition(Element context, Element newSource) {
		return newSource instanceof Class || newSource instanceof Interface || newSource instanceof Enumeration
				|| newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected Usage edge target could be
	 * reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after
	 *            reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectUsageLinkPrecondition(Element context, Element newSource) {
		return newSource instanceof Class || newSource instanceof Package || newSource instanceof Interface
				|| newSource instanceof Enumeration || newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected Association edge source/target
	 * could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param target
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean reconnectAssociationLinkPrecondition(Element context, Element target) {
		return target instanceof Class || target instanceof Interface || target instanceof Enumeration
				|| target instanceof PrimitiveType;
	}

	/**
	 * Get the root model of the diagram
	 */
	private Model getRootModel(Element element) {
		Model currentModel = element.getModel();
		if (!element.equals(currentModel)) {
			currentModel = getRootModel(currentModel);
		}

		return currentModel;
	}

	/**
	 * Service used to reconnect a Realization edge source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */

	public void reconnectRealizationEdgeSource(Element context, Element oldSource, Element newSource) {
		// get the root model of the diagram
		Model rootModel = getRootModel(oldSource);

		// remove the old source from the realization element and add the new source
		Realization realizationEdge = (Realization) context;
		realizationEdge.getClients().remove(oldSource);
		realizationEdge.getClients().add((NamedElement) newSource);

		// keep the removed old source in the root model
		if (oldSource instanceof Package && !(newSource instanceof Package)) {
			((Package) oldSource).getPackagedElements().remove(realizationEdge);
			rootModel.getPackagedElements().add(realizationEdge);
		} else if (newSource instanceof Package && !(oldSource instanceof Package)) {
			((Package) newSource).getPackagedElements().add(realizationEdge);
			rootModel.getPackagedElements().remove(realizationEdge);
		}
	}

	/**
	 * Service used to reconnect a Realization edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectRealizationEdgeTarget(Element context, Element source, Element target) {
		Realization realizationEdge = (Realization) context;
		realizationEdge.getSuppliers().remove(source);
		realizationEdge.getSuppliers().add((NamedElement) target);
	}

	/**
	 * Service used to reconnect a Substitution edge source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectSubstitutionEdgeSource(Element context, Element oldSource, Element newSource) {
		Substitution substitutionEdge = (Substitution) context;
		substitutionEdge.getClients().remove(oldSource);
		substitutionEdge.getClients().add((NamedElement) newSource);

		((Classifier) oldSource).getSubstitutions().remove(substitutionEdge);
		((Classifier) newSource).getSubstitutions().add(substitutionEdge);
	}

	/**
	 * Service used to reconnect a Substitution edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectSubstitutionEdgeTarget(Element context, Element oldTarget, Element newTarget) {
		Substitution substitutionEdge = (Substitution) context;
		substitutionEdge.getSuppliers().remove(oldTarget);
		substitutionEdge.getSuppliers().add((NamedElement) newTarget);
		substitutionEdge.setContract((Classifier) newTarget);
	}

	/**
	 * Service used to reconnect a Usage edge source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectUsageEdgeSource(Element context, Element oldSource, Element newSource) {
		// get the root model of the diagram
		Model rootModel = getRootModel(oldSource);

		// remove the old source from the Usage element and the new source
		Usage usageEdge = (Usage) context;
		usageEdge.getClients().remove(oldSource);
		usageEdge.getClients().add((NamedElement) newSource);

		// keep the removed old source in the root model
		if (oldSource instanceof Package && !(newSource instanceof Package)) {
			((Package) oldSource).getPackagedElements().remove(usageEdge);
			rootModel.getPackagedElements().add(usageEdge);
		} else if (newSource instanceof Package && !(oldSource instanceof Package)) {
			((Package) newSource).getPackagedElements().add(usageEdge);
			rootModel.getPackagedElements().remove(usageEdge);
		}
	}

	/**
	 * Service used to reconnect a Usage edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param source
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param target
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectUsageEdgeTarget(Element context, Element source, Element target) {
		Usage usageEdge = (Usage) context;
		usageEdge.getSuppliers().remove(source);
		usageEdge.getSuppliers().add((NamedElement) target);
	}

	/**
	 * Service used to reconnect a Link edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param edgeView
	 *            Represents the graphical new edge
	 * @param oldSource
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectAssociationEdgeSource(Element context, DEdge edgeView, Element oldSource, Element newSource) {
		// if reconnect the source of an AssociationClass edge
		if (context instanceof AssociationClass) {
			// set the ownedEnd to the new source
			for (Property ownedEnd : ((AssociationClass) context).getOwnedEnds()) {
				if (ownedEnd.getType().equals(oldSource)) {
					ownedEnd.setType((Type) newSource);
					ownedEnd.setName(((Type) newSource).getName());
					break;
				}
			}
		} else // if reconnect the source of an Association edge
		{
			// get the target of the edge
			Element target = null;
			if (edgeView.getTargetNode() instanceof DNodeListSpec) {
				target = (Element) ((DNodeListSpec) edgeView.getTargetNode()).getTarget();
			} else if (edgeView.getTargetNode() instanceof DNodeContainerSpec)

			{
				target = (Element) ((DNodeContainerSpec) edgeView.getTargetNode()).getTarget();
			}

			// find the Attribute in the old source to be modified
			EList<Property> attributes = null;
			if (oldSource instanceof Class) {
				attributes = ((Class) oldSource).getOwnedAttributes();
			} else if (oldSource instanceof Interface) {
				attributes = ((Interface) oldSource).getOwnedAttributes();
			} else if (oldSource instanceof DataType) {
				attributes = ((DataType) oldSource).getOwnedAttributes();
			}

			// remove attribute from the old source
			Property modifiedAttribute = findAttributToBeModified(attributes, (Element) target);
			attributes.remove(modifiedAttribute);

			// add attribute to the new source
			if (newSource instanceof Class) {
				((Class) newSource).getOwnedAttributes().add(modifiedAttribute);
			} else if (newSource instanceof Interface) {
				((Interface) newSource).getOwnedAttributes().add(modifiedAttribute);
			} else if (newSource instanceof DataType) {
				((DataType) newSource).getOwnedAttributes().add(modifiedAttribute);
			}

			Association association = (Association) modifiedAttribute.getAssociation();
			for (Property ownedEnd : association.getOwnedEnds()) {
				if (ownedEnd.getType().equals(oldSource)) {
					ownedEnd.setType((Type) newSource);
					ownedEnd.setName(((Type) newSource).getName());
					break;
				}
			}
		}
	}

	/**
	 * Find an attribute in a list of attributes
	 * 
	 * @param attributes
	 *            list of attributes
	 * @param target
	 *            the searched attribute
	 * @return the found attribute
	 */
	private Property findAttributToBeModified(EList<Property> attributes, Element target) {
		for (Property attribut : attributes) {
			if (attribut.getType().equals(target)) {
				return attribut;
			}
		}

		return null;
	}

	/**
	 * Service used to reconnect a Link edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param edgeView
	 *            Represents the graphical new edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before
	 *            reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after
	 *            reconnecting
	 * @return the Element attached to the edge once it has been modified
	 */
	public void reconnectAssociationEdgeTarget(Element context, DEdge edgeView, Element oldTarget, Element newTarget) {
		// if reconnect the target of an AssociationClass edge
		if (context instanceof AssociationClass) {
			// set the ownedEnd to the new target
			for (Property ownedEnd : ((AssociationClass) context).getOwnedEnds()) {
				if (ownedEnd.getType().equals(oldTarget)) {
					ownedEnd.setType((Type) newTarget);
					ownedEnd.setName(((Type) newTarget).getName());
					break;
				}
			}
		} else // if reconnect the target of an Association edge
		{
			// get the source of the edge
			Element source = null;
			if (edgeView.getSourceNode() instanceof DNodeListSpec) {
				source = (Element) ((DNodeListSpec) edgeView.getSourceNode()).getTarget();
			} else if (edgeView.getSourceNode() instanceof DNodeContainerSpec)

			{
				source = (Element) ((DNodeContainerSpec) edgeView.getSourceNode()).getTarget();
			}

			// find the Attribute in the source to be modified
			EList<Property> attributes = null;
			if (source instanceof Class) {
				attributes = ((Class) source).getOwnedAttributes();
			} else if (source instanceof Interface) {
				attributes = ((Interface) source).getOwnedAttributes();
			} else if (source instanceof DataType) {
				attributes = ((DataType) source).getOwnedAttributes();
			}
			Property modifiedAttribute = findAttributToBeModified(attributes, (Element) oldTarget);

			// set the attribute to the new target
			modifiedAttribute.setType((Type) newTarget);
			modifiedAttribute.setName(((Type) newTarget).getName());
		}
	}

	/**
	 * Get the target element of the instance specification link.
	 * 
	 * @param elem
	 *            the instance specification Link
	 * @return the target element of the current instance specification link
	 */
	public EObject instanceSpecificationLink_getTarget(EObject elem) {
		if (elem instanceof InstanceSpecification) {
			InstanceSpecification instanceSpecification = (InstanceSpecification) elem;
			if (!instanceSpecification.getEAnnotations().isEmpty()) {
				EAnnotation eAnnotation = instanceSpecification.getEAnnotations().get(0);
				if (!eAnnotation.getReferences().isEmpty()) {
					return eAnnotation.getReferences().get(1);
				}
			}
		}

		return null;
	}

	/**
	 * Get the source element of the instance specification link.
	 * 
	 * @param elem
	 *            the instance specification Link
	 * @return the source element of the current instance specification link
	 */
	public EObject instanceSpecificationLink_getSource(EObject elem) {
		if (elem instanceof InstanceSpecification) {
			InstanceSpecification instanceSpecification = (InstanceSpecification) elem;
			if (!instanceSpecification.getEAnnotations().isEmpty()) {
				EAnnotation eAnnotation = instanceSpecification.getEAnnotations().get(0);
				if (!eAnnotation.getReferences().isEmpty()) {
					return eAnnotation.getReferences().get(0);
				}
			}
		}

		return null;
	}

	/**
	 * Compute the label of the given association.
	 *
	 * @param association
	 *            the {@link Association} for which to retrieve a label.
	 * @return the computed label.
	 */
	public String computeAssociationBeginLabel(Association association) {
		return LabelServices.INSTANCE.computeAssociationBeginLabel(association);
	}

	/**
	 * Compute the association edge begin name
	 * 
	 * @param association
	 *            the current association
	 * @return the begin name
	 */
	public String computeAssociationClassBeginLabel(Association association) {
		Property source = AssociationServices.INSTANCE.getSource(association);
		return "+ " + source.getName();
	}

	/**
	 * Compute the association edge end name
	 * 
	 * @param association
	 *            the current association
	 * @return the end name
	 */
	public String computeAssociationClassEndLabel(Association association) {
		Property target = AssociationServices.INSTANCE.getTarget(association);
		return "+ " + target.getName();
	}

	/**
	 * Compute the instance specification edge begin name
	 * 
	 * @param context
	 *            the current instance specification
	 * @return the begin name
	 */
	public String computeInstanceSpecBeginLabel(EObject context) {
		InstanceSpecification instanceSpec = (InstanceSpecification) context;
		InstanceSpecification source = (InstanceSpecification) instanceSpec.getEAnnotations().get(0).getReferences()
				.get(0);
		return source.getName();
	}

	/**
	 * Compute the instance specification edge end name
	 * 
	 * @param context
	 *            the current instance specification
	 * @return the end name
	 */
	public String computeInstanceSpecEndLabel(EObject context) {
		InstanceSpecification instanceSpec = (InstanceSpecification) context;
		InstanceSpecification target = (InstanceSpecification) instanceSpec.getEAnnotations().get(0).getReferences()
				.get(1);
		return target.getName();
	}

	/**
	 * Compute the label of the given association.
	 *
	 * @param association
	 *            the {@link Association} for which to retrieve a label.
	 * @return the computed label.
	 */
	public String computeAssociationEndLabel(Association association) {
		return LabelServices.INSTANCE.computeAssociationEndLabel(association);
	}


	/**
	 * Create an association between two elements.
	 *
	 * @param source
	 *            association source
	 * @param target
	 *            association target
	 * @return The association
	 */
	private Association createAssociation(Element source, Element target, int assocType) {
		// get the root model of the diagram
		Model rootModel = getRootModel(source);

		final Association association = UMLFactory.eINSTANCE.createAssociation();
		final Property end1 = AssociationServices.INSTANCE.createAssociationClassEnd((Type) source);
		association.getMemberEnds().add(end1);
		final Property end2 = AssociationServices.INSTANCE.createAssociationEnd((Type) target);
		association.getMemberEnds().add(end2);

		association.getOwnedEnds().add(end1);
		end2.setAssociation(association);
		end2.setUpper(1);

		switch (assocType) {
		case AggregationKind.SHARED:
			end2.setAggregation(AggregationKind.SHARED_LITERAL);
			break;
		case AggregationKind.COMPOSITE:
			end2.setAggregation(AggregationKind.COMPOSITE_LITERAL);
			break;
		default:
			break;
		}

		if (source instanceof Class) {
			((Class) source).getOwnedAttributes().add(end2);
		} else if (source instanceof Interface) {
			((Interface) source).getOwnedAttributes().add(end2);
		} else if (source instanceof DataType) {
			((DataType) source).getOwnedAttributes().add(end2);
		}

		EAnnotation eAnnotation = association.createEAnnotation(ANNOTATION_GENERIC_SOURCE);
		eAnnotation.getDetails().put(ANNOTATION_DETAIL_KEY, ANNOTATION_DETAIL_VALUE);
		association.getEAnnotations().add(eAnnotation);

		rootModel.getPackagedElements().add(association);

		return association;
	}

	public boolean isNotAssociation(EObject elem) {
		return ((Property) elem).getAssociation() == null;
	}

	public void createProfileApplicationLink(EObject object, Element source) {
		ProfileApplication profileApplication = UMLFactory.eINSTANCE.createProfileApplication();
		((Package) source).getProfileApplications().add(profileApplication);
	}

	/**
	 * Create a new association.
	 *
	 * @param object
	 *            Object
	 * @param source
	 *            selected source
	 * @param target
	 *            selected Target
	 * @param sourceView
	 *            Source view
	 * @param targetView
	 *            Target view
	 * @return Association
	 */
	public Association createAssociation(EObject object, Element source, Element target, EObject sourceView,
			EObject targetView, int assocType) {
		if (source.eContainer() instanceof Package) {
			// tool creation association edge
			if (!(source instanceof Association || target instanceof Association)) {
				return createAssociation(source, target, assocType);
			} else if ((source instanceof AssociationClass || target instanceof AssociationClass)
					&& (sourceView instanceof DEdge || targetView instanceof DEdge)) {
				// try to connect association from/to associationClas (edge part)
				return createAssociationAddEnd(source, target);
			} else if (source instanceof AssociationClass || target instanceof AssociationClass
					&& (sourceView instanceof DNodeList || targetView instanceof DNodeList)) {
				// try to connect association from/to associationClas (container part)
				return createAssociation(source, target, assocType);
			} else if (source instanceof Association || target instanceof Association) {
				return createAssociationAddEnd(source, target);
			}
		}
		return null;
	}

	/**
	 * Add an end to an existing association.
	 *
	 * @param source
	 *            Association or element
	 * @param target
	 *            element or association
	 */
	private Association createAssociationAddEnd(Element source, Element target) {
		Association association;
		Type type;
		if (source instanceof Association) {
			association = (Association) source;
			type = (Type) target;
		} else {
			association = (Association) target;
			type = (Type) source;
		}

		if (isBroken(association)) { // Look for broken association
			fixAssociation(association, type);
		} else { // create new end
			final Property end = AssociationServices.INSTANCE.createAssociationEnd(type);
			association.getNavigableOwnedEnds().add(end);
			association.getOwnedEnds().add(end);
		}
		return association;
	}

	/**
	 * Precondition for n-ary association creation.
	 *
	 * @param object
	 *            selected association
	 * @return true if association is binary and no end have no qualifiers
	 */
	public boolean createNaryAssociationPrecondition(EObject object) {
		return AssociationServices.INSTANCE.createNaryAssociationPrecondition(object);
	}

	/**
	 * Create an operation in a class.
	 *
	 * @param type
	 *            the container {@link org.eclipse.uml2.uml.Type} element
	 * @return New operation
	 */
	public Operation createOperation(org.eclipse.uml2.uml.Type type) {
		return OperationServices.INSTANCE.createOperation(type);
	}

	/**
	 * Create new qualifier for association
	 *
	 * @param association
	 *            selected association
	 */
	public void createQualifier(Association association) {
		// Display a selection pop-up to choose the end
		final ListDialog dialog = new ListDialog(Display.getCurrent().getActiveShell());
		dialog.setTitle("Qualifier creation"); //$NON-NLS-1$
		dialog.setMessage("Please select the end to create new Qualifier:"); //$NON-NLS-1$
		dialog.setInput(association.getMemberEnds().toArray());
		dialog.setContentProvider(new ArrayContentProvider());
		dialog.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((NamedElement) element).getName();
			}
		});
		dialog.setInitialSelections(new Object[] { association.getMemberEnds().get(0) });

		final int status = dialog.open();
		if (status == Window.OK) {
			final Property qualifier = UMLFactory.eINSTANCE.createProperty();
			final Property end = (Property) dialog.getResult()[0];
			end.getQualifiers().add(qualifier);
			qualifier.setName(LabelServices.INSTANCE.computeDefaultName(qualifier));
		}
	}

	/**
	 * Delete selected edge fron a N-Ary association
	 *
	 * @param association
	 *            association
	 * @param element
	 *            edge to delete
	 */
	public void deleteNAryAssociation(Association association, DDiagramElement element) {
		if (isNary(association)) {
			final Property end = AssociationServices.INSTANCE.getSourceEndAssociation(association, element);
			end.eContainer();
			EcoreUtil.delete(end);
		}
	}

	/**
	 * Edit the qualifier label
	 *
	 * @param context
	 *            property association end element
	 * @param editedLabelContent
	 *            edit label content
	 * @return end
	 */
	public Element editQualifierLabel(Property context, String editedLabelContent) {
		final DisplayLabelSwitch displayLabelSwitch = new DisplayLabelSwitch();
		final EditLabelSwitch editLabel = new EditLabelSwitch();
		// Separator for direct edit
		final ArrayList<String> labels = new ArrayList<String>(
				Arrays.asList(editedLabelContent.split(DirectEditLabelSwitch.QUALIFIER_SEPARATOR)));
		// List qualifiers
		final EList<Property> qualifiers = context.getQualifiers();
		// Check for changes
		if (labels.size() != qualifiers.size()) {
			qualifiers.clear();
			for (final String label : labels) {
				final Property qualifier = UMLFactory.eINSTANCE.createProperty();
				qualifiers.add(qualifier);
				editLabel.setEditedLabelContent(label);
				editLabel.doSwitch(qualifier);
			}
		} else {
			// Only rename element
			int index = 0;
			for (final String label : labels) {
				if (!label.equals(displayLabelSwitch.doSwitch(qualifiers.get(index)))) {
					editLabel.setEditedLabelContent(label);
					editLabel.doSwitch(qualifiers.get(index));
				}
				index++;
			}
		}
		return context;
	}

	/**
	 * Iterate over the given {@link Collection} of root elements to find a
	 * {@link Type} element with the given name.
	 *
	 * @param roots
	 *            the elements to inspect
	 * @param typeName
	 *            the name to match
	 * @return the found {@link Type} or <code>null</code>
	 */
	public Type findTypeByName(Collection<EObject> roots, String typeName) {
		return ElementServices.INSTANCE.findTypeByName(roots, typeName);
	}

	/**
	 * With the given {@link EObject} iterate over root elements to find a
	 * {@link Type} element with the given name.
	 *
	 * @param object
	 *            the elements to inspect
	 * @param typeName
	 *            the name to match
	 * @return the found {@link Type} or <code>null</code>
	 */
	public Type findTypeByName(EObject object, String typeName) {
		return ElementServices.INSTANCE.findTypeByName(object, typeName);
	}

	private void fixAssociation(Association association, Type type) {
		final EList<Property> ends = association.getMemberEnds();
		final List<Property> brokenEnds = new ArrayList<Property>();
		for (final Property end : ends) {
			if (end.getType() == null) {
				brokenEnds.add(end);
			}
		}

		if (brokenEnds.size() > 1) {
			// If several broken links exist user have to select the link to reconnect.

			final ListDialog dialog = new ListDialog(Display.getCurrent().getActiveShell());
			dialog.setTitle("Reconnect broken association end:"); //$NON-NLS-1$
			dialog.setMessage("Please select the end to reconnect: "); //$NON-NLS-1$
			dialog.setInput(brokenEnds.toArray());
			dialog.setContentProvider(new ArrayContentProvider());
			dialog.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return ((NamedElement) element).getName();
				}
			});
			dialog.setInitialSelections(new Object[] { brokenEnds.get(0) });

			final int status = dialog.open();
			if (status == Window.OK) {
				final Object[] types = dialog.getResult();
				if (types != null && types.length == 1) {
					final Property endToFix = (Property) types[0];
					endToFix.setType(type);
					endToFix.setName(AssociationServices.INSTANCE.getAssociationEndsName(type));
				}
			}
		} else {
			final Property endToFix = brokenEnds.get(0);
			endToFix.setType(type);
			endToFix.setName(AssociationServices.INSTANCE.getAssociationEndsName(type));
		}
	}

	/**
	 * Fix association.
	 *
	 * @param host
	 *            Host
	 * @param a
	 *            Association
	 * @param b
	 *            Association
	 */
	public void fixAssociation(EObject host, EObject a, EObject b) {
		if (a instanceof Association && b instanceof Type) {
			fixAssociation((Association) a, (Type) b);
		} else if (b instanceof Association && a instanceof Type) {
			fixAssociation((Association) b, (Type) a);
		}
	}

	/**
	 * Get abstract label format.
	 *
	 * @param object
	 *            EObject
	 * @return Abstract label format
	 */
	public List<FontFormat> getAbstractBoldLabelFormat(EObject object) {
		// Fix to return bold/italic when bug will be fixed on sirius
		final List<FontFormat> fontFormats = new ArrayList<FontFormat>();
		fontFormats.add(FontFormat.BOLD_LITERAL);
		fontFormats.add(FontFormat.ITALIC_LITERAL);
		return fontFormats;
	}

	/**
	 * Get abstract label format.
	 *
	 * @param object
	 *            EObject
	 * @return Abstract label format
	 */
	public FontFormat getAbstractItalicLabelFormat(EObject object) {
		// Fix to return bold/italic when bug will be fixed on sirius
		return FontFormat.ITALIC_LITERAL;
	}

	/**
	 * Get all the stereotype applications according to the selected diagram.
	 *
	 * @param diagram
	 *            Current diagram
	 * @return Stereotype applications
	 */
	public Collection<Object> getAllStereotypeApplications(DDiagram diagram) {
		return org.eclipse.papyrus.sirius.uml.diagram.common.core.services.StereotypeServices.INSTANCE
				.getAllStereotypeApplications(diagram);
	}

	/**
	 * Get list of association. Check in diagram only two ends are presents.
	 *
	 * @param container
	 *            package
	 * @param diagram
	 *            diagram
	 * @return list of binary association
	 */
	public List<Association> getAssociation(Package container, DDiagram diagram) {
		final List<Association> result = new ArrayList<Association>();
		final Collection<EObject> associations = getAssociationInverseRefs(diagram);
		for (final EObject object : associations) {
			final Association association = (Association) object;
			if (getVisibleAssociationEnds(association, diagram).size() <= 2) {
				result.add(association);
			}
		}
		return result;
	}

	/**
	 * Retrieve the cross references of the association of all the UML elements
	 * displayed as node in a Diagram. Note that a Property cross reference will
	 * lead to retrieve the cross references of this property.
	 *
	 * @param diagram
	 *            a diagram.
	 * @return the list of cross reference of the given
	 */
	public Collection<EObject> getAssociationInverseRefs(DDiagram diagram) {
		return NodeInverseRefsServices.INSTANCE.getAssociationInverseRefs(diagram);
	}

	/**
	 * Get base class associated to a stereotype application.
	 *
	 * @param stereotypeApplication
	 *            Stereotype application
	 * @return Base class
	 */
	public Element getBaseClass(EObject stereotypeApplication) {
		return StereotypeServices.INSTANCE.getBaseClass(stereotypeApplication);
	}

	/**
	 * Get broken associations.
	 *
	 * @param container
	 *            the current container.
	 * @return a list of association which might be considered as "broken", we are
	 *         not able to display them as edges.
	 */
	public Collection<Association> getBrokenAssociations(EObject container) {
		final Collection<Association> result = new ArrayList<Association>();
		for (final EObject child : container.eContents()) {
			if (child instanceof Association && isBroken((Association) child)) {
				result.add((Association) child);
			}
		}
		return result;
	}

	/**
	 * Get navigable owned end of an association
	 *
	 * @param association
	 *            Association
	 * @return Association
	 */
	public List<Property> getNavigableOwnedEnds(Association association) {
		final List<Property> ends = Lists.newArrayList();
		final Property source = AssociationServices.INSTANCE.getSource(association);
		final Property target = AssociationServices.INSTANCE.getTarget(association);
		if (source != null) {
			ends.add(source);
		}
		if (target != null) {
			ends.add(target);
		}
		return ends;
	}

	/**
	 * Get the type of the association source end.
	 *
	 * @param association
	 *            association
	 * @param diagram
	 *            diagram
	 * @return type of the source
	 */
	public Element getSourceType(Association association, DDiagram diagram) {
		final EList<DDiagramElement> elements = diagram.getDiagramElements();
		// List semantic elements visible in diagram
		final List<EObject> visibleEnds = new ArrayList<EObject>();
		for (final DDiagramElement element : elements) {
			visibleEnds.add(element.getTarget());
		}
		final EList<Property> ends = association.getMemberEnds();
		for (final Property end : ends) {
			if (visibleEnds.contains(end.getType())) {
				if (end.getQualifiers().isEmpty()) {
					return end.getType();
				}
				return end;
			}
		}
		return null;
	}

	/**
	 * Get the type of the association target end.
	 *
	 * @param association
	 *            association
	 * @param diagram
	 *            diagram
	 * @return Type of the target
	 */
	public Element getTargetType(Association association, DDiagram diagram) {
		final EList<DDiagramElement> elements = diagram.getDiagramElements();
		// List semantic elements visible in diagram
		final List<EObject> diagramElements = new ArrayList<EObject>();
		for (final DDiagramElement element : elements) {
			diagramElements.add(element.getTarget());
		}
		final List<Property> ends = association.getMemberEnds();
		// find source index in list
		int sourceIndex = 0;
		for (int i = 0; i < ends.size(); i++) {
			if (diagramElements.contains(ends.get(i).getType())) {
				sourceIndex = i;
				break;
			}
		}
		// find target from the end of the list
		int targetIndex = ends.size() - 1;
		while (targetIndex > 0 && targetIndex > sourceIndex) {
			if (diagramElements.contains(ends.get(targetIndex).getType())) {
				if (ends.get(targetIndex).getQualifiers().isEmpty()) {
					return ends.get(targetIndex).getType();
				}
				return ends.get(targetIndex);
			}
			targetIndex--;
		}
		return null;
	}

	/**
	 * Retrieve the cross references of the template binding of all the UML elements
	 * displayed as node in a Diagram. Note that a Property cross reference will
	 * lead to retrieve the cross references of this property.
	 *
	 * @param diagram
	 *            a diagram.
	 * @return the list of cross reference of the given
	 */
	public Collection<EObject> getTemplateBindingInverseRefs(DDiagram diagram) {
		return NodeInverseRefsServices.INSTANCE.getTemplateBindingInverseRefs(diagram);
	}

	/**
	 * Return collection of visible association class in a diagram.
	 *
	 * @param diagram
	 *            Diagram
	 * @param container
	 *            Container of the associationClass
	 * @return Set of visible association Classes or empty collection
	 */
	public Collection<EObject> getVisibleAssociationClass(DSemanticDiagram diagram, EObject container) {
		final Set<EObject> associationClasses = new HashSet<EObject>();
		final Collection<EObject> displayedNodes = UIServices.INSTANCE.getDisplayedNodes(diagram);
		final Collection<EObject> associations = getAssociationInverseRefs(diagram);
		for (final EObject association : associations) {
			if (association instanceof AssociationClass) {
				final Property source = AssociationServices.INSTANCE.getSource((AssociationClass) association);
				final Property target = AssociationServices.INSTANCE.getTarget((AssociationClass) association);
				if (source != null && target != null) {
					final Type sourceType = source.getType();
					final Type targetType = target.getType();
					final Package parent = ((AssociationClass) association).getNearestPackage();

					// An association class is visible in its parent if the parent is visible, else
					// it is visible
					// directly on the diagram
					if ((container.equals(parent)
							|| !displayedNodes.contains(parent) && container.equals(diagram.getTarget()))
							&& sourceType != null && displayedNodes.contains(sourceType) && targetType != null
							&& displayedNodes.contains(targetType)) {
						associationClasses.add(association);
					}
				}
			}
		}
		return associationClasses;
	}

	private List<Property> getVisibleAssociationEnds(Association association, DDiagram diagram) {
		final List<Property> ends = new ArrayList<Property>();
		// Association should be visible in self container
		// At least one of the ends is visible in diagram
		final EList<DDiagramElement> elements = diagram.getDiagramElements();
		// check if at least more than 2 ends are displayed in diagram
		final List<EObject> visibleEndsList = new ArrayList<EObject>();
		for (final DDiagramElement element : elements) {
			visibleEndsList.add(element.getTarget());
		}
		final EList<Property> associationEnds = association.getMemberEnds();
		for (final Property end : associationEnds) {
			if (visibleEndsList.contains(end.getType())) {
				ends.add(end);
			}
			if (end.getType() == null) { // Broken association case
				ends.add(end);
			}
		}
		return ends;
	}

	private boolean isBroken(Association child) {
		final EList<Property> ends = child.getMemberEnds();
		for (final Property end : ends) {
			if (end.getType() == null) {
				return true;
			}
		}
		return false;
	}

	private boolean isNary(Association association) {
		if (association != null && association.getMembers() != null && association.getMembers().size() > 2) {
			return true;
		}
		return false;
	}

	/**
	 * Check an element is not a Class.
	 *
	 * @param element
	 *            Element
	 * @return return true if the element is not a Class
	 */
	public boolean isNotTypeOfClass(EObject element) {
		return !isTypeOfClass(element);
	}

	/**
	 * Check is a feature is static.
	 *
	 * @param feature
	 *            Feature
	 * @return True if it is a static feature
	 */
	public boolean isStatic(Feature feature) {
		return feature != null && feature.isStatic();
	}

	/**
	 * Check if an element is type of class.
	 *
	 * @param element
	 *            Element
	 * @return True if element is a class
	 */
	public boolean isTypeOfClass(EObject element) {
		return "Class".equals(element.eClass().getName()); //$NON-NLS-1$
	}

	/**
	 * Check if an association can be created.
	 *
	 * @param self
	 *            association
	 * @param preSource
	 *            selected element
	 * @return true if valid
	 */
	public boolean isValidAssociation(EObject self, Element preSource) {
		if (preSource instanceof Association) {
			// Verify association ends don't have qualifiers
			final EList<Property> ends = ((Association) preSource).getMemberEnds();
			for (final Property end : ends) {
				if (!end.getQualifiers().isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if an association can be created. Both selected elements are not
	 * association. If an association is selected is should not be an aggregation or
	 * a composite
	 *
	 * @param self
	 *            association
	 * @param preSource
	 *            User select element as source
	 * @param preTarget
	 *            user select element as target
	 * @return true if valid
	 */
	public boolean isValidAssociation(EObject self, Element preSource, Element preTarget) {

		if (preSource instanceof Association && preTarget instanceof Association) {
			return false; // Source and Target are not association
		} else if (preSource instanceof Association || preTarget instanceof Association) {
			// Verify association is not a binary association
			Association association;
			if (preSource instanceof Association) {
				association = (Association) preSource;
			} else {
				association = (Association) preTarget;
			}

			final EList<Property> ends = association.getMemberEnds();
			for (final Property end : ends) {
				if (AssociationServices.INSTANCE.isComposite(end) || AssociationServices.INSTANCE.isShared(end)) {
					return false;
				}
				if (!end.getQualifiers().isEmpty()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Check is an association source is composite.
	 *
	 * @param association
	 *            Association
	 * @return True if source is composite
	 */
	public boolean sourceIsComposite(Association association) {
		return AssociationServices.INSTANCE.sourceIsComposite(association);
	}

	/**
	 * Check is an association source is navigable.
	 *
	 * @param association
	 *            Association
	 * @param element
	 *            Edge element
	 * @return True if source is navigable
	 */
	public boolean sourceIsNavigable(Association association, DDiagramElement element) {
		return AssociationServices.INSTANCE.sourceIsNavigable(association, element);
	}

	/**
	 * Check is an association source is navigable and composite.
	 *
	 * @param association
	 *            Association
	 * @return True if source is navigable and composite
	 */
	public boolean sourceIsNavigableAndTargetIsComposite(Association association) {
		return AssociationServices.INSTANCE.sourceIsNavigableAndTargetIsComposite(association);
	}

	/**
	 * Check is an association source is navigable and shared.
	 *
	 * @param association
	 *            Association
	 * @return True if source is navigable and shared
	 */
	public boolean sourceIsNavigableAndTargetIsShared(Association association) {
		return AssociationServices.INSTANCE.sourceIsNavigableAndTargetIsShared(association);
	}

	/**
	 * Check is an association source is shared.
	 *
	 * @param association
	 *            Association
	 * @return True if source is shared
	 */
	public boolean sourceIsShared(Association association) {
		return AssociationServices.INSTANCE.sourceIsShared(association);
	}

	/**
	 * Check is an association target is composite.
	 *
	 * @param association
	 *            Association
	 * @return True if target is composite
	 */
	public boolean targetIsComposite(Association association) {
		return AssociationServices.INSTANCE.targetIsComposite(association);
	}

	/**
	 * Check is an association target is navigable.
	 *
	 * @param association
	 *            Association
	 * @return True if target is navigable
	 */
	public boolean targetIsNavigable(Association association) {
		return AssociationServices.INSTANCE.targetIsNavigable(association);
	}

	/**
	 * Check is an association target is navigable and composite.
	 *
	 * @param association
	 *            Association
	 * @return True if target is navigable and composite
	 */
	public boolean targetIsNavigableAndSourceIsComposite(Association association) {
		return AssociationServices.INSTANCE.targetIsNavigableAndSourceIsComposite(association);
	}

	/**
	 * Check is an association target is navigable and shared.
	 *
	 * @param association
	 *            Association
	 * @return True if target is navigable and shared
	 */
	public boolean targetIsNavigableAndSourceIsShared(Association association) {
		return AssociationServices.INSTANCE.targetIsNavigableAndSourceIsShared(association);
	}

	/**
	 * Check is an association target is shared.
	 *
	 * @param association
	 *            Association
	 * @return True if target is shared
	 */
	public boolean targetIsShared(Association association) {
		return AssociationServices.INSTANCE.targetIsShared(association);
	}

}

/*****************************************************************************
 * Copyright (c) 2023 CEA LIST
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
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.clazz.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * This service is used to manage elements which are outside of the context of the current diagram. 
 */
public class ClassDiagramOutsideMappingServices {

	/**
	 * 
	 * Constructor.
	 *
	 */
	public ClassDiagramOutsideMappingServices() {
		// nothing to do
	}

	/**
	 * This method is used to know if the element is outside of the context of the diagram where we want to represent it.
	 * This method is used by Semantic DnD.
	 * 
	 * @param element
	 *            a UML {@link NamedElement}
	 * @param aContainerView
	 *            a containerView
	 * @return
	 *         <code>true</code> if the containerView is a {@link DSemanticDiagram} (or an children of a {@link DSemanticDiagram}) and if the element is not a children of the {@link DSemanticDiagram#getTarget()}.
	 */
	public boolean isOutsideOfTheDiagramContext(final NamedElement element, final EObject aContainerView) {
		if (aContainerView instanceof DSemanticDiagram) {
			return isOutsideOfTheDiagramContext(element, (DSemanticDiagram) aContainerView);
		} else if (aContainerView instanceof DDiagramElement) {
			final DDiagram parentDiagram = ((DDiagramElement) aContainerView).getParentDiagram();
			if (parentDiagram instanceof DSemanticDiagram) {
				return isOutsideOfTheDiagramContext(element, (DSemanticDiagram) parentDiagram);
			}
		}
		return false; // others cases are forbidden
	}

	/**
	 * This method is used to know if the element is external to the diagram where we want to represent it.
	 * This method is used by getSemanticCandidates expression
	 * 
	 * @param element
	 *            a UML {@link NamedElement}
	 * @param diagram
	 *            a diagram
	 * @return
	 *         <code>true</code> if the element is not a children of the {@link DSemanticDiagram#getTarget()}.
	 */
	private boolean isOutsideOfTheDiagramContext(final NamedElement element, final DSemanticDiagram diagram) {
		Assert.isTrue(diagram.getTarget() instanceof NamedElement, "We expected a NamedElement as target for a Diagram"); //$NON-NLS-1$
		NamedElement target = (NamedElement) diagram.getTarget();
		final Collection<Namespace> namespaces = element.allNamespaces();
		return !namespaces.contains(target);
	}

	/**
	 * 
	 * @param element
	 *            an element
	 * @return
	 *         <code>true</code> if the element is read-only
	 */
	public boolean isReadOnly(final EObject element) {
		return EMFHelper.isReadOnly(element);
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link Class}es in the ResourceSet external to the diagram
	 */
	public Collection<EObject> getOutsideClasses(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getClass_());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link Component}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsideComponents(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getComponent());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link DataType}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsideDataTypes(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getDataType());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link Enumeration}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsideEnumerations(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getEnumeration());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link Interface}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsideInterfaces(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getInterface());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link Package}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsidePackages(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getPackage());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link PrimitiveType}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsidePrimitiveTypes(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getPrimitiveType());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link Model}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsideModels(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getModel());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @return
	 *         all {@link Signal}s in the ResourceSet defined outside of the context of the current diagram
	 */
	public Collection<EObject> getOutsideSignals(final EObject self, final DSemanticDiagram diagram) {
		return getOutsideElements(self, diagram, UMLPackage.eINSTANCE.getSignal());
	}

	/**
	 * 
	 * @param self
	 *            the context of the search
	 * @param diagram
	 *            the current {@link DSemanticDiagram}
	 * @param wantedType
	 *            the {@link EClass} represented the wanted type
	 * @return
	 *         all EObject with the wanted EClass type in the {@link ResourceSet} defined outside of the context of the current diagram
	 */
	private Collection<EObject> getOutsideElements(final EObject self, final DSemanticDiagram diagram, final EClass wantedType) {
		final Collection<EObject> wantedElements = new ArrayList<EObject>();
		final Resource currentResource = self.eResource();
		final ResourceSet set = currentResource.getResourceSet();
		for (final Resource resource : set.getResources()) {
			if (resource instanceof UMLResource) {
				final Collection<NamedElement> elements = getAllElementsByType((UMLResource) resource, wantedType);
				if (resource == currentResource) {
					// when the element comes from the current semantic resource, we need to check the element is outside of the context of the current diagram
					for (final NamedElement current : elements) {
						if (isOutsideOfTheDiagramContext((NamedElement) current, diagram)) {
							wantedElements.add(current);
						}
					}
				} else {
					wantedElements.addAll(elements);
				}
			}
		}

		return wantedElements;
	}

	/**
	 * 
	 * @param resource
	 *            a {@link UMLResource}
	 * @param wantedEClassType
	 *            the EClass of the wanted elements
	 * @return
	 *         all {@link NamedElement} found in the {@link UMLResource} which are instance of the expected EClass (using ==)
	 */
	private Collection<NamedElement> getAllElementsByType(final UMLResource resource, final EClass wantedEClassType) {
		// TODO later : try to use the service org.eclipse.papyrus.uml.domain.services.services.UMLService.getAllReachable(EObject, String)
		// TODO later : maybe we could use a cache for these libraries
		// - we find pathmap://UML_METAMODELS/Ecore.metamodel.uml
		// - we also find pathmap://UML_METAMODELS/UML.metamodel.uml
		// - we also find pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml
		final Collection<NamedElement> elements = new ArrayList<NamedElement>();
		final Iterator<EObject> iter = resource.getAllContents();
		while (iter.hasNext()) {
			final EObject current = iter.next();
			if (current instanceof NamedElement && current.eClass() == wantedEClassType) {
				elements.add((NamedElement) current);
			}
		}
		return elements;
	}
}

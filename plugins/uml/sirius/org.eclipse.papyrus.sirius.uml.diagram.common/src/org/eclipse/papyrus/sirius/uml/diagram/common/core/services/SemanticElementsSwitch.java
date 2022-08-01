/******************************************************************************
 * Copyright (c) 2009, 2022 Obeo, CEA LIST, Artal Technologies
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
package org.eclipse.papyrus.sirius.uml.diagram.common.core.services;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.util.UMLSwitch;

import com.google.common.collect.Lists;

/**
 * An EMF Switch used to compute the elements related to another one, which should be displayed on the
 * property view.
 */
public class SemanticElementsSwitch extends UMLSwitch<Object> {

	private List<EObject> semantics = Lists.newArrayList();

	private void addIfNotNull(EObject defaultValue) {
		if (defaultValue != null) {
			semantics.add(defaultValue);
		}
	}

	@Override
	public Object caseActivityEdge(ActivityEdge object) {
		addIfNotNull(object.getGuard());
		return super.caseActivityEdge(object);
	}

	@Override
	public Object caseAssociation(Association object) {
		semantics.addAll(object.getMemberEnds());
		return super.caseAssociation(object);
	}

	@Override
	public Object caseConnector(Connector object) {
		semantics.addAll(object.getEnds());
		return super.caseConnector(object);
	}

	@Override
	public Object caseElement(Element object) {
		semantics.addAll(object.getStereotypeApplications());
		semantics.addAll(object.getAppliedStereotypes());
		return super.caseElement(object);
	}

	@Override
	public Object caseEModelElement(EModelElement object) {
		semantics.addAll(object.getEAnnotations());
		return super.caseEModelElement(object);
	}

	@Override
	public Object caseExtend(Extend object) {
		addIfNotNull(object.getExtension());
		addIfNotNull(object.getExtendedCase());
		return super.caseExtend(object);
	}

	@Override
	public Object caseInclude(Include object) {
		addIfNotNull(object.getIncludingCase());
		addIfNotNull(object.getAddition());
		return super.caseInclude(object);
	}

	@Override
	public Object caseMessage(Message object) {
		addIfNotNull(object.getSendEvent());
		addIfNotNull(object.getReceiveEvent());
		return super.caseMessage(object);
	}

	@Override
	public Object caseOperation(Operation object) {
		semantics.addAll(object.getOwnedParameters());
		return super.caseOperation(object);
	}

	@Override
	public Object caseProperty(Property object) {
		final ValueSpecification defaultValue = object.getDefaultValue();
		addIfNotNull(defaultValue);
		addIfNotNull(object.getNameExpression());
		return super.caseProperty(object);
	}

	@Override
	public Object caseSlot(Slot object) {
		addIfNotNull(object.getDefiningFeature());
		return super.caseSlot(object);
	}

	@Override
	public Object caseTransition(Transition object) {
		if (object.getGuard() != null) {
			addIfNotNull(object.getGuard().getSpecification());
		}
		return super.caseTransition(object);
	}

	@Override
	public Object defaultCase(EObject object) {
		addIfNotNull(object);
		return super.defaultCase(object);
	}

	/**
	 * Get semantics elements.
	 * 
	 * @param cur
	 *            Element
	 * @return Semantic elements
	 */
	public Collection<EObject> getSemanticElements(EObject cur) {
		semantics = Lists.newArrayList();
		doSwitch(cur);
		/*
		 * We are reversing as we want the first specific before.
		 */
		return Lists.reverse(semantics);
	}
}

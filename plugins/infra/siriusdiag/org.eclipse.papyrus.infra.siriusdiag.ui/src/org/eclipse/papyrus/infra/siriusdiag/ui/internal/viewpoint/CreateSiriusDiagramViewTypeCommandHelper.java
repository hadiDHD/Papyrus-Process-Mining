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
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.infra.siriusdiag.ui.internal.viewpoint;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.infra.architecture.ArchitectureDomainManager;
import org.eclipse.papyrus.infra.architecture.representation.PapyrusRepresentationKind;
import org.eclipse.papyrus.infra.siriusdiag.representation.ICreateSiriusDiagramEditorCommand;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramConstants;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.ui.Activator;
import org.eclipse.papyrus.infra.tools.util.ClassLoaderHelper;
import org.eclipse.papyrus.infra.viewpoints.policy.IViewTypeHelper;
import org.eclipse.papyrus.infra.viewpoints.policy.PolicyChecker;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.description.DAnnotation;

/**
 * Represents a helper for the handling of Sirius Diagram Template View Type creation commands.
 *
 */
public class CreateSiriusDiagramViewTypeCommandHelper implements IViewTypeHelper {

	/**
	 * The cache of prototypes
	 */
	private Map<PapyrusRepresentationKind, SiriusDiagramViewPrototype> cache;

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.IViewTypeHelper#isSupported(org.eclipse.emf.ecore.EClass)
	 *
	 * @param type
	 * @return
	 */
	@Override
	public boolean isSupported(EClass type) {
		return EcoreUtil.equals(type, org.eclipse.papyrus.infra.siriusdiag.representation.RepresentationPackage.eINSTANCE.getSiriusDiagramPrototype());
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.IViewTypeHelper#isSupported(org.eclipse.emf.ecore.EObject)
	 *
	 * @param view
	 * @return
	 */
	@Override
	public boolean isSupported(EObject view) {
		return (view instanceof DSemanticDiagram);
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.IViewTypeHelper#getPrototypeFor(org.eclipse.papyrus.infra.architecture.representation.PapyrusRepresentationKind)
	 *
	 * @param kind
	 * @return
	 */
	@Override
	public ViewPrototype getPrototypeFor(PapyrusRepresentationKind kind) {
		if (!(kind instanceof SiriusDiagramPrototype)) {
			return null;
		}

		SiriusDiagramPrototype siriusDiagram = (SiriusDiagramPrototype) kind;
		if (cache == null) {
			cache = new HashMap<>();
		}
		if (cache.containsKey(siriusDiagram)) {
			return cache.get(siriusDiagram);
		}
		String creationCommandClassName = siriusDiagram.getCreationCommandClass();
		if (creationCommandClassName == null || creationCommandClassName.isEmpty()) {
			return null;
		}

		Class<?> creationCommandClass = ClassLoaderHelper.loadClass(creationCommandClassName);
		if (creationCommandClass != null) {
			try {
				final Constructor<?> constructor = creationCommandClass.getDeclaredConstructor(new Class[0]);
				Object newInstance = constructor.newInstance();
				if (newInstance instanceof ICreateSiriusDiagramEditorCommand) {
					ICreateSiriusDiagramEditorCommand command = (ICreateSiriusDiagramEditorCommand) newInstance;
					SiriusDiagramViewPrototype proto = new SiriusDiagramViewPrototype(siriusDiagram, command);
					cache.put(siriusDiagram, proto);
					return proto;
				}
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException e) {
				Activator.log.error(e);
			} catch (IllegalArgumentException e) {
				Activator.log.error(e);
			} catch (InvocationTargetException e) {
				Activator.log.error(e);
			}
		}
		return null;
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.IViewTypeHelper#getPrototypeOf(org.eclipse.emf.ecore.EObject)
	 *
	 * @param view
	 * @return
	 */
	@Override
	public ViewPrototype getPrototypeOf(EObject view) {
		if (!isSupported(view)) {
			return null;
		}

		final DSemanticDiagram dsd = (DSemanticDiagram) view;
		final DAnnotation annotation = dsd.getDAnnotation(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_SOURCE);
		final String protoID = annotation.getDetails().get(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_KEY);

		PolicyChecker checker = PolicyChecker.getFor(view);
		ArchitectureDomainManager manager = ArchitectureDomainManager.getInstance();

		SiriusDiagramPrototype repKind = (SiriusDiagramPrototype) manager.getRepresentationKindById(protoID);
		if (null != repKind && checker.isInViewpoint(repKind)) {
			return getPrototypeFor(repKind);
		}
		return ViewPrototype.UNAVAILABLE_VIEW;
	}

}

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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and others
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.hyperlink;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.core.sashwindows.di.service.IPageManager;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.infra.hyperlink.Activator;
import org.eclipse.uml2.uml.Element;

public class HyperlinkService {


	public static List<EObject> allAvailableViews(EObject obj) {

		if (obj instanceof Element) {
			return getViews((Element) obj);
		}
		return null;

	}

	protected static List<EObject> getViews(Element element) {
		if (element == null) {
			return null;
		}

		IPageManager pageManager;
		try {
			pageManager = ServiceUtilsForEObject.getInstance().getService(IPageManager.class, element);
		} catch (ServiceException e) {
			Activator.log.error(e);
			return null;
		}
		return pageManager.allPages().stream().filter(obj -> obj instanceof EObject).map(obj -> (EObject) obj)
				.collect(Collectors.toList());
	}

	public static boolean isNotSemanticElement(EObject obj) {
		return !(obj instanceof Element);
	}
}

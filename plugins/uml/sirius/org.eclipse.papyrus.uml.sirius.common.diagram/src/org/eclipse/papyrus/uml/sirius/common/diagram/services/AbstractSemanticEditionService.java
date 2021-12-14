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
package org.eclipse.papyrus.uml.sirius.common.diagram.services;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;


public abstract class AbstractSemanticEditionService {
	protected final IElementEditService getCommandProvider(final EObject eobject) {
		return ElementEditServiceUtils.getCommandProvider(eobject);
	}
	protected final ICommand getGMFEditCommand(final EObject eobject, final IEditCommandRequest request) {
		final IElementEditService provider = getCommandProvider(eobject);
		final ICommand cmd = provider.getEditCommand(request);
		return cmd;
	}
	protected final Command getEMFEditCommand(final EObject eobject, final IEditCommandRequest request) {
		final ICommand cmd = getGMFEditCommand(eobject, request);
		return cmd == null ? null : GMFtoEMFCommandWrapper.wrap(cmd);
	}
	//
	// protected final IElementType getElementType(EClass eClass) {
	// IElementType elementType = ElementTypeRegistry.getInstance().getElementType(eClass);
	// if(elementType==null) {
	// ElementTypeSetConfigurationRegistry.getInstance();
	// elementType = ElementTypeRegistry.getInstance().getElementType(eClass);
	// }
	// return elementType;
	// }
	protected final TransactionalEditingDomain getEditingDomain(final EObject eobject) {
		return TransactionUtil.getEditingDomain(eobject);// TODO check another way in Papyrus
	}
	protected final boolean isPapyrusResource(final EObject eobject) {
		final Resource res = eobject.eResource();
		if (res != null) {
			ResourceSet rset = res.getResourceSet();
			return rset instanceof ModelSet;
		}
		return false;
	}
	protected final IElementType getElementType(final String elementTypeId) {
		IElementType elementType = ElementTypeRegistry.getInstance().getType(elementTypeId);
		if (elementType == null) { // initialize Papyrus element type when we are in a pure Sirius context
			// si on initialise �a dans un context Sirius, la cr�ation depuis la palette d'un diagramme de Classe papyrus ne marche plus!!!
			// mais �a marche presque parfaitement en Sirius natif...
			// System.out.println("loading elementtypesetconfiguration");
			// ElementTypeSetConfigurationRegistry.getInstance();
			// elementType = ElementTypeRegistry.getInstance().getType(elementTypeId);
			// System.out.println("loaded");
		}
		return elementType;
	}
//	protected final boolean isInCurrentArchitecture(final EObject eobject, final IElementType elementType) {
//		if (isPapyrusResource(eobject)) {
//			MergedArchitectureContext str = new ArchitectureDescriptionUtils((ModelSet) eobject.eResource().getResourceSet()).getArchitectureContext();
//			for (ElementTypeSetConfiguration current : str.getElementTypes()) {
//				// if (!UML_METAMODEL_URI.equals(typeSet.getMetamodelNsURI())) {
//				// continue;
//				// }
//				for (ElementTypeConfiguration tmp : current.getElementTypeConfigurations()) {
//					String hint = tmp.getHint();
//					String id = tmp.getIdentifier();
//					String name = tmp.getName();
//					System.out.println("hint=" + hint);
//					System.out.println("id=" + id);
//					System.out.println("name=" + name);
//				}
//			}
//			// TODO : to be continued when we will be able to open sirius in papyrus
//		}
//		return false;
//	}
}
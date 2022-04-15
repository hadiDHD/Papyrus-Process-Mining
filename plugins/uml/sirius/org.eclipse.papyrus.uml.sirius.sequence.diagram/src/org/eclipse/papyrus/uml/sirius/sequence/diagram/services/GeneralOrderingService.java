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
 *    Yann Binot (ARTAL) - yann.binot@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.services;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.LabelServices;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.GeneralOrdering;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The Class GeneralOrderingService.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
@SuppressWarnings("restriction")
public class GeneralOrderingService {

	/** The service. */
	static private GeneralOrderingService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private GeneralOrderingService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public GeneralOrderingService getInstance() {
		if (service == null) {
			service = new GeneralOrderingService();
		}
		return service;
	}


	/**
	 * Creates the general ordering.
	 *
	 * @param context
	 *            the context
	 * @param sourceVariable
	 *            the source variable
	 * @param targetVariable
	 *            the target variable
	 * @return the general ordering
	 */
	public GeneralOrdering createGeneralOrdering(EObject context, EObject sourceVariable, EObject targetVariable) {
		if (context instanceof DNodeSpec) {
			EObject target = ((DNodeSpec) context).getTarget();
			if (target instanceof ExecutionSpecification) {
				InteractionFragment interaction = FragmentsService.getInstance().getEnclosingFragment(target);

				GeneralOrdering generalOrdering = UMLFactory.eINSTANCE.createGeneralOrdering();
				interaction.getGeneralOrderings().add(generalOrdering);
				generalOrdering.setName(computeDefaultName(generalOrdering));


				generalOrdering.setBefore(((ExecutionSpecification) sourceVariable).getFinish());
				generalOrdering.setAfter(((ExecutionSpecification) targetVariable).getStart());

				return generalOrdering;
			}
		}
		return null;
	}



	/**
	 * Compute default name.
	 *
	 * @param element
	 *            New element
	 * @return Name for the new element, he name will looks like
	 *         'ElementType'+total of existing elements of the same type.
	 */
	private String computeDefaultName(final EObject element) {
		return LabelServices.INSTANCE.computeDefaultName(element);
	}

}

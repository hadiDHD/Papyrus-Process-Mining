/******************************************************************************
 * Copyright (c) 2021, 2022 CEA LIST, Artal Technologies
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
 *  Vincent Lorenzo (CEA LIST) - vincent.lorenzo@cea.fr - Bug 579701
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.internal.viewpoint;

import org.eclipse.emf.ecore.EObject;

/**
 * Interface for ViewPrototype:
 * <ul>
 * <li>to get the result of the instantiation instead of a boolean</li>
 * <li>to add creation check</li>
 * </ul>
 * TODO : move me into Papyrus
 */
public interface ExtendedViewPrototype<T extends EObject> {

	/**
	 *
	 * @param semanticOwner
	 *            the semantic owner of the instantiated view
	 * @param graphicalOwner
	 *            the graphical owner of the instantiated view
	 * @param name
	 *            the name of the instantiated view
	 * @param openCreatedView
	 *            if <code>true</code> the created view will be open
	 * @return
	 *         the instantiated view
	 */
	public T instantiateOn(EObject semanticOwner, EObject graphicalOwner, String name, boolean openCreatedView);

	/**
	 * This method can be used to execute checks in addition of those defined in the architecture file
	 * 
	 * @param semanticOwner
	 *            the selected element used as semantic owner for the creation.
	 * @return
	 *         <code>true</code> if the semanticOwner can be used as semantic owner for the view to create
	 */
	//TODO : when we will add it in Papyrus, the method PolicyChecker.getFor(selection).getPrototypesFor(selection) must call me.
	public boolean canInstantianteOn(EObject semanticOwner);
}

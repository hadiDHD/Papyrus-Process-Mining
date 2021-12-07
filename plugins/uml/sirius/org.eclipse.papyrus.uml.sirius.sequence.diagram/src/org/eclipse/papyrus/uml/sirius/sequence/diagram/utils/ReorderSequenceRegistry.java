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
package org.eclipse.papyrus.uml.sirius.sequence.diagram.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * The Class ReorderSequenceRegistry.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
/**
 * The Class ReorderSequenceRegistry.
 */
public class ReorderSequenceRegistry {

	/** The instance. */
	static private ReorderSequenceRegistry instance;

	/**
	 * Instantiates a new reorder sequence registry.
	 */
	private ReorderSequenceRegistry() {

	}

	/**
	 * Gets the single instance of ReorderSequenceRegistry.
	 *
	 * @return single instance of ReorderSequenceRegistry
	 */
	static public ReorderSequenceRegistry getInstance() {
		if (instance == null) {
			instance = new ReorderSequenceRegistry();
		}
		return instance;
	}


	/** The registry. */
	static Map<EObject, EObject> registry = new HashMap<>();




	/**
	 * Clear.
	 */
	public void clear() {
		registry.clear();
	}

	/**
	 * Put.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void put(EObject key, EObject value) {
		registry.put(key, value);
	}

	/**
	 * Removes the.
	 *
	 * @param key
	 *            the key
	 */
	public void remove(EObject key) {
		registry.remove(key);
	}


	/**
	 * Contains.
	 *
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public boolean containsKey(EObject value) {
		return registry.containsKey(value);
	}

	/**
	 * Contains.
	 *
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public boolean containsValue(EObject value) {
		return registry.containsValue(value);
	}

	/**
	 * Gets the.
	 *
	 * @param key
	 *            the key
	 * @return the e object
	 */
	public EObject get(EObject key) {
		return registry.get(key);
	}


}

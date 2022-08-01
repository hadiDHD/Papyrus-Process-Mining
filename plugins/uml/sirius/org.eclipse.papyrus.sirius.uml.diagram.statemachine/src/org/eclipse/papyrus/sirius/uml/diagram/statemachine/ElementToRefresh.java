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
package org.eclipse.papyrus.sirius.uml.diagram.statemachine;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.ContainerLayout;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.uml2.uml.StateMachine;

/**
 * The Class ElementToRefresh.
 * This class allows to keep in memory Sirius graphical elements to refresh. Refresh the position or the ContainerLayout.
 * 
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class ElementToRefresh {


	/** The Constant toReposition. */
	public static final Map<AbstractDNode, Rectangle> toReposition = new HashMap<>();

	/** The map. */
	public static Map<DNodeContainer, ContainerLayout> map = new HashMap<DNodeContainer, ContainerLayout>();

	/** The initial state machine. */
	private static StateMachine initialStateMachine = null;




	/**
	 * Adds the position element.
	 *
	 * @param element
	 *            the element
	 * @param rec
	 *            the rec
	 */
	static public void addPositionElement(AbstractDNode element, Rectangle rec) {
		toReposition.put(element, rec);
	}




	/**
	 * Save state machine.
	 *
	 * @param stateMachine
	 *            the state machine
	 */
	public static void saveStateMachine(StateMachine stateMachine) {
		initialStateMachine = stateMachine;
	}

	/**
	 * Gets the initial state machine.
	 *
	 * @return the initial state machine
	 */
	public static StateMachine getInitialStateMachine() {
		return initialStateMachine;
	}




}

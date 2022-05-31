/*****************************************************************************
 * Copyright (c) 2022 CEA LIST
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
package org.eclipse.papyrus.uml.diagram.specs.extractor.utils;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class is used to store the creation capabilities of each Link
 */
public class LinkRepresentationCapability {

	/**
	 * the "metaclass" of the link (the tool_label in the palette UI)
	 */
	private String metaclassLink;

	/**
	 * the tested source for the link
	 */
	private String metaclassSource;

	/**
	 * key : target "metaclass" (the tool_label in the palette UI)
	 * value : true -> link can be created
	 */
	private Map<String, Boolean> targetAndResult = new TreeMap<>();

	/**
	 * boolean indicating if the link can be self reflective (same source and target)
	 */
	private boolean isReflexive = false;

	/**
	 * 
	 * Constructor.
	 *
	 * @param metaclassLink
	 *            the "metaclass" of the link (the tool_label in the palette UI)
	 * 
	 * @param metaclassSource
	 *            the tested source for the link
	 */
	public LinkRepresentationCapability(final String metaclassLink, final String metaclassSource) {
		this.metaclassLink = metaclassLink;
		this.metaclassSource = metaclassSource;
	}

	/**
	 * 
	 * @return
	 *         the metaclass/label of the link
	 */
	public String getLinkMetaclass() {
		return this.metaclassLink;
	}

	/**
	 * 
	 * @param isReflexive
	 *            if <code>true</code> the source and the target of the link can be the same element
	 */
	public void setReflexive(boolean isReflexive) {
		this.isReflexive = isReflexive;
	}

	/**
	 * 
	 * @param targetMetaclass
	 * @param accepted
	 *            true, when the target can be used for the represented link and the selected metaclass source
	 */
	public void addTargetResult(final String targetMetaclass, final boolean accepted) {
		if (!this.targetAndResult.containsKey(targetMetaclass)) {
			// if we doesn't yet have a value
			this.targetAndResult.put(targetMetaclass, Boolean.valueOf(accepted));
		} else if (!this.targetAndResult.get(targetMetaclass).booleanValue()) {
			// we already have a value, we only erase when the previous value is false
			this.targetAndResult.put(targetMetaclass, Boolean.valueOf(accepted));
		}
	}


	/**
	 * 
	 * @return
	 *         the map of the result
	 */
	public Map<String, Boolean> getTargetResult() {
		return this.targetAndResult;
	}

	/**
	 * 
	 * @return
	 *         <code>true</code> when the source and the target of the link can be the same element
	 */
	public boolean isReflexive() {
		return this.isReflexive;
	}

	/**
	 * 
	 * @return
	 *         the metaclass of the current represented link
	 */
	public String getMetaclassLink() {
		return this.metaclassLink;
	}

	/**
	 * 
	 * @return
	 *         the metaclass of the source
	 */
	public String getMetaclassSource() {
		return this.metaclassSource;
	}
}

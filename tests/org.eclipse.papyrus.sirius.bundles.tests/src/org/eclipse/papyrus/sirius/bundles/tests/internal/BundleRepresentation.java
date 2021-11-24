/*****************************************************************************
 * Copyright (c) 2021 CEA LIST.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Vincent Lorenzo (CEA LIST) vincent.lorenzo@cea.fr - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.sirius.bundles.tests.internal;


/**
 * This class allows to represent a bundle declaration in the Manifest file
 */
public class BundleRepresentation {

	/**
	 * The name of the bundle (org.eclipse.papyrus.sirius....)
	 */
	private final String symbolicName;

	/**
	 * the required version for the bundle
	 */
	private final Version version;

	/**
	 * a boolean indicating if the bundle is reexported
	 */
	private final boolean isReexported;

	/**
	 * a boolean indicating id the bundle is declared as optional
	 */
	private final boolean isOptional;

	/**
	 * a boolean indicating id the bundle is declared as greedy
	 */
	private final boolean isGreedy;


	/**
	 * Constructor.
	 *
	 * @param bundleName
	 *            (org.eclipse.papyrus.sirius....)
	 * @param version
	 * @param isReexported
	 * @param isOptional
	 */
	public BundleRepresentation(final String symbolicName, final Version version, final boolean isReexported, final boolean isOptional, final boolean isGreedy) {
		super();
		this.isReexported = isReexported;
		this.isOptional = isOptional;
		this.version = version;
		this.symbolicName = symbolicName;
		this.isGreedy = isGreedy;
	}

	/**
	 * @return the isReexported
	 */
	public boolean isReexported() {
		return this.isReexported;
	}

	/**
	 * @return the isOptional
	 */
	public boolean isOptional() {
		return this.isOptional;
	}

	/**
	 * @return the version
	 */
	public Version getVersion() {
		return this.version;
	}

	/**
	 * @return the symbolic name (org.eclipse.papyrus.sirius....)
	 */
	public String getSymbolicName() {
		return this.symbolicName;
	}

	/**
	 *
	 * @return the isGreedy
	 */
	public boolean isGreedy() {
		return this.isGreedy;
	}

}

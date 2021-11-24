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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * duplicated class from Papyrus
 */
public class Version {

	private boolean minIncluding;

	private boolean maxIncluding;

	private int[] min = null;

	private int[] max = null;

	public Version(final String versionAsString) {
		this.minIncluding = true;
		this.maxIncluding = true;
		if (versionAsString != null) {
			this.minIncluding = !versionAsString.startsWith("("); //$NON-NLS-1$
			this.maxIncluding = !versionAsString.endsWith(")"); //$NON-NLS-1$
			final Pattern versionNumber = Pattern.compile("\\d+(\\.\\d+)*"); //$NON-NLS-1$
			final Matcher matcher = versionNumber.matcher(versionAsString);
			while (matcher.find()) {
				final String grp = matcher.group();
				final String[] versions = grp.split("\\."); //$NON-NLS-1$
				int[] vers = new int[versions.length];
				for (int i = 0; i < versions.length; i++) {
					vers[i] = Integer.parseInt(versions[i]);
				}
				if (min == null) {
					min = vers;
				} else {
					max = vers;
				}
			}
		}
		if (min == null) {
			min = new int[] { 0, 0, 0 };
		}
		if (max == null) {
			max = new int[] { 99, 99, 99 };
		}
	}

	/**
	 * not provided by Papyrus API
	 */
	public String getMinVersion() {
		final StringBuilder builder = new StringBuilder();


		for (int i = 0; i < min.length; i++) {
			builder.append(min[i]);
			if (i < min.length - 1) {
				builder.append(".");
			}
		}
		return builder.toString();
	}

	/**
	 * not provided by Papyrus API
	 */
	public String getMaxVersion() {
		final StringBuilder builder = new StringBuilder();


		for (int i = 0; i < max.length; i++) {
			builder.append(max[i]);
			if (i < max.length - 1) {
				builder.append(".");
			}
		}
		return builder.toString();
	}

	public boolean inIncludedIn(final Version version) {
		// verifying intersection between versions!
		if (compare(this.max, version.min) < 0) {
			return false;
		}
		if (compare(version.max, this.min) < 0) {
			return false;
		}
		if (compare(this.max, version.min) == 0 && (!this.maxIncluding || !version.minIncluding)) {
			return false;
		}
		if (compare(version.max, this.min) == 0 && (!version.maxIncluding || !this.minIncluding)) {
			return false;
		}

		// verifying inclusion
		if (compare(this.min, version.min) < 0) {
			return false;
		}

		if (compare(this.min, version.min) == 0 && (this.minIncluding != version.minIncluding)) {
			return false;
		}

		if (compare(this.max, version.max) > 0) {
			return false;
		}

		if (compare(this.max, version.max) == 0 && (this.maxIncluding != version.maxIncluding)) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param first
	 * @param second
	 * @return
	 *         <ul>
	 *         <li>0 when they are equal</li>
	 *         <li>1 if first is greater than second</li>
	 *         <li>-1 if first is smaller than second</li>
	 *         </ul>
	 */
	protected int compare(int[] first, int[] second) {
		int min = Math.min(first.length, second.length);
		for (int i = 0; i < min; i++) {
			if (first[i] < second[i]) {
				return -1;
			} else if (first[i] > second[i]) {
				return 1;
			}
		}
		if (first.length == second.length) {
			return 0;
		} else if (first.length > second.length) {
			return 1;
		}
		return -1;
	}

	/**
	 *
	 * @return
	 *         <code>true</code> if the min version is declared as included
	 */
	public boolean isMinIncluded() {
		return this.minIncluding;
	}

	/**
	 *
	 * @return
	 *         <code>true</code> if the max version is declared as included
	 */
	public boolean isMaxIncluded() {
		return this.maxIncluding;
	}

	/**
	 *
	 * @return
	 *         <code>true</code>
	 */
	public boolean isDefaultMinVersion() {
		return "0.0.0".equals(getMinVersion());
	}

	/**
	 *
	 * @return
	 *         <code>true</code>
	 */
	public boolean isDefaultMaxVersion() {
		return "99.99.99".equals(getMaxVersion());
	}
}
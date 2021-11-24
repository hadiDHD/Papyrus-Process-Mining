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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.Bundle;

@SuppressWarnings("nls")
public class PapyrusSiriusBundleUtils {

	/**
	 * Papyrus-Sirius prefix bundles
	 */
	private static final String PAPYRUS_SIRIUS_PREFIX = "org.eclipse.papyrus.sirius."; //$NON-NLS-1$

	/**
	 *
	 * @return the Bundle with a name beginning by {@link #PAPYRUS_PREFIX}
	 */
	public static final List<Bundle> getPapyrusSiriusBundles() {
		return org.eclipse.papyrus.sirius.bundles.tests.internal.BundleTestsUtils.getBundleByPrefix(PAPYRUS_SIRIUS_PREFIX);
	}

	/**
	 *
	 * @param bundle
	 *            a bundle
	 * @return
	 *         a list of BundleRepresentation representing the declared dependencies in the Manifest.MF file
	 */
	public static final List<BundleRepresentation> getBundleDependencies(final Bundle bundle) {
		final List<BundleRepresentation> bundleRep = new ArrayList<>();


		final String dependenciesString = bundle.getHeaders().get(BundleTestsUtils.REQUIRE_BUNDLE);

		if (dependenciesString == null || dependenciesString.isEmpty()) {
			return bundleRep;
		}
		// split without eating the previous char
		// (?<=X) X, via zero-width positive lookbehind
		// https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
		List<String> dependencies = Arrays.asList(dependenciesString.split("(?<=\\D),")); //$NON-NLS-1$

		for (final String current : dependencies) {
			final String bundleName;
			if (!current.contains(";")) { //$NON-NLS-1$
				bundleName = current;
			} else {
				bundleName = current.substring(0, current.indexOf(";")); //$NON-NLS-1$
			}

			Version version = null;
			Pattern p = Pattern.compile(";bundle-version=\"([^\"]*)\"?"); //$NON-NLS-1$
			Matcher matcher = p.matcher(current);
			while (matcher.find()) {
				int groupCount = matcher.groupCount();
				if (groupCount > 0) {
					final String str2 = matcher.group(1);
					version = new Version(str2);
				}
			}
			boolean isOptional = current.contains("resolution:=optional"); //$NON-NLS-1$
			boolean isReexported = current.contains("visibility:=reexport"); //$NON-NLS-1$
			boolean isGreedy = current.contains("x-installation:=greedy"); //$NON-NLS-1$
			bundleRep.add(new BundleRepresentation(bundleName, version, isReexported, isOptional, isGreedy));
		}

		return bundleRep;
	}

	/**
	 *
	 * @param bundle
	 *            a bundle
	 * @return
	 *         a list of BundleRepresentation representing the declared import package in the Manifest.MF file
	 */
	public static final List<BundleRepresentation> getBundleImportPackage(final Bundle bundle) {
		final List<BundleRepresentation> bundleRep = new ArrayList<>();


		final String dependenciesString = bundle.getHeaders().get(BundleTestsUtils.BUNDLE_IMPORT_PACKAGE);

		if (dependenciesString == null || dependenciesString.isEmpty()) {
			return bundleRep;
		}
		// split without eating the previous char
		// (?<=X) X, via zero-width positive lookbehind
		// https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
		List<String> dependencies = Arrays.asList(dependenciesString.split("(?<=\\D),")); //$NON-NLS-1$

		for (final String current : dependencies) {
			final String bundleName;
			if (!current.contains(";")) { //$NON-NLS-1$
				bundleName = current;
			} else {
				bundleName = current.substring(0, current.indexOf(";")); //$NON-NLS-1$
			}

			Version version = null;
			Pattern p = Pattern.compile(";version=\"([^\"]*)\"?"); //$NON-NLS-1$
			Matcher matcher = p.matcher(current);
			while (matcher.find()) {
				int groupCount = matcher.groupCount();
				if (groupCount > 0) {
					final String str2 = matcher.group(1);
					version = new Version(str2);
				}
			}
			boolean isOptional = current.contains("resolution:=optional"); //$NON-NLS-1$
			boolean isReexported = current.contains("visibility:=reexport"); //$NON-NLS-1$
			boolean isGreedy = current.contains("x-installation:=greedy"); //$NON-NLS-1$
			bundleRep.add(new BundleRepresentation(bundleName, version, isReexported, isOptional, isGreedy));
		}

		return bundleRep;
	}

}

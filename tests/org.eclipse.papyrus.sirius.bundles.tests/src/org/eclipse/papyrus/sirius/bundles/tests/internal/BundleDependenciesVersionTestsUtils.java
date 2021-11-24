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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.osgi.util.NLS;
import org.junit.Assert;
import org.osgi.framework.Bundle;

/**
 * This class provides useful method to check the unicity of dependency version
 *
 */
public class BundleDependenciesVersionTestsUtils {

	/**
	 *
	 * This method checks all Papyrus-Sirius dependencies are expected with the same minor/major version
	 *
	 * @param mapOfDependencies
	 *            a map of bundles as key and their dependencies as values
	 *
	 */
	public static void checkUnicityVersionOfDependencies(final Map<Bundle, List<BundleRepresentation>> mapOfDependencies) {
		final Map<String, DependencyVersionCount> wantedMinPluginVersion = new TreeMap<>();
		final Map<String, DependencyVersionCount> wantedMaxPluginVersion = new TreeMap<>();

		for (Entry<Bundle, List<BundleRepresentation>> current : mapOfDependencies.entrySet()) {
			final Bundle currentBundle = current.getKey();
			String bundleName = currentBundle.getHeaders().get("Bundle-SymbolicName"); //$NON-NLS-1$
			bundleName = bundleName.replaceAll(";singleton:=true", ""); //$NON-NLS-1$ //$NON-NLS-2$

			for (final BundleRepresentation bundleRep : current.getValue()) {
				final Version version = bundleRep.getVersion();
				if (null != version) {
					DependencyVersionCount minVersion = wantedMinPluginVersion.get(bundleRep.getSymbolicName());
					if (null == minVersion) {
						minVersion = new DependencyVersionCount(bundleRep.getSymbolicName(), true);
						wantedMinPluginVersion.put(bundleRep.getSymbolicName(), minVersion);
					}
					minVersion.updateCountForVersion(version.getMinVersion());

					DependencyVersionCount maxVersion = wantedMaxPluginVersion.get(bundleRep.getSymbolicName());
					if (null == maxVersion) {
						maxVersion = new DependencyVersionCount(bundleRep.getSymbolicName(), false);
						wantedMaxPluginVersion.put(bundleRep.getSymbolicName(), maxVersion);
					}
					maxVersion.updateCountForVersion(version.getMaxVersion());

				}
			}
		}


		// know we cross all dependencies and check the uniqueness of the required versions

		final StringBuilder minVersionbuilder = new StringBuilder();
		final StringBuilder maxVersionbuilder = new StringBuilder();

		final Iterator<Entry<String, DependencyVersionCount>> minIter = wantedMinPluginVersion.entrySet().iterator();
		if (minIter.hasNext()) {
			while (minIter.hasNext()) {
				final Entry<String, DependencyVersionCount> current = minIter.next();
				DependencyVersionCount versionCount = current.getValue();
				if (!versionCount.isUnique()) {
					minVersionbuilder.append(versionCount.toString());
					if (minIter.hasNext()) {
						minVersionbuilder.append("\n"); //$NON-NLS-1$
					}
				}
			}
		}

		final Iterator<Entry<String, DependencyVersionCount>> maxIter = wantedMaxPluginVersion.entrySet().iterator();
		if (maxIter.hasNext()) {
			while (maxIter.hasNext()) {
				final Entry<String, DependencyVersionCount> current = maxIter.next();
				DependencyVersionCount versionCount = current.getValue();
				if (!versionCount.isUnique()) {
					maxVersionbuilder.append(versionCount.toString());
					if (maxIter.hasNext()) {
						maxVersionbuilder.append("\n"); //$NON-NLS-1$
					}
				}
			}
		}

		final StringBuilder finalBuilder = new StringBuilder();
		if (minVersionbuilder.length() > 0) {
			finalBuilder.append("\n"); //$NON-NLS-1$
			finalBuilder.append(minVersionbuilder);
		}

		if (maxVersionbuilder.length() > 0) {
			finalBuilder.append("\n"); //$NON-NLS-1$
			finalBuilder.append(maxVersionbuilder);
		}



		Assert.assertTrue(finalBuilder.toString(), finalBuilder.length() == 0);
	}

	/**
	 *
	 * This method checks all Papyrus-Sirius dependencies are declared with a minor/major version
	 *
	 * @param mapOfDependencies
	 *            a map of bundles as key and their dependencies as values
	 *
	 */
	public static void checkDependencyVersionIsDeclared(final Map<Bundle, List<BundleRepresentation>> mapOfDependencies) {
		StringBuilder builder = new StringBuilder();
		for (Entry<Bundle, List<BundleRepresentation>> current : mapOfDependencies.entrySet()) {
			final Bundle currentBundle = current.getKey();
			String bundleName = currentBundle.getHeaders().get("Bundle-SymbolicName"); //$NON-NLS-1$
			bundleName = bundleName.replaceAll(";singleton:=true", ""); //$NON-NLS-1$ //$NON-NLS-2$

			for (final BundleRepresentation bundleRep : current.getValue()) {
				final Version version = bundleRep.getVersion();
				if (null == version) {
					builder.append(NLS.bind("The plugin {0} don't declare dependency version for {1}.\n", bundleName, bundleRep.getSymbolicName())); //$NON-NLS-1$
				} else {
					if (version.isDefaultMinVersion()) {
						builder.append(NLS.bind("The plugin {0} don't declare min dependency version for {1}.\n", bundleName, bundleRep.getSymbolicName())); //$NON-NLS-1$
					}
					if (version.isDefaultMaxVersion()) {
						builder.append(NLS.bind("The plugin {0} don't declare max dependency version for {1}.\n", bundleName, bundleRep.getSymbolicName())); //$NON-NLS-1$
					}
				}
			}
		}
		Assert.assertTrue(builder.toString(), builder.length() == 0);

	}

	/**
	 *
	 * This method checks all Papyrus-Sirius dependencies are declared with an included minor version
	 *
	 * @param mapOfDependencies
	 *            a map of bundles as key and their dependencies as values
	 *
	 */
	public static void checkIncludedMinorDependencyVersion(final Map<Bundle, List<BundleRepresentation>> mapOfDependencies) {
		StringBuilder builder = new StringBuilder();
		for (Entry<Bundle, List<BundleRepresentation>> current : mapOfDependencies.entrySet()) {
			final Bundle currentBundle = current.getKey();
			String bundleName = currentBundle.getHeaders().get("Bundle-SymbolicName"); //$NON-NLS-1$
			bundleName = bundleName.replaceAll(";singleton:=true", ""); //$NON-NLS-1$ //$NON-NLS-2$

			for (final BundleRepresentation bundleRep : current.getValue()) {
				final Version version = bundleRep.getVersion();
				if (null != version) {
					if (false == version.isMinIncluded()) {
						builder.append(NLS.bind("The plugin {0} don't declare the minor version as included for {1}.\n", bundleName, bundleRep.getSymbolicName())); //$NON-NLS-1$
					}
				}
			}
		}
		Assert.assertTrue(builder.toString(), builder.length() == 0);

	}

	/**
	 *
	 * This method checks all Papyrus-Sirius dependencies are declared with an excluded major version
	 *
	 * @param mapOfDependencies
	 *            a map of bundles as key and their dependencies as values
	 *
	 */
	public static void checkExludedMajorDependencyVersion(final Map<Bundle, List<BundleRepresentation>> mapOfDependencies) {
		StringBuilder builder = new StringBuilder();
		for (Entry<Bundle, List<BundleRepresentation>> current : mapOfDependencies.entrySet()) {
			final Bundle currentBundle = current.getKey();
			String bundleName = currentBundle.getHeaders().get("Bundle-SymbolicName"); //$NON-NLS-1$
			bundleName = bundleName.replaceAll(";singleton:=true", ""); //$NON-NLS-1$ //$NON-NLS-2$

			for (final BundleRepresentation bundleRep : current.getValue()) {
				final Version version = bundleRep.getVersion();
				if (null != version) {
					if (true == version.isMaxIncluded()) {
						builder.append(NLS.bind("The plugin {0} don't declare the major version as excluded for {1}.\n", bundleName, bundleRep.getSymbolicName())); //$NON-NLS-1$

					}
				}
			}
		}
		Assert.assertTrue(builder.toString(), builder.length() == 0);
	}

	/**
	 * This class allows to count the different expected version for a given dependency
	 */
	private static class DependencyVersionCount {

		private final Map<String, Integer> nbDeclarationByVersion = new TreeMap<>();

		private final String dependencyName;

		private final boolean isMin;

		public DependencyVersionCount(final String dependencyName, final boolean isMin) {
			Assert.assertNotNull(dependencyName);
			Assert.assertTrue(dependencyName.length() > 0);
			this.dependencyName = dependencyName;
			this.isMin = isMin;
		}

		public void updateCountForVersion(final String version) {
			int count = nbDeclarationByVersion.get(version) != null ? nbDeclarationByVersion.get(version).intValue() : 0;
			count++;
			this.nbDeclarationByVersion.put(version, Integer.valueOf(count));
		}

		public boolean isUnique() {
			return this.nbDeclarationByVersion.size() == 1;
		}

		/**
		 * @see java.lang.Object#toString()
		 *
		 * @return
		 */
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			final String version = isMin ? "MIN version" : "MAX version"; //$NON-NLS-1$ //$NON-NLS-2$
			String[] replacement = new String[] { this.dependencyName, Integer.toString(nbDeclarationByVersion.size()), version };
			builder.append(NLS.bind("{0} dependency is declared with {1} different {2} numbers.", replacement)); //$NON-NLS-1$
			if (!isUnique()) {
				final Iterator<Entry<String, Integer>> iter = nbDeclarationByVersion.entrySet().iterator();
				while (iter.hasNext()) {
					final Entry<String, Integer> current = iter.next();
					builder.append("\n\t"); //$NON-NLS-1$
					builder.append(NLS.bind("{0} declaration as {1}", current.getValue().toString(), current.getKey())); //$NON-NLS-1$
				}
			}
			return builder.toString();
		}
	}
}

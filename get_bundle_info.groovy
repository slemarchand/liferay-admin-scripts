// MIT License
//
// Copyright (c) 2018-present Sebastien Le Marchand
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

//
// get_bundle_info.groovy
//
// Get information about an OSGi bundle.
//
// Tested under the following Liferay versions: 7.0.
//
// Getting other scripts: https://github.com/slemarchand/liferay-admin-scripts

//
// -- Constants (to update at your convenience) 
//

// The symbolic name of the bundle to search

BUNDLE_SYMBOLIC_NAME ="com.liferay.portal.search"

// The version of the bundle to search. Can be left blank.

BUNDLE_VERSION = ""

//
// -- Implementation
//

import com.liferay.portal.scripting.groovy.internal.*;
import org.osgi.framework.*;
import java.util.*;

PORTLET = binding.hasVariable("actionRequest");

states = [1:  "UNINSTALLED", 2: "INSTALLED", 4: "RESOLVED", 8: "STARTING", 10: "STOPPING", 32: "ACTIVE"];

Bundle[] bundles = FrameworkUtil.getBundle(GroovyExecutor.class).getBundleContext().getBundles();


Bundle foundBundle = null;

for (bundle in bundles){
	if(bundle.getSymbolicName().equals(BUNDLE_SYMBOLIC_NAME)) {
		if(foundBundle == null && (BUNDLE_VERSION == "" || bundle.getVersion().toString().equals(BUNDLE_VERSION))) {
			foundBundle = bundle;
		}
	}
}

if(foundBundle != null) {

	b = foundBundle

	state = states.get(b.getState());

	println("""
BundleId: ${b.getBundleId()}
Version: ${b.getVersion()}
State: ${state}
LastModified: ${b.getLastModified()}
Location: ${b.getLocation()}

Headers ===
		""");

	headers = b.getHeaders();	

	keys = headers.keys();

	while(keys.hasMoreElements()) {
		name = keys.nextElement();
		value = headers.get(name);
		println("${name}: ${value}");
	}

	println("""
Bundles in use ===
		""");
	bundlesInUse = new HashSet();
	for (s in b.getServicesInUse()){
		bundlesInUse.add(s.getBundle());
	}
	for (sb in bundlesInUse){
		println("${sb.getSymbolicName()} ${sb.getVersion()}");
	}
} else {	
	message = "Bundle not found for symbolic name ${BUNDLE_SYMBOLIC_NAME} and version ${BUNDLE_VERSION==''?'*':BUNDLE_VERSION}";
	if(PORTLET) {
		println(message)
	} else {
		throw new Exception(message);
	}
}


/**
 * Copyright (c) 2013 Sébastien Le Marchand, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

//
// remove_layoutprototypr_link.groovy
//
// Remove LayoutPrototype link for each layouts in a group
//

// Variables to update at your convenience

groupId = 10190L // The site where removing LayoutPrototype links

// Implementation

import com.liferay.portal.kernel.util.*
import com.liferay.portal.model.*
import com.liferay.portal.service.*
import com.liferay.portal.util.*

try {
	layouts = new ArrayList<Layout>(2)
	layouts.addAll(LayoutLocalServiceUtil.getLayouts(groupId, true))
	layouts.addAll(LayoutLocalServiceUtil.getLayouts(groupId, false))
	layouts.each{ l ->
		if(l.getLayoutPrototypeLinkEnabled() 
			|| Validator.isNotNull(l.getLayoutPrototypeUuid())) {
			l.setLayoutPrototypeLinkEnabled(false)
			l.setLayoutPrototypeUuid(StringPool.BLANK)
			LayoutLocalServiceUtil.updateLayout(l)
		}
	}
} catch (e) {
	e.printStackTrace(out)
}
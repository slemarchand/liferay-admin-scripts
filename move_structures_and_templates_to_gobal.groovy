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
// move_structures_and_templates_to_global.groovy
//
// Move structures and templates from a site to the Global site
//

// Variables to update at your convenience

groupId = 10190L // The source site

// Implementation

import com.liferay.portal.util.*
import com.liferay.portlet.journal.model.*
import com.liferay.portlet.journal.service.*

try {

	companyGroupId = PortalUtil.getCompany(actionRequest).getGroup().getGroupId()

	structures = JournalStructureLocalServiceUtil.getStructures(groupId)
	structures.each{ s ->
		s.setGroupId(companyGroupId)
		JournalStructureLocalServiceUtil.updateJournalStructure(s)
	}
	templates = JournalTemplateLocalServiceUtil.getTemplates(groupId)
	templates.each{ t ->
		t.setGroupId(companyGroupId)
		JournalTemplateLocalServiceUtil.updateJournalTemplate(t)
	}

} catch (e) {
	e.printStackTrace(out)
}

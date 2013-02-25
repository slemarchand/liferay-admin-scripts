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
// refactor_template_ids.groovy
//
// Refactor some templates IDs
//

// Variables to update at your convenience

groupId = 10190L // The site from getting templates

includeGlobalTemplates = true

map = [
	'12807':'HEADER-TPL',
	'14501':'PIECE-OF-NEWS-TPL', 
	'15901':'FOOTER-TPL'
	] 

// Implementation

import com.liferay.portal.kernel.dao.orm.*
import com.liferay.portal.util.*
import com.liferay.portlet.journal.model.*
import com.liferay.portlet.journal.service.*
	
try {

	map.each{ oldId, newId ->
		t = JournalTemplateLocalServiceUtil.getTemplate(groupId, oldId, includeGlobalTemplates)
		t.setTemplateId(newId)
		JournalTemplateLocalServiceUtil.updateJournalTemplate(t)
		
		q = DynamicQueryFactoryUtil.forClass(JournalArticle.class)
		q.add(PropertyFactoryUtil.forName('templateId').eq(oldId))
		articles = JournalArticleLocalServiceUtil.dynamicQuery(q)
		articles.each{ a ->
			a.setTemplateId(newId)
			JournalArticleLocalServiceUtil.updateJournalArticle(a)
		}
	}

} catch (e) {
	e.printStackTrace(out)
}
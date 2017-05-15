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
// refactor_structure_ids.groovy
//
// Refactor some structures IDs
//

// Variables to update at your convenience

groupId = 10190L // The site from getting structures

includeGlobalStructures = true

map = [
	'10904':'HEADER-STR',
	'11501':'PIECE-OF-NEWS-STR', 
	'11803':'FOOTER-STR'
	] 

// Implementation

import com.liferay.portal.kernel.dao.orm.*
import com.liferay.portal.util.*
import com.liferay.portlet.journal.model.*
import com.liferay.portlet.journal.service.*
import com.liferay.portlet.dynamicdatamapping.model.*
import com.liferay.portlet.dynamicdatamapping.service.*
import com.liferay.portal.service.ClassNameLocalServiceUtil

try {

    map.each{ oldId, newId ->

        cId = ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class.getName())

        s = DDMStructureLocalServiceUtil.getStructure(groupId, cId, oldId, includeGlobalStructures)
        s
        s.setStructureKey(newId)
        DDMStructureLocalServiceUtil.updateDDMStructure(s)


        q = DynamicQueryFactoryUtil.forClass(JournalArticle.class)
        q.add(PropertyFactoryUtil.forName('structureId').eq(oldId))
        articles = JournalArticleLocalServiceUtil.dynamicQuery(q)
        articles.each{ a ->
            a.setStructureId(newId)
            JournalArticleLocalServiceUtil.updateJournalArticle(a)
        }

    }

} catch (e) {
    e.printStackTrace(out)
}

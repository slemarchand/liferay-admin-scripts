//
// refactor_structure_ids.groovy
//
// Author: Sébastien Le Marchand (@slemarchand)
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

try {

	map.each{ oldId, newId ->
		s = JournalStructureLocalServiceUtil.getStructure(groupId, oldId, includeGlobalStructures)
		s.setStructureId(newId)
		JournalStructureLocalServiceUtil.updateJournalStructure(s)
		
		q = DynamicQueryFactoryUtil.forClass(JournalTemplate.class)
		q.add(PropertyFactoryUtil.forName('structureId').eq(oldId))
		templates = JournalTemplateLocalServiceUtil.dynamicQuery(q)
		templates.each{ t ->
			t.setStructureId(newId)
			JournalTemplateLocalServiceUtil.updateJournalTemplate(t)
		}
		
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
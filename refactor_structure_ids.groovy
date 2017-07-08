/**
 * refactor_structure_ids.groovy
 *
 * Refactor some structures IDs
 *
 * @author Sébastien Le Marchand (@slemarchand on github)
 * @author Orin Fink (@einsty on github) - 
 */

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

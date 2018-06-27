import com.liferay.portal.kernel.search.IndexWriterHelperUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;

PORTLET = binding.hasVariable("actionRequest");

def reindexAllSpellCheckIndexes = { ->

	CompanyLocalServiceUtil.getCompanies().each({ 
		long companyId = (it.getCompanyId()); 
		IndexWriterHelperUtil.indexQuerySuggestionDictionaries(companyId);
		IndexWriterHelperUtil.indexSpellCheckerDictionaries(companyId);
	 });
}

if(PORTLET) {
	try {
		reindexAllSpellCheckIndexes();
	} catch(e) {
		outprintln("""<div class="portlet-msg-error" >${e}</div>""")
		e.printStackTrace(out)
	}
} else {
	reindexAllSpellCheckIndexes();
}
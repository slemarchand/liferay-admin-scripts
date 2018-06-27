import com.liferay.portal.kernel.search.IndexWriterHelperUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import java.util.HashMap;
import java.util.Map;

PORTLET = binding.hasVariable("actionRequest");

def reindexAllSearchIndexes = { ->

	long userId = 0;

	if(PORTLET) {
	 	userId = PortalUtil.getUserId(actionRequest);
	}

	Map<String, Serializable> taskContextMap = new HashMap<>();

	long[] companyIds = CompanyLocalServiceUtil.getCompanies().collect({ c -> c.getCompanyId() });

	String className = "";

	IndexWriterHelperUtil.reindex(
		userId, "reindex",
		companyIds, className,
		taskContextMap);
}


if(PORTLET) {
	try {
		reindexAllSearchIndexes();
	} catch(e) {
		outprintln("""<div class="portlet-msg-error" >${e}</div>""")
		e.printStackTrace(out)
	}
} else {
	reindexAllSearchIndexes();
}
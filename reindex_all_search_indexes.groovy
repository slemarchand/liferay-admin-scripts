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
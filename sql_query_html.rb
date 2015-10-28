#
# Copyright (c) 2012 Sébastien Le Marchand, All rights reserved.
#

# This library is free software; you can redistribute it and/or modify it under
# the terms of the GNU Lesser General Public License as published by the Free
# Software Foundation; either version 2.1 of the License, or (at your option)
# any later version.
#
# This library is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
# details.
#

#
# sql_query_csv.rb
#
# Execute a SQL Query on the Liferay Database and display results in HTML format
# Modified by Nicolas Grué (2014) : support of CLOB column types and HTML escaping.
#

# Constants (to update at your convenience) 

QUERY = "
	SELECT * 
	FROM PORTLETPREFERENCES " 	# The SQL query to execute
MAX_ROWS = 100 		# The max rows to display	

# Implementation

java_import java.io.PrintStream
java_import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream
java_import com.liferay.portal.kernel.dao.jdbc.DataAccess
java_import com.liferay.portal.kernel.util.HtmlUtil

if $out.class == UnsyncByteArrayOutputStream # Fix for Liferay version < 6.0.11
	$out = PrintStream.new($out)
end
	
def log(message)
	puts message
	$out.println(message)
end

rs = nil
stmt = nil
con = DataAccess.getConnection()

begin
	stmt = con.createStatement()
	stmt.setMaxRows(MAX_ROWS)
	rs = stmt.executeQuery(QUERY)
	md = rs.getMetaData()
	cc = md.getColumnCount() 
	
	header = <<-eos
		</pre>
		<div class="lfr-search-container">
		<div class="results-grid">
		<table class="taglib-search-iterator">
		<thead>
		<tr class="portlet-section-header results-header">
	eos
	for column in 1..cc
		value = md.getColumnLabel(column)	
		header = header + "<th>" + value.to_s + "</th>"	
	end
	header = header + <<-eos
		</tr>
		</thead>
		<tbody>
	eos
	log(header)

	alt = false
	while rs.next() do
		line = "<tr class=\"portlet-section-alternate results-row " + (alt ? "alt" : "") + "\">" 
		for column in 1..cc
			value =  rs.getObject(column)
			typeCol = md.getColumnTypeName(column)
			if (typeCol == "CLOB")
				clob = rs.getClob(column)
				line = line + "<td>" + HtmlUtil.escape(clob.getSubString(1, clob.length())) + "</td>"
			else
				line = line + "<td>" + HtmlUtil.escape(value.to_s) + "</td>"
			end
		end	
		log(line)
		alt = !alt
	end
	
	log(<<-eos
		</tbody>
		</table>
		</div>
		</div>
		</pre>
	eos
	)
			
ensure
	DataAccess.cleanUp(con, stmt, rs)
end

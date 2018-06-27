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

//
// sql_query.groovy
//
// Execute a SQL Query on the Liferay Database and display results in ASCII format.
//
// Tested under the following Liferay versions: 7.0.
//
// Getting other scripts: https://github.com/slemarchand/liferay-admin-scripts

//
// -- Constants (to update at your convenience) 
//

// The SQL query to execute

QUERY = """
	SELECT * 
	FROM User_
""" 	

// The max rows to display

MAX_ROWS = 100 

// The display format: can be "HTML", "ASCII" or "CSV"

FORMAT = "HTML"

// The min width for a cell display (used only for ASCII format)

CELL_MIN_WIDTH = 8

//
// -- Implementation
//

import java.io.PrintStream
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.HtmlUtil

def toCSV = { table ->
	
	result = ""

	header = "#"
	table[0].each {
		header +=  "," + it.toString()
	}
	result += header + "\n"

	1.upto(table.size() - 1) {
		line = "${it}"
		table[it].each {
			line += "," + it.toString()
		}
		result += line + "\n"
	}

	return result
}

def toASCII = { table ->
	
	result = ""

	header = "|"
	table[0].each {
		header += it.toString().padRight(CELL_MIN_WIDTH) + "|"
	}
	result += header + "\n"
	result += "".padLeft(header.length(),'-') + "\n"

	1.upto(table.size() - 1) {
		line = "|"
		table[it].each {
			line += it.toString().padRight(CELL_MIN_WIDTH) + "|"
		}
		result += line + "\n"
	}

	return result
}

def toHTML = { table ->
	
	result = ""

	header = """
		</pre>
		<table class="table table-autofit table-heading-nowrap table-list">
		<thead>
		<tr class="portlet-section-header results-header">
	"""
	table[0].each {
		header += "<th>${it.toString().padRight(CELL_MIN_WIDTH)}</th>"	
	}
	result += header + """
		</tr>
		</thead>
		<tbody>
	"""

	alt = false
	1.upto(table.size() - 1) {
		line = "<tr class=\"entry-display-style" + (alt ? "alt" : "") + "\">" 
		table[it].each {
			line += "<td>" + HtmlUtil.escape(it.toString()) + "</td>"
		}
		result += line + "</tr>\n"
		alt = !alt
	}

	result += """
		</tbody>
		</table>
		</pre>
	"""
	return result
}
def toString = [
	"ASCII": toASCII,
	"HTML": toHTML,
	"CSV": toCSV
	].get(FORMAT)

rs = null;
stmt = null;
con = DataAccess.getConnection();

table = []

try {

	stmt = con.createStatement()
	stmt.setMaxRows(MAX_ROWS)
	rs = stmt.executeQuery(QUERY)
	md = rs.getMetaData()
	cc = md.getColumnCount()
	
	header = []
	1.upto(cc) {
		value = md.getColumnLabel(it)	
		header.add(value)
	}

	table.add(header)

	while(rs.next()) {
		line = []
		1.upto(cc) {
			if(md.getColumnTypeName(it).equals("CLOB")) {
				clob = rs.getClob(it);
				value = clob.getSubString(1, clob.length())
			} else {
				value =  rs.getObject(it)
			}
			line.add(value)
		}
		table.add(line)
	}

	println(toString(table))

} catch(e) {
	e.printStackTrace(out);
} finally {
	DataAccess.cleanUp(con, stmt, rs);
}



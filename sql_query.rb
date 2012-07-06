#
# Copyright (c) 2011-2012 Sébastien Le Marchand, All rights reserved.
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
# sql_query.rb
#
# Execute a SQL Query on the Liferay Database and display results in ASCII format
#

# Constants (to update at your convenience) 

QUERY = "
	SELECT * 
	FROM USER_" 	# The SQL query to execute
MAX_ROWS = 100 		# The max rows to display	
CELL_MIN_WIDTH = 8	# The min width for a cell display

# Implementation

java_import java.io.PrintStream
java_import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream
java_import com.liferay.portal.kernel.dao.jdbc.DataAccess

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
	
	header = "|"
	for column in 1..cc
		value = md.getColumnLabel(column)	
		header = header + value.to_s.ljust(CELL_MIN_WIDTH) + "|"	
	end
	log(header)
	log("-" * header.length)

	while rs.next() do
		line = "|"
		for column in 1..cc
			value =  rs.getObject(column)
			line = line + value.to_s.ljust(CELL_MIN_WIDTH) + "|"
		end	
		log(line)
	end 
ensure
	DataAccess.cleanUp(con, stmt, rs)
end

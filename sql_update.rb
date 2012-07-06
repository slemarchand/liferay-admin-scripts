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
# sql_update.rb
#
# Execute a SQL update on the Liferay Database and display affected row count.
#

# Constants (to update at your convenience) 

UPDATE = "
	UPDATE User_ 
	SET firstName = 'Alan', lastName = 'Turing' 
	WHERE screenName = 'test'
" #  SQL update to execute (this can be an INSERT, UPDATE or DELETE statement)	

# Implementation

java_import java.io.PrintStream
java_import com.liferay.portal.kernel.dao.jdbc.DataAccess

if $out.class == UnsyncByteArrayOutputStream # Fix for Liferay version < 6.0.11
	$out = PrintStream.new($out)
end

def log(message)
	puts message
	$out.println(message)
end

stmt = nil
con = DataAccess.getConnection()

begin
	con.setAutoCommit(true)
	stmt = con.createStatement()
	count = stmt.executeUpdate(UPDATE)
	log(count.to_s() << " row(s) affected") 
ensure
	DataAccess.cleanUp(con, stmt)
end


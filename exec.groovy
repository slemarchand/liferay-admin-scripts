/**
 * Copyright (c) 2011 Sébastien Le Marchand, All rights reserved.
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
// exec.groovy
//
// Execute a shell command
//

// Constants (to update at your convenience) 

final def CMD = 'df -h -a -i -T' 	// The shell command to execute

// Implementation

def exec = { cmd -> def p = cmd.execute(); out.leftShift(new SequenceInputStream(p.getErr(), p.getIn())) }
exec(CMD)
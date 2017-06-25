import com.liferay.portal.scripting.executor.groovy.*

USERS = [
	['user01@slemarchand.com', 'firstName1', 'lastName1'],
	['user02@slemarchand.com', 'firstName2', 'lastName2'],
	['user03@slemarchand.com', 'firstName3', 'lastName3']
]

PASSWORD = 'test'

String[] ROLES = [
	'Power User',
	'Administrator'
]

ctx = new GroovyScriptingContext()

USERS.each{ 
	(email, firstName, lastName) = it
	user = new GroovyUser(email, PASSWORD, firstName, lastName, '')
	user.create(ctx)
	user.addRoles(ctx, ROLES)
}


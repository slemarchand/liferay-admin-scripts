import com.liferay.portal.scripting.executor.groovy.*
import com.liferay.portal.kernel.service.*

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

SITES_ROLES = [
	'Guest':['Site Administrator']

]

try {
	ctx = new GroovyScriptingContext()

	USERS.each{ 
		(email, firstName, lastName) = it
		
		user = new GroovyUser(email, PASSWORD, firstName, lastName, '')
		user.create(ctx)
		user.addRoles(ctx, ROLES)

		SITES_ROLES.each{ siteName, siteRoles ->

			group = GroupLocalServiceUtil.search(ctx.companyId, siteName, null, [:], true, 0, 1).get(0)

			user.addSiteRoles(ctx, group.getGroupId(), (String[])siteRoles)
		}
	}
} catch (e) {
	e.printStackTrace(out)
}
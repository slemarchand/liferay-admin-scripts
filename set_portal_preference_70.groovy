import com.liferay.portal.kernel.service.*
import com.liferay.portal.kernel.util.*

PREF_KEY = 'com.mycompany.mypref'
PREF_VALUE = 'myval'

ownerId = PortalUtil.getDefaultCompanyId()
ownerType = PortletKeys.PREFS_OWNER_TYPE_COMPANY
prefs =  PortalPreferencesLocalServiceUtil.getPreferences(ownerId, ownerType)

value = prefs.getValue(PREF_KEY, null)
out.println('old value: ' + value)

prefs.setValue(PREF_KEY, PREF_VALUE)
prefs.store()

value = prefs.getValue(PREF_KEY, null)
out.println('new value: ' + value)
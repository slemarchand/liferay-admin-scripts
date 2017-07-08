/**
 *
 * @author Paweł Kruszewski (@ktor on github)
 */
import com.liferay.portal.kernel.util.HtmlUtil

try {
   file = new File(System.getProperty('liferay.home'),'/jboss-eap-6.3/standalone/configuration/standalone.xml')
   content = com.liferay.portal.kernel.util.FileUtil.read(file)
   out.println(HtmlUtil.escape(content))
} catch(Exception e) {
  e.printStackTrace(out)
}

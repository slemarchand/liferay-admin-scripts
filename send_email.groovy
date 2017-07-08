/**
 *
 * @author Paweł Kruszewski (@ktor on github)
 */
import com.liferay.util.mail.*

String from = ""
String to = ""
String subject="This is email title"
String body="Hello World, this is my test email"

try {
  out.println("Before Mail Engine")
  MailEngine.send(from, to, subject, body)
  out.println("After Mail Engine")
} catch(Exception e) {
  e.printStackTrace(out)
}


import com.liferay.portal.kernel.service.*
import com.liferay.portal.kernel.model.*
import com.liferay.portal.kernel.portlet.*
import com.liferay.portal.kernel.util.*

import java.util.HashMap
import java.util.Map
import java.util.Set
import java.io.File
import java.io.StringWriter

EXCLUDE_PREFIXES = [
    'hibernate.',
    'image.default.company.logo',
    'image.hook.file.system.root.dir',
    'include-and-override',
    'auto.deploy.deploy.dir',
    'auto.deploy.tomcat.dest.dir',
    'auto.deploy.websphere.wsadmin.app.manager.update.options',
    'dl.char.blacklist',
    'module.framework.',
    'source.forge.mirrors',
    'spring.infrastructure.configs',
    'jsonws.web.service.parameter.type.whitelist.class.names',
    'javax.persistence.validation.mode',
    'message.boards.user.ranks',
    'liferay.home',
    'setup.database.url[hypersonic]',
    'browser.launcher.url',
    'resource.repositories.root'
];

try {

    Properties defaultProps = new Properties();
    defaultProps.load(com.liferay.portal.util.PropsValues.class.getResourceAsStream("/portal.properties"));

    Properties props = PropsUtil.getProperties();

    Properties overridenProps = new Properties();

    props.keySet().each { key ->
        value = props.get(key);
        defaultValue = defaultProps.get(key);
        if(!key.isEmpty() && !value.equals(defaultValue)) {

            excluded = false

            EXCLUDE_PREFIXES.each { prefix ->
                if(key.startsWith(prefix)) {
                    excluded = true
                }
            }

            if(excluded) {
                return;
            }

            overridenProps.put(key, value);
        }
    }

    overridenKeys = new Sequence(String.class);

    overridenKeys.addAll(overridenProps.keySet());

    overridenKeys.sort();

    overridenKeys.each { key ->
        value = overridenProps.get(key);
        out.println("${key}=${value}");
    }

} catch(e) {
    out.println("""<div class="portlet-msg-error" >${e}</div>""")
    e.printStackTrace(out)
}

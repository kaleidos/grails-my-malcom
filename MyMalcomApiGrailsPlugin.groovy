
import net.kaleidos.mymalcom.PluginSpringBeans

class MyMalcomApiGrailsPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/domain/**",
            "grails-app/controllers/**",
            "grails-app/taglib/**",
            "grails-app/i18n/**",
            "web-app/**"
    ]

    def title = "Grails My Malcom Api Plugin"
    def author = "Iván López"
    def authorEmail = "ivan.lopez@kaleidos.net"
    def description = "This plugin provides a service to send push notifications to your mobile devices via MyMalcom (http://www.mymalcom.com) from your grails application."

    // URL to the plugin's documentation
    def documentation = "https://github.com/kaleidos/grails-my-malcom/blob/master/README.md"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "Kaleidos", url: "http://kaleidos.net/" ]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GITHUB", url: "https://github.com/kaleidos/grails-my-malcom/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/kaleidos/grails-my-malcom" ]

    def doWithSpring = PluginSpringBeans.doWithSpring
}

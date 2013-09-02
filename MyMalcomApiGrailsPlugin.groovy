
class MyMalcomApiGrailsPlugin {
    // the plugin version
    def version = "0.1"
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
    def description = '''\
This plugin provides a service to send push notifications to your mobile devices via MyMalcom (http://www.mymalcom.com) from your grails application.
'''

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

    def doWithSpring = {
        def myMalcomApiConfig = application.config.myMalcom

        if (!myMalcomApiConfig.applicationCode) {
            log.error "ERROR: MyMalcom API applicationCode not found. The property myMalcom.applicationCode must be defined in Config.groovy"
        }
        if (!myMalcomApiConfig.environment || !["SANDBOX", "PRODUCTION"].contains(myMalcomApiConfig.environment)) {
            log.error "ERROR: MyMalcom API environment not found or not valid. The property myMalcom.environment must be defined in Config.groovy and must be 'SANDBOX' or 'PRODUCTION'"
        }
        if (!myMalcomApiConfig.user) {
            log.error "ERROR: MyMalcom API user not found. The property myMalcom.user must be defined in Config.groovy"
        }
        if (!myMalcomApiConfig.password) {
            log.error "ERROR: MyMalcom API password not found. The property myMalcom.password must be defined in Config.groovy"
        }

        connectionClient(net.kaleidos.mymalcom.ConnectionClient) {
            grailsApplication = ref("grailsApplication")
        }
    }
}

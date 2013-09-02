
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

    // TODO Fill in these fields
    def title = "Grails My Malcom Api Plugin" // Headline display name of the plugin
    def author = "Your name"
    def authorEmail = ""
    def description = '''\
Brief summary/description of the plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/my-malcom-api"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

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

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}

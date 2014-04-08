package net.kaleidos.mymalcom

class PluginSpringBeans {
    static doWithSpring = {
        def myMalcomApiConfig = application.config.myMalcom

        if (!myMalcomApiConfig.applicationCode) {
            log.warn "MyMalcom API applicationCode not found. The property myMalcom.applicationCode must be defined in Config.groovy"
        }
        if (!myMalcomApiConfig.environment || !["SANDBOX", "PRODUCTION"].contains(myMalcomApiConfig.environment)) {
            log.warn "MyMalcom API environment not found or not valid. The property myMalcom.environment must be defined in Config.groovy and must be 'SANDBOX' or 'PRODUCTION'"
        }
        if (!myMalcomApiConfig.user) {
            log.warn "MyMalcom API user not found. The property myMalcom.user must be defined in Config.groovy"
        }
        if (!myMalcomApiConfig.password) {
            log.warn "MyMalcom API password not found. The property myMalcom.password must be defined in Config.groovy"
        }

        def clients = myMalcomApiConfig.clients
        if (clients == null || clients.size() == 0) {
            myMalcomConnectionClient(net.kaleidos.mymalcom.ConnectionClient) {
                grailsApplication = ref("grailsApplication")
            }
        } else {
            clients?.each { key, val->
                "${key}MyMalcomConnectionClient"(net.kaleidos.mymalcom.ConnectionClient) {
                    grailsApplication = ref("grailsApplication")
                    user = val['user']
                    password = val['password']
                    clientId = key
                }
            }
        }
    }
}

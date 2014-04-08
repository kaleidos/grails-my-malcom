package net.kaleidos.mymalcom

import groovy.json.JsonBuilder
import org.springframework.beans.factory.InitializingBean

class MyMalcomPushService implements InitializingBean {

    static transactional = false

    def grailsApplication
    def connectionClientMap

    void afterPropertiesSet() {
        def myMalcomConfig = grailsApplication.config.myMalcom
        def clients = myMalcomConfig.clients

        connectionClientMap = [:]

        if (grailsApplication.mainContext.containsBean("myMalcomConnectionClient")) {
            connectionClientMap['default'] = grailsApplication.mainContext.getBean("myMalcomConnectionClient")
        }

        clients.each { key, value->
            def beanName  = "${key}MyMalcomConnectionClient"
            if (grailsApplication.mainContext.containsBean(beanName)) {
                connectionClientMap[key] = grailsApplication.mainContext.getBean(beanName)
            }
        }
        log.debug "Connection clients found: ${connectionClientMap}"
    }

    /**
     * Send a push notification
     *
     * @param userDevices A list of the devices to send the notification
     * @param message The message to send
     * @param customFields Optional fields to send additional information to the mobile application
     *
     * Example of the json sent:
     *
     * {
     *     "notification": {
     *         "applicationCode": "APP-UUID",
     *         "environment": "SANDBOX",
     *         "udids": [
     *             "ID-MOBILE-1",
     *             "ID-MOBILE2"
     *         ],
     *         "message": "The text of the notification",
     *         "notificationCustomization": {
     *             "customFields": {
     *                 "entry": [
     *                     {
     *                         "key": "The-key1",
     *                         "value": "The-value1"
     *                     },
     *                     {
     *                         "key": "The-key2",
     *                         "value": "The-value2"
     *                     }
     *                 ]
     *             }
     *         }
     *     }
     * }
     *
     * @return true if done, false otherwise
     */
    public Boolean sendPushNotification(List<String> userDevices, String message, Map customFields = [:]) {
        return sendPushNotification("default", userDevices, message, customFields)
    }

    /**
     * Send a push notification
     *
     * @param client Client to use for the remote method call
     * @param userDevices A list of the devices to send the notification
     * @param message The message to send
     * @param customFields Optional fields to send additional information to the mobile application
     *
     * Example of the json sent:
     *
     * {
     *     "notification": {
     *         "applicationCode": "APP-UUID",
     *         "environment": "SANDBOX",
     *         "udids": [
     *             "ID-MOBILE-1",
     *             "ID-MOBILE2"
     *         ],
     *         "message": "The text of the notification",
     *         "notificationCustomization": {
     *             "customFields": {
     *                 "entry": [
     *                     {
     *                         "key": "The-key1",
     *                         "value": "The-value1"
     *                     },
     *                     {
     *                         "key": "The-key2",
     *                         "value": "The-value2"
     *                     }
     *                 ]
     *             }
     *         }
     *     }
     * }
     *
     * @return true if done, false otherwise
     */
    public Boolean sendPushNotification(String client, List<String> userDevices, String message, Map customFields = [:]) {
        def myMalcomConfig = grailsApplication.config.myMalcom

        def additionalInfo = []
        customFields.each {
            def map = [:]
            map.key = it.key
            map.value = it.value

            additionalInfo << map
        }

        if (connectionClientMap == null || !connectionClientMap[client]) {
            return false
        }

        def confApplicationCode = myMalcomConfig.clients?."${client}"?.applicationCode
        if (!confApplicationCode) {
            confApplicationCode = myMalcomConfig.applicationCode
        }

        def confEnvironment = myMalcomConfig.clients?."${client}"?.environment
        if (!confEnvironment) {
            confEnvironment = myMalcomConfig.environment
        }

        def builder = new JsonBuilder()
        builder {
            notification {
                applicationCode(confApplicationCode)
                environment(confEnvironment)
                udids(userDevices)
                delegate.message(message)

                if (additionalInfo) {
                    notificationCustomization {
                        delegate.customFields {
                            entry(additionalInfo)
                        }
                    }
                }
            }
        }

        return connectionClientMap[client].sendJson(builder.toString())
    }
}

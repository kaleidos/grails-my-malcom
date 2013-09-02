package net.kaleidos.mymalcom

import groovy.json.JsonBuilder

class MyMalcomPushService {

    static transaction = false

    def connectionClient
    def grailsApplication

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

        def myMalcomConfig = grailsApplication.config.myMalcom

        def additionalInfo = []
        customFields.each {
            def map = [:]
            map.key = it.key
            map.value = it.value

            additionalInfo << map
        }

        def builder = new JsonBuilder()
        builder {
            notification {
                applicationCode(myMalcomConfig.applicationCode)
                environment(myMalcomConfig.environment)
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

        return connectionClient.sendJson(builder.toString())
    }
}

package net.kaleidos.mymalcom

import grails.plugin.spock.*
import spock.lang.*

class MyMalcomPushServiceSpec extends Specification {

    def myMalcomPushService

    void setup() {
        myMalcomPushService = new MyMalcomPushService()
        myMalcomPushService.grailsApplication = [
            config: [
                "myMalcom" : [
                    applicationCode:"APP-UUID",
                    environment: "SANDBOX"
                ]
            ]
        ]
    }

    void 'push a simple notification to one device'() {
        setup: 'mock colaborator'
            def connectionClient = Mock(ConnectionClient)
            myMalcomPushService.connectionClientMap = ['default':connectionClient]

        when:
            myMalcomPushService.sendPushNotification(userDevices, message)

        then:
            1 * connectionClient.sendJson(json)

        where:
            message = "The text of the notification"
            userDevices = ["ID-MOBILE-1"]
            json = """{"notification":{"applicationCode":"APP-UUID","environment":"SANDBOX","udids":["${userDevices[0]}"],"message":"${message}"}}"""
    }

    void 'push a simple notification to more devices'() {
        setup: 'mock colaborator'
            def connectionClient = Mock(ConnectionClient)
            myMalcomPushService.connectionClientMap = ['default':connectionClient]

        when:
            myMalcomPushService.sendPushNotification(userDevices, message)

        then:
            1 * connectionClient.sendJson(json)

        where:
            message = "The text of the notification"
            userDevices = ["ID-MOBILE-1", "ID-MOBILE2", "ID-MOBILE3"]
            json = """{"notification":{"applicationCode":"APP-UUID","environment":"SANDBOX","udids":["${userDevices[0]}","${userDevices[1]}","${userDevices[2]}"],"message":"${message}"}}"""
    }

    void 'push a notification with additional custom fields'() {
        setup: 'mock colaborator'
            def connectionClient = Mock(ConnectionClient)
            myMalcomPushService.connectionClientMap = ['default':connectionClient]

        when:
            myMalcomPushService.sendPushNotification(userDevices, message, customFields)

        then:
            1 * connectionClient.sendJson(json)

        where:
            message = "The text of the notification"
            userDevices = ["ID-MOBILE-1", "ID-MOBILE2"]
            customFields = ["The-key1":"The-value1", "The-key2":"The-value2"]
            json = """{"notification":{"applicationCode":"APP-UUID","environment":"SANDBOX","udids":["${userDevices[0]}","${userDevices[1]}"],"message":"${message}","notificationCustomization":{"customFields":{"entry":[{"key":"The-key1","value":"The-value1"},{"key":"The-key2","value":"The-value2"}]}}}}"""
    }

    void 'Multi-client notification test'() {
        setup: "MyMalcom configuration for multiple clients"
            myMalcomPushService.grailsApplication = [
                config: [
                    "myMalcom" : [
                        clients: [
                            android: [
                                applicationCode : "APP-android",
                                environment : "ENV-android",
                                user : "USER-android",
                                password : "PASS-android"
                            ],
                            ios: [
                                applicationCode : "APP-ios",
                                environment : "ENV-ios",
                                user : "USER-ios",
                                password : "PASS-ios"
                            ],
                            otherClient: [
                                applicationCode : "APP-otherClient",
                                environment : "ENV-otherClient",
                                user : "USER-otherClient",
                                password : "PASS-otherClient"
                            ]
                        ]
                    ]
                ]
            ]

        and: 'Mock connection client'
            myMalcomPushService.connectionClientMap = [
                'android': Mock(ConnectionClient),
                'ios': Mock(ConnectionClient),
                'otherClient': Mock(ConnectionClient)
            ]

        when:
            myMalcomPushService.sendPushNotification(currentClient, userDevices, message, customFields)

        then:
            1 * myMalcomPushService.connectionClientMap[currentClient].sendJson(json)

        where:
            message = "The text of the notification"
            currentClient << ['android', 'ios', 'otherClient']
            userDevices = ["ID-MOBILE-1", "ID-MOBILE2"]
            customFields = ["The-key1":"The-value1", "The-key2":"The-value2"]
            json = """{"notification":{"applicationCode":"APP-${currentClient}","environment":"ENV-${currentClient}","udids":["${userDevices[0]}","${userDevices[1]}"],"message":"${message}","notificationCustomization":{"customFields":{"entry":[{"key":"The-key1","value":"The-value1"},{"key":"The-key2","value":"The-value2"}]}}}}"""
    }

    void 'There is no multiclient configuration but a client is called'() {
        setup:
            myMalcomPushService.connectionClientMap = [:]

        when:
            def result = myMalcomPushService.sendPushNotification(currentClient, userDevices, message, customFields)

        then:
            result == false
            0 * _.sendJson(_)

        where:
            message = "The text of the notification"
            currentClient << ['android', 'ios', 'otherClient']
            userDevices = ["ID-MOBILE-1", "ID-MOBILE2"]
            customFields = ["The-key1":"The-value1", "The-key2":"The-value2"]
            json = """{"notification":{"applicationCode":"APP-${currentClient}","environment":"ENV-${currentClient}","udids":["${userDevices[0]}","${userDevices[1]}"],"message":"${message}","notificationCustomization":{"customFields":{"entry":[{"key":"The-key1","value":"The-value1"},{"key":"The-key2","value":"The-value2"}]}}}}"""
    }

    void 'There are only some configuration configured. Should use the defaults'() {
        setup: "MyMalcom configuration for multiple clients"
            myMalcomPushService.grailsApplication = [
                config: [
                    "myMalcom" : [
                        environment : "APP-ENV",
                        clients: [
                            android: [
                                applicationCode : "APP-android",
                            ],
                            ios: [
                                applicationCode : "APP-ios",
                            ],
                            otherClient: [
                                applicationCode : "APP-otherClient",
                            ]
                        ]
                    ]
                ]
            ]

        and: 'Mock connection client'
            myMalcomPushService.connectionClientMap = [
                'android': Mock(ConnectionClient),
                'ios': Mock(ConnectionClient),
                'otherClient': Mock(ConnectionClient)
            ]

        when:
            myMalcomPushService.sendPushNotification(currentClient, userDevices, message, customFields)

        then:
            1 * myMalcomPushService.connectionClientMap[currentClient].sendJson(json)

        where:
            message = "The text of the notification"
            currentClient << ['android', 'ios', 'otherClient']
            userDevices = ["ID-MOBILE-1", "ID-MOBILE2"]
            customFields = ["The-key1":"The-value1", "The-key2":"The-value2"]
            json = """{"notification":{"applicationCode":"APP-${currentClient}","environment":"APP-ENV","udids":["${userDevices[0]}","${userDevices[1]}"],"message":"${message}","notificationCustomization":{"customFields":{"entry":[{"key":"The-key1","value":"The-value1"},{"key":"The-key2","value":"The-value2"}]}}}}"""
    }
}


package net.kaleidos.mymalcom

import grails.plugin.spock.*
import spock.lang.*

class MyMalcomPushServiceSpec extends Specification {

    def myMalcomPushService

    void setup() {
        myMalcomPushService = new MyMalcomPushService()
        myMalcomPushService.grailsApplication = [config: ["myMalcom" : [applicationCode:"APP-UUID", environment: "SANDBOX"]]]
    }

    void 'push a simple notification to one device'() {
        setup: 'mock colaborator'
            def connectionClient = Mock(ConnectionClient)
            myMalcomPushService.connectionClient = connectionClient

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
            myMalcomPushService.connectionClient = connectionClient

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
            myMalcomPushService.connectionClient = connectionClient

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
}


package net.kaleidos.mymalcom

import grails.plugin.spock.*
import spock.lang.*

import grails.spring.BeanBuilder

class PluginSpringBeansSpec extends Specification {
    void 'Test spring beans created by the plugin'(){
        setup: "Bean Builder"
            def builder = new BeanBuilder()

            builder.metaClass.setProperty("application",[
                    config: [
                        "myMalcom" : [
                            applicationCode:"APP-UUID",
                            environment: "SANDBOX"
                        ]
                    ]
                ]
            )

            builder.metaClass.ref = { String ref->
                new org.codehaus.groovy.grails.commons.DefaultGrailsApplication()
            }

        when:
            builder.beans(PluginSpringBeans.doWithSpring)
            def context = builder.createApplicationContext()
            def result = context.getBeansOfType(net.kaleidos.mymalcom.ConnectionClient)

        then:
            result.size() == 1
    }
    void 'Test spring beans created by the plugin'(){
        setup: "Bean Builder"
            def builder = new BeanBuilder()

            builder.metaClass.setProperty("application",[
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
            )

            builder.metaClass.ref = { String ref->
                if (ref == "grailsApplication") {
                    return new org.codehaus.groovy.grails.commons.DefaultGrailsApplication()
                }
                return null
            }

        when:
            builder.beans(PluginSpringBeans.doWithSpring)
            def context = builder.createApplicationContext()
            def result = context.getBeansOfType(net.kaleidos.mymalcom.ConnectionClient)

        then:
            result.size() == 3
    }
}


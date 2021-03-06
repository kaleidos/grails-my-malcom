Grails MyMalcom API
===================

[![Build Status](https://drone.io/github.com/kaleidos/grails-my-malcom/status.png)](https://drone.io/github.com/kaleidos/grails-my-malcom/latest)
[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/kaleidos/grails-my-malcom/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

This plugin provides a service to send push notifications to your mobile devices via [MyMalcom](http://www.mymalcom.com/) from your grails application.

Please note that only basic text notification and additional param options are implemented.

## Configuration

You have to include the following mandatory properties in your Config.groovy file. Note that the `apiUrl` parameter is optional.

```groovy
myMalcom {
    apiUrl = "https://api.mymalcom.com/v3/notification/push"
    applicationCode = "App UUID identifier in Malcom"
    environment = "SANDBOX" // or "PRODUCTION"
    user = "YOUR-USER-API-KEY"
    password = "YOUR-PASSWORD"
}
```

If you have several posible application codes you can use the alternative configuration:

```groovy
myMalcom {
    apiUrl = "https://api.mymalcom.com/v3/notification/push"

    clients {
        android {
            applicationCode = "App UUID Android identifier in Malcom"
            environment = "SANDBOX" // or "PRODUCTION"
            user = "YOUR-USER-ANDROID-API-KEY"
            password = "YOUR-ANDROID-PASSWORD"
        }
        ios {
            applicationCode = "App UUID iOS identifier in Malcom"
            environment = "SANDBOX" // or "PRODUCTION"
            user = "YOUR-USER-IOS-API-KEY"
            password = "YOUR-IOS-PASSWORD"
        }
        otherclient {
            applicationCode = "App UUID Other Client identifier in Malcom"
            environment = "SANDBOX" // or "PRODUCTION"
            user = "YOUR-USER-OTHER-API-KEY"
            password = "YOUR-OTHER-PASSWORD"
        }
    }
}
```

## Send push notification

The plugin provides a service to send the push notification to MyMalcom. All you have to do is to inject the `myMalcomPushService` into your grails artefacts.

```groovy
class MyService {
    def myMalcomPushService

    def sendPushNotification() {
        def userDevices = ["MY_ANDROID_DEVICE_ID", "MY_IOS_DEVICE_ID"]
        def message = "The message to send to the device"

        myMalcomPushService.sendPushNotification(userDevices, message)
    }
}
```

If multiple clients has been configured you can select one of them as a method argument:

```groovy
class MyService {
    def myMalcomPushService

    def sendPushNotification() {
        def userDevices = ["MY_ANDROID_DEVICE_ID", "MY_IOS_DEVICE_ID"]
        def message = "The message to send to the device"

        myMalcomPushService.sendPushNotification("ios", userDevices, message)
    }
}
```

The `sendPushNotification` method also supports and optional `Map` param with additional information to send to the application. This information can be used, for example, when you have a new follower to send the id of the follower and when you click on the notification on your mobile device, the application can open the profile of your new follower. This additional information is send in the `customfield` API field.

```groovy
class MyService {
    def myMalcomPushService

    def sendPushNotificationWhenNewFollower(User user) {
        def userDevices = ["MY_ANDROID_DEVICE_ID"]
        def message = "You have a new follower"

        myMalcomPushService.sendPushNotification(userDevices, message, [notificationType:'NEW_FOLLOWER', userId:user.id])
    }
}
```

## Additional information

The full documentation of the MyMalcom API is available at [https://github.com/MyMalcom/MalcomAPI](https://github.com/MyMalcom/MalcomAPI). Please, check it out for additional information.


## Authors

You can send any questions to:

- Iván López: lopez.ivan@gmail.com ([@ilopmar](https://twitter.com/ilopmar))

Collaborations are appreciated :-)


## Release Notes

* 0.2 - 08/Apr/2014 - Added multi-client capabilities
* 0.1.1 - 03/Sep/2013 - Fix wrong Config.groovy
* 0.1 - 02/Sep/2013 - Initial version of the plugin with basic support of push notifications.

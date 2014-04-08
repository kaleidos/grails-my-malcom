package net.kaleidos.mymalcom

import wslite.http.auth.HTTPBasicAuthorization
import wslite.rest.ContentType
import wslite.rest.RESTClient
import org.springframework.beans.factory.InitializingBean

@groovy.util.logging.Log4j
class ConnectionClient implements InitializingBean {

    def grailsApplication

    private static final String API_URL = "https://api.mymalcom.com/v3/notification/push"

    String url
    String user
    String password
    String clientId

    private RESTClient client

    void afterPropertiesSet() {
        def myMalcomApiConfig = grailsApplication.config.myMalcom

        if (!this.url) {
            this.url = myMalcomApiConfig.apiUrl ?: API_URL
        }
        if (!this.user) {
            this.user = myMalcomApiConfig.user ?: ""
        }
        if (!this.password) {
            this.password = myMalcomApiConfig.password ?: ""
        }

        client = new RESTClient(this.url)
        client.authorization = new HTTPBasicAuthorization(this.user, this.password)

        log.debug "ConnectionClient succesfully created: ${clientId}"
    }

    public boolean sendJson(String json) {
        try {
            client.post() {
                type ContentType.JSON
                text json
            }
        } catch (Exception e) {
            log.error "There was an error with MyMalcom request"
            log.error e
            return false
        }
        return true

    }
}

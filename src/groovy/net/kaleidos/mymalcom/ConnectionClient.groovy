package net.kaleidos.mymalcom

import wslite.http.auth.HTTPBasicAuthorization
import wslite.rest.ContentType
import wslite.rest.RESTClient

class ConnectionClient {

    def grailsApplication

    private static final String API_URL = "https://api.mymalcom.com/v3/notification/push"
    private RESTClient client

    /**
     * Create the client with the url, user and password
     *
     * @return the client to use MyMalcom API
     */
    public RESTClient getMyMalcomClient() {
        if (client == null) {
            def myMalcomApiConfig = grailsApplication.config.myMalcom

            def url = myMalcomApiConfig.apiUrl ?: API_URL
            def user = myMalcomApiConfig.user ?: ""
            def password = myMalcomApiConfig.password ?: ""

            client = new RESTClient(url)
            client.authorization = new HTTPBasicAuthorization(user, password)
        }

        return client
    }

    public boolean sendJson(String json) {
        RESTClient client = this.getMyMalcomClient()

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

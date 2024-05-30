package com.example.mediaserver.config

import org.fluentd.logger.FluentLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FluentdClient(
    @Value("\${fluentd.tag}") val tag: String,
    @Value("\${fluentd.host}") val host: String,
    @Value("\${fluentd.port}") val port: Int
) {
    var logger: FluentLogger = FluentLogger.getLogger(tag, host, port)
    fun log(tag: String, data: Map<String, Any>) {
        logger.log(tag, data)
    }

    fun log(tag: String, message: String) {
        logger.log(tag, mutableMapOf(Pair<String, Any>("message", message)))
    }
}

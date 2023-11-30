package it.macgood.core

import android.content.Context
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLProtocolException

fun <T> handleException(exception: Exception, context: Context): Resource<T> {
    return when (exception) {
        is HttpException -> Resource.Error<T>(
            exception.code().toErrorCodeMessage(context)
        )

        else -> Resource.Error<T>(
            exception.toErrorMessage(context)
        )
    }
}

fun Int.toErrorCodeMessage(context: Context): String {
    return when (this) {
        400 -> context.getString(R.string.error_400_bad_request)
        401 -> context.getString(R.string.error_401_unauthorized)
        403 -> context.getString(R.string.error_403_forbidden)
        404 -> context.getString(R.string.error_404_not_found)
        500 -> context.getString(R.string.error_500_internal_server_error)
        501 -> context.getString(R.string.error_501_not_implemented)
        502 -> context.getString(R.string.error_502_bad_gateway)
        302 -> context.getString(R.string.error_302_redirect)
        else -> context.getString(R.string.error_number, this.toString())
    }
}

fun Throwable.toErrorMessage(context: Context): String {
    return when (this) {
        is SocketTimeoutException -> context.getString(R.string.socket_timeout_error)
        is ConnectException -> context.getString(R.string.failed_to_connect_to_the_server)
        is UnknownHostException -> context.getString(R.string.unknown_host)
        is SSLHandshakeException -> context.getString(R.string.ssl_handshake_failed)
        is SSLProtocolException -> context.getString(R.string.ssl_protocol_error)
        is IOException -> context.getString(R.string.check_your_internet_connection)
        else -> context.getString(R.string.unknown_error)
    }
}
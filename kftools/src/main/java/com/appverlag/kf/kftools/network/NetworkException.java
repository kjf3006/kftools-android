package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.UnknownHostException;

public class NetworkException extends Exception {

    public static Exception noDataReceived() { return new Exception("Es wurden keine Daten empfangen."); }
    public static Exception invalidDataReceived() { return new Exception("Es wurden ungültige Daten empfangen. Eine Verarbeitung ist nicht möglich."); }

    private static final String UNKNOWN_ERROR_MESSAGE = "Unbekannter Fehler";

    public NetworkException() {
        super(UNKNOWN_ERROR_MESSAGE);
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(Throwable cause) {
        super(messageForThrowable(cause));
    }

    public NetworkException(int statusCode) {
        super(localizedMessageForStatusCode(statusCode));
    }

    @NonNull
    @Override
    public String getLocalizedMessage() {
        String message = super.getLocalizedMessage();
        return message != null ? message : UNKNOWN_ERROR_MESSAGE;
    }

    private static String messageForThrowable(Throwable cause) {
        String message;
        if (cause instanceof UnknownHostException) {
            message = "Server nicht gefunden. Das Gerät ist eventuell nicht mit dem Internet verbunden.";
        }
        else {
            message = cause.getLocalizedMessage();
        }
        return message;
    }

    public static String localizedMessageForStatusCode(int statusCode) {
        String message = switch (statusCode) {
            case 400:
                yield "Ungültige Anfrage";
            case 401:
                yield "Die Anfrage kann nicht ohne gültige Authentifizierung durchgeführt werden.";
            case 403:
                yield "Die Anfrage wurde mangels Berechtigung nicht durchgeführt.";
            case 404:
                yield "Die angeforderte Ressource wurde nicht gefunden.";
            case 500:
                yield "Interner Server-Fehler";
            default:
                yield UNKNOWN_ERROR_MESSAGE;
        };
        return message + " (" + statusCode + ")";
    }
}

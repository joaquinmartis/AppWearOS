package com.android.wearable.cuandollegawearos.business;

/**
 * Excepcion para el caso que la Hora que provee la API para la ultima actualizacion del colectivo sea posterior a la hora actual
 *
 */
public class HoraAPIInvalidaException extends RuntimeException {
    public HoraAPIInvalidaException(String message) {
        super(message);
    }
}

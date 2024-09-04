package com.empirefx.fxbo_countries.commonlib.exceptions;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ProviderNotCachedException extends Exception {

    private static final long serialVersionUID = 5819281122008560712L;

    public ProviderNotCachedException(String message) {
        super(message);
    }

}

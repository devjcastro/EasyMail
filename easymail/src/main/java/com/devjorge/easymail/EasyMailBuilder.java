package com.devjorge.easymail;

/**
 * Created by jorge.castro on 24/06/2016.
 */
public abstract class EasyMailBuilder {

    protected Email email = new Email();

    public abstract void defaultConfiguration();
}

package com.ml.android.melitraining.common;

/**
 * Created by marbarfa on 5/1/14.
 */
public interface ICallbackHandler<T,R> {

    /**
     * Method to be used for callback methods.
     * @param input input to be used in the callback
     * @return value to be returned.
     */
    public R apply(T input);

}

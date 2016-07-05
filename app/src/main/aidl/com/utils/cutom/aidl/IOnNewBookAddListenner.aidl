// IOnNewBookAddListenner.aidl
package com.utils.cutom.aidl;

// Declare any non-default types here with import statements
import com.utils.cutom.aidl.Book;
interface IOnNewBookAddListenner {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrivedListenner(in Book book);
}

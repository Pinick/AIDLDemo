// IBookManager.aidl
package com.utils.cutom.aidl;

import com.utils.cutom.aidl.Book;
import com.utils.cutom.aidl.IOnNewBookAddListenner;
 interface IBookManager{
    List<Book> getBookList();
    void addBook(in Book book);
    void  rigisterListenner(IOnNewBookAddListenner listenner);
    void  unrigisterListenner(IOnNewBookAddListenner listenner);
}
package kr.ac.pusan.cs.bookforyou;

import java.io.Serializable;

public class Obj_book_info implements Serializable {

    Obj_book book;
    String state = "none";
    String userID;
    String description;
    Obj_imageURI imageURI;
    void init(){
        imageURI = new Obj_imageURI();
    }

}

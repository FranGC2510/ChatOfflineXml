module org.dam.fcojavier.chatofflinexml {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.xml.bind;
    requires jbcrypt;

    opens org.dam.fcojavier.chatofflinexml to javafx.fxml;
    opens org.dam.fcojavier.chatofflinexml.controllers to javafx.fxml;
    opens org.dam.fcojavier.chatofflinexml.model to java.xml.bind;

    exports org.dam.fcojavier.chatofflinexml;
    exports org.dam.fcojavier.chatofflinexml.controllers;
    exports org.dam.fcojavier.chatofflinexml.model;
    exports org.dam.fcojavier.chatofflinexml.utils;
}
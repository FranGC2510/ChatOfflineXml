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

    opens org.dam.fcojavier.chatofflinexml to javafx.fxml;
    exports org.dam.fcojavier.chatofflinexml;

    opens org.dam.fcojavier.chatofflinexml.model to java.xml.bind;
    exports org.dam.fcojavier.chatofflinexml.model;
}
module cz.vse.praguePub.main {
    requires static lombok;

    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.slf4j;
    requires java.scripting;

    requires javafx.controls;
    requires javafx.web;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.apache.commons.codec;
    requires org.apache.commons.lang3;

    exports cz.vse.praguePub.start;
    opens cz.vse.praguePub.logika.dbObjekty;
}

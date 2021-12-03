module praguePub.main {
    requires javafx.controls;

    requires static lombok;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;

    requires org.apache.commons.codec;

    exports cz.vse.praguePub.start;
}

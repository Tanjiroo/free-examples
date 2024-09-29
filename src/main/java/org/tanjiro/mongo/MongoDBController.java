package org.tanjiro.mongo;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;


public interface MongoDBController {

    String getCollectionName();

    MongoCollection getCollection(String collection);

    void setCollection(MongoCollection collection);

    MongoDBControllerImpl setCollectionName(String collectionName);

    String getDatabaseName();


    MongoClient getConnection();

    MongoCollection getCollection();

    ClientSession getSession();


}

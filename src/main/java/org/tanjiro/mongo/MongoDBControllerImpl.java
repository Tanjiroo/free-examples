package org.tanjiro.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import org.bson.Document;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MongoDBControllerImpl implements MongoDBController{
    private MongoClient connection;
    private String collectionName;
    private String databaseName;
    private MongoCollection collection;
    private ClientSession session;
    public MongoDBControllerImpl(String path){

            try (InputStream input = MongoDBControllerImpl.class.getClassLoader().getResourceAsStream(path)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // Read MongoDB connection properties from the properties file
            String _host = prop.getProperty("host");
            String _port = prop.getProperty("port");
            String _password = prop.getProperty("password");
            String _user = prop.getProperty("user");
            String _database = prop.getProperty("database");
            databaseName = prop.getProperty("database");
            collectionName = prop.getProperty("collection");
            MongoCredential credential = MongoCredential.createCredential(
                    _user,
                    _database,
                    _password.toCharArray()
            );
            this.connection = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyToClusterSettings(builder ->
                                    builder.hosts(Arrays.asList(new ServerAddress(_host, Integer.parseInt(_port)))))
                            //.credential(credential)
                            .build()
            );
            this.session = this.getConnection().startSession();
    } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public String getCollectionName() {
        return collectionName;
    }

    public List<Document> getAllDocuments(){
        List<Document> documents = new ArrayList<>();
        MongoCollection collection = this.connection
                .getDatabase(databaseName)
                .getCollection(collectionName);
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                documents.add(doc);

            }
        } finally {
            cursor.close();
        }
        return documents;
    }
    @Override
    public MongoCollection getCollection(String collection) {
        this.collection = this.connection.getDatabase(databaseName).getCollection(collection);
        return this.collection;
    }

    @Override
    public void setCollection(MongoCollection collection) {
        this.collection = collection;
    }

    @Override
    public MongoDBControllerImpl setCollectionName(String collectionName) {
        this.collectionName = collectionName;
        return this;

    }
    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    public MongoDBControllerImpl setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    @Override
    public String toString() {
        return "MongoDBControllerImpl{" +
                "connection=" + connection +
                ", collectionName='" + collectionName + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", collection=" + collection +
                '}';
    }

    @Override
    public MongoClient getConnection() {
        return connection;
    }

    @Override
    public MongoCollection getCollection() {
        return collection;
    }

    @Override
    public ClientSession getSession() {
        return session;
    }



}

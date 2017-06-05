package com.yl.ims.controller;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class MongoDbTestCtrl {
	public static void main(String[] args) throws UnknownHostException {
		Mongo mg = new Mongo("127.0.0.1",27017);
		
		// 查询所有的Database
		for (String name : mg.getDatabaseNames()) {
			System.out.println("dbName: " + name);
		}

		DB db = mg.getDB("test");
		// 查询所有的聚集集合
		for (String name : db.getCollectionNames()) {
			System.out.println("collectionName: " + name);
		}

		DBCollection users = db.getCollection("users");

		// 查询所有的数据
		DBCursor cur = users.find();
		while (cur.hasNext()) {
			System.out.println(cur.next());
		}
		System.out.println(cur.count());
		System.out.println(cur.getCursorId());
		System.out.println(JSON.serialize(cur));
	}

}

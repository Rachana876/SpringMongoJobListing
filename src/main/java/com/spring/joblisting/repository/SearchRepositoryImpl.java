package com.spring.joblisting.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.spring.joblisting.model.Post;

@Component
public class SearchRepositoryImpl implements SearchRepository{

	
	@Autowired
	MongoClient client;
	
	@Autowired
	MongoConverter converter;
	
	@Override
	public List<Post> finaByText(String text) {
		final List<Post> posts= new ArrayList<>();
		
		//Getting the search filter from Mongo Atlas from collection- aggregate after adding stages of filtering 
		//1. Search
		//2. Sort based on exp
		//3. Limit of upto 5
		MongoDatabase database = client.getDatabase("Rachana");
		MongoCollection<Document> collection = database.getCollection("JobPost");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
		    new Document("text", 
		    new Document("query", text)
		                .append("path", Arrays.asList("techs", "desc", "profile")))), 
		    new Document("$sort", 
		    new Document("exp", 1L)), 
		    new Document("$limit", 5L)));

		//Adding data in posts from result using MongoConverter , converter can convert the given type to desired type
		result.forEach(doc -> posts.add(converter.read(Post.class, doc)));
		
		return posts;
	}

}

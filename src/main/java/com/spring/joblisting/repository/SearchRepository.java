package com.spring.joblisting.repository;

import java.util.List;

import com.spring.joblisting.model.Post;

public interface SearchRepository {
	
	List<Post> finaByText(String text);

}

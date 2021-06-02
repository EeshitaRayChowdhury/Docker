package com.demo.repository;

import org.junit.Before;
import org.junit.Test;

import com.demo.repository.domain.Node;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class NodeRepositoryIT {

	private NodeRepository nodeRepository;
	
	@Before
	public void setup(){
		nodeRepository = new NodeRepository();
	}
	
	@Test
	public void testPersistSingleNode(){
		Node node = new Node();
		nodeRepository.saveNode(node);
		node = new Node();
		nodeRepository.saveNode(node);	
		assertThat(nodeRepository.getAllNodes().size(), is(2));
	}	
}

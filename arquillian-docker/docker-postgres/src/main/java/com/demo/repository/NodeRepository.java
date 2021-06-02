package com.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.demo.repository.domain.Node;

public class NodeRepository {
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("node_db");
	
	public void saveNode(Node node){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(node);
		em.getTransaction().commit();
		em.close();
	}

	public List<Node> getAllNodes(){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Query query = em.createQuery("select n from Node n");
		List<Node> nodes = query.getResultList();
		em.getTransaction().commit();
		em.close();
		return nodes;
	}
}

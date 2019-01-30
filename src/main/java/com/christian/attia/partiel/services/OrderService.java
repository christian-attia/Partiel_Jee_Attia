package com.christian.attia.partiel.services;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.christian.attia.partiel.database.PersistenceManager;
import com.christian.attia.partiel.database.ScopedEntityManager;
import com.christian.attia.partiel.database.entities.Order;
import com.christian.attia.partiel.database.entities.Product;


@Stateless
@LocalBean
@Path("/orders")
public class OrderService {

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getAllOrders() {
    	try (ScopedEntityManager em = getScopedEntityManager()) {
    		
    		List<Order> orders = em.createQuery("select o from Order o ").getResultList();
    		
			return  orders;
		}
    }
	
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrder(@PathParam("id") Long id) {
    	try (ScopedEntityManager em = getScopedEntityManager()) {
    		
    		TypedQuery<Order> query = em.createQuery("select o from Order o where o.id = :id", Order.class);
    		query.setParameter("id", id.intValue());
    		
    		Order order = query.getSingleResult();
    		
			return order;
		}
        
    }
    
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteOrder(@PathParam("id") int id) {
    	try (ScopedEntityManager em = getScopedEntityManager()) {
    		
    		EntityTransaction tx = em.getTransaction();

    		tx.begin();
    		
    		Query query = em.createQuery("delete from Order where id = :id");
    		
    		query.setParameter("id", id).executeUpdate();
    		
    		tx.commit();
    		
    	}
    }
    

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createOrder(Order order) {
    	try (ScopedEntityManager em = getScopedEntityManager()) {
    		
    		EntityTransaction tx = em.getTransaction();

    		tx.begin();
    		
    		// TODO: création d'un utilisateur / ajouter un object user en paramètre
    		
    		em.persist(order);
    		
    		tx.commit();
    		
    	}
    }
    
    
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateOrder(@PathParam("id") int id, Order order) {
    	try (ScopedEntityManager em = getScopedEntityManager()) {
    		
    		EntityTransaction tx = em.getTransaction();

    		tx.begin();
    		
    		// TODO: ajouter un utilisateur en paramètre pour l'update
    		
    		
    		
    		Query query = em.createQuery("update Order o set"
    				+ "  o.user_id = :user_id"
    				+ "  where o.id = :id");
    		
    		query.setParameter("id", id);
    		query.setParameter("user_id", order.getUser().getId());

    		
    		query.executeUpdate();
    		
    		tx.commit();
    		
    	}
    }
	
    public ScopedEntityManager getScopedEntityManager() {
		return PersistenceManager.getInstance().getScopedEntityManagerFactory().createScopedEntityManager();
	}
}

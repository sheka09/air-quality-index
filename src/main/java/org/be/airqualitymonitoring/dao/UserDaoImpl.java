package org.be.airqualitymonitoring.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.be.airqualitymonitoring.entity.User;
import org.be.airqualitymonitoring.error.UserAlreadyExistsException;
import org.be.airqualitymonitoring.error.UsernameAlreadyExistsException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	
	@Override
	public User save(User user) throws UserAlreadyExistsException,UsernameAlreadyExistsException,DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(user);
		entityManager.flush();
		entityManager.close();
        transaction.commit();

		return user;
	}

    @Override
    public User update(int id, User user) throws DataAccessException  {
    	EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		User existing_User=entityManager.find(User.class, id);
		existing_User.setId(id);
        entityManager.merge(existing_User);
		entityManager.close();
        transaction.commit();

     return user;
     
    }
	@Override
	public User delete(int id) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
      	User user=entityManager.find(User.class, id);
	
	entityManager.remove(user);
	entityManager.close();
	transaction.commit();
	
	return user;

	}


	@Override
	public User findByEmail(String email) throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
    TypedQuery<User> query=entityManager.createQuery("select u from User u where u.email=:email",User.class)
    		.setParameter("email", email);
    	User user=query.getSingleResult();
		return user;
	}

	@Override
	public User findByPhonenumber(String phoneNumber)  throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
	    TypedQuery<User> query=entityManager.createQuery("select u from User u where u.phoneNumber=:phoneNumber",User.class)
	    		.setParameter("phoneNumber", phoneNumber);
	    	User user=query.getSingleResult();
			entityManager.close();
			return user;
		
	}

	
	@Override
	public User findBySensor(int sensorId) throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		 TypedQuery<User> query=entityManager.createQuery("select u from User u where u.sensorId=:sensorId",User.class)
		    		.setParameter("sensorId", sensorId);
		    	User user=query.getSingleResult();
				entityManager.close();
				return user;

	}



	// find users by zipcode
	@Override
	public List<User> findByZipcode(String zipCode) throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		TypedQuery<User> query=entityManager.createQuery("select u from User u where u.zipCode=:zipCode",User.class)
	    		.setParameter("zipCode", zipCode);
	    	List<User> users=query.getResultList();
			entityManager.close();
         transaction.commit();
			return users;
	}

	@Override
	public User findById(int id) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		 TypedQuery<User> query=entityManager.createQuery("select u from User u where u.id=:id",User.class)
		    		.setParameter("id", id);
		    	User user=query.getSingleResult();
		    	entityManager.close();
		    	transaction.commit();
				return user;


	}

	@Override
	public List<User> findByRole(String role) throws DataAccessException{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		TypedQuery<User> query=entityManager.createQuery("select u from User u where u.role=:role",User.class)
				.setParameter("role", role);
		List<User> users=query.getResultList();
		entityManager.close();
		transaction.commit();
		return users;
	}

	@Override
	public User findByUsername(String username) throws DataAccessException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		TypedQuery<User> query = entityManager
				.createQuery("select u from User u where u.username=:username", User.class)
				.setParameter("username", username);
		
		List<User> users = query.getResultList();
		User user=null;
		if(!users.isEmpty()) {
			user=users.get(0);
		}
		
			entityManager.close();
		transaction.commit();
		return user;
	}
	

}

package org.be.airqualitymonitoring.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.be.airqualitymonitoring.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Override
	public Role findById(int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Role> query = entityManager.createQuery("select r from Role r where r.id=:id", Role.class)
				.setParameter("id", id);
		Role role = query.getSingleResult();
		entityManager.close();
		transaction.commit();
		return role;
	}

	@Override
	public List<Role> findByName(String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		TypedQuery<Role> query = entityManager.createQuery("select r from Role r where r.name=:name", Role.class)
				.setParameter("name", name);
		List<Role> role = query.getResultList();
		entityManager.close();
		transaction.commit();
		return role;
	}

	@Override
	public Collection<Role> save(Collection<Role> roles) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(roles);
		entityManager.close();
		transaction.commit();
		return roles;
	}

}

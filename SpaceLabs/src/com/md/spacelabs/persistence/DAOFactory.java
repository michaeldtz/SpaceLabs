package com.md.spacelabs.persistence;


public class DAOFactory {

	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractBaseEntity> DAO<T> getEntityManager(String name){
		
		return DAO.getByName(name);
		
	}
}

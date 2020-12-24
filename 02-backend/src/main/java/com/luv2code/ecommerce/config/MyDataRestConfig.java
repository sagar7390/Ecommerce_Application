package com.luv2code.ecommerce.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{
	
	private EntityManager entityManager;
	
	@Autowired
	public MyDataRestConfig(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		HttpMethod[] theUnsupportedActions = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};
		
		//disable http  methods for products : PUT, POST AND DELETE
		config.getExposureConfiguration()
			.forDomainType(Product.class)
			.withItemExposure((metdata,httpMethods)  -> httpMethods.disable(theUnsupportedActions))
			.withAssociationExposure((metdata, httpMethods)  -> httpMethods.disable(theUnsupportedActions));
		
		//disable http  methods for products-category : PUT, POST AND DELETE
				config.getExposureConfiguration()
					.forDomainType(ProductCategory.class)
					.withItemExposure((metdata,httpMethods)  -> httpMethods.disable(theUnsupportedActions))
					.withAssociationExposure((metdata, httpMethods)  -> httpMethods.disable(theUnsupportedActions));
	
	//call an internal helper method
				exposeIds(config);
	}

	private void exposeIds(RepositoryRestConfiguration config) {
		
		//exposing entity ids
		
		//get a list of all the entities classes using the entity manager
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		
		//create an array of the entity types
		List<Class> entityClasses = new ArrayList<>();
		
		//get the entity types for the entities
		for(EntityType tempEntityType : entities) {
			entityClasses.add(tempEntityType.getJavaType());
		}
		
		//expose the entity ids for the array of enitity/domain types
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		
		config.exposeIdsFor(domainTypes);
	}

	
	

}

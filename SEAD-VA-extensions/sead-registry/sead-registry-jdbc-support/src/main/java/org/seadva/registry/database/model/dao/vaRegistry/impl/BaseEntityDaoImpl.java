package org.seadva.registry.database.model.dao.vaRegistry.impl;

import com.felees.hbnpojogen.persistence.impl.GenericHibernateDaoImpl;
import org.seadva.registry.database.model.obj.vaRegistry.BaseEntity;
import org.springframework.stereotype.Repository;
import org.seadva.registry.database.model.dao.vaRegistry.BaseEntityDao;
 
 
/**
 * DAO for table: BaseEntity.
 * @author autogenerated
 */
@Repository
public class BaseEntityDaoImpl extends GenericHibernateDaoImpl<BaseEntity, String> implements BaseEntityDao {
	
	/** Constructor method. */
		public BaseEntityDaoImpl() {
			super(BaseEntity.class);
		}
	}

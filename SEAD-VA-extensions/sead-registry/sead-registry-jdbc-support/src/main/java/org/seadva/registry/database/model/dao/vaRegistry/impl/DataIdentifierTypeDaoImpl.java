package org.seadva.registry.database.model.dao.vaRegistry.impl;

import com.felees.hbnpojogen.persistence.impl.GenericHibernateDaoImpl;
import org.seadva.registry.database.model.obj.vaRegistry.DataIdentifierType;
import org.springframework.stereotype.Repository;
import org.seadva.registry.database.model.dao.vaRegistry.DataIdentifierTypeDao;
 
 
/**
 * DAO for table: DataIdentifierType.
 * @author autogenerated
 */
@Repository
public class DataIdentifierTypeDaoImpl extends GenericHibernateDaoImpl<DataIdentifierType, String> implements DataIdentifierTypeDao {
	
	/** Constructor method. */
		public DataIdentifierTypeDaoImpl() {
			super(DataIdentifierType.class);
		}
	}

package org.seadva.registry.database.model.dao.vaRegistry.impl;

import com.felees.hbnpojogen.persistence.impl.GenericHibernateDaoImpl;
import org.seadva.registry.database.model.obj.vaRegistry.Event;
import org.springframework.stereotype.Repository;
import org.seadva.registry.database.model.dao.vaRegistry.EventDao;
 
 
/**
 * DAO for table: Event.
 * @author autogenerated
 */
@Repository
public class EventDaoImpl extends GenericHibernateDaoImpl<Event, String> implements EventDao {
	
	/** Constructor method. */
		public EventDaoImpl() {
			super(Event.class);
		}
	}

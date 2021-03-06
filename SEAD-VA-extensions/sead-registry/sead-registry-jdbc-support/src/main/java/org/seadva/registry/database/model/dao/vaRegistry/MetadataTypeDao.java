package org.seadva.registry.database.model.dao.vaRegistry;

import org.seadva.registry.database.model.obj.vaRegistry.MetadataType;
 
/**
 * DAO interface for table: MetadataType.
 * @author autogenerated
 */
public interface MetadataTypeDao {
	// constructor only
    MetadataType getMetadataType(String metadataName);
    MetadataType getMetadataTypeById(String metadataId);
}


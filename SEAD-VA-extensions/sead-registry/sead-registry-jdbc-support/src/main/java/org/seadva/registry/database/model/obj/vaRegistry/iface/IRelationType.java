package org.seadva.registry.database.model.obj.vaRegistry.iface;
import java.util.Set;
import javax.persistence.Basic;
import org.seadva.registry.database.model.obj.vaRegistry.Relation;


/** 
 * Object interface mapping for hibernate-handled table: relation_type.
 * @author autogenerated
 */

public interface IRelationType {



    /**
     * Return the value associated with the column: id.
	 * @return A String object (this.id)
	 */
	String getId();
	

  
    /**  
     * Set the value related to the column: id.
	 * @param id the id value you wish to set
	 */
	void setId(final String id);

    /**
     * Return the value associated with the column: relation.
	 * @return A Set&lt;Relation&gt; object (this.relation)
	 */
	//Set<Relation> getRelations();
	
	/**
	 * Adds a bi-directional link of type Relation to the relations set.
	 * @param relation item to add
	 */
//	void addRelation(Relation relation);

  
    /**  
     * Set the value related to the column: relation.
	 * @param relation the relation value you wish to set
	 */
//	void setRelations(final Set<Relation> relation);

    /**
     * Return the value associated with the column: relationElement.
	 * @return A String object (this.relationElement)
	 */
	String getRelationElement();
	

  
    /**  
     * Set the value related to the column: relationElement.
	 * @param relationElement the relationElement value you wish to set
	 */
	void setRelationElement(final String relationElement);

    /**
     * Return the value associated with the column: relationSchema.
	 * @return A String object (this.relationSchema)
	 */
	String getRelationSchema();
	

  
    /**  
     * Set the value related to the column: relationSchema.
	 * @param relationSchema the relationSchema value you wish to set
	 */
	void setRelationSchema(final String relationSchema);

	// end of interface
}
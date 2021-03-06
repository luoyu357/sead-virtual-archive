package org.seadva.registry.database.model.obj.vaRegistry.iface;
import java.util.Set;
import javax.persistence.Basic;
import org.seadva.registry.database.model.obj.vaRegistry.AgentProfile;


/** 
 * Object interface mapping for hibernate-handled table: profile_type.
 * @author autogenerated
 */

public interface IProfileType {



    /**
     * Return the value associated with the column: agentProfile.
	 * @return A Set&lt;AgentProfile&gt; object (this.agentProfile)
	 */
	//Set<AgentProfile> getAgentProfiles();
	
	/**
	 * Adds a bi-directional link of type AgentProfile to the agentProfiles set.
	 * @param agentProfile item to add
	 */
	//void addAgentProfile(AgentProfile agentProfile);

  
    /**  
     * Set the value related to the column: agentProfile.
	 * @param agentProfile the agentProfile value you wish to set
	 */
	//void setAgentProfiles(final Set<AgentProfile> agentProfile);

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
     * Return the value associated with the column: profileTypeName.
	 * @return A String object (this.profileTypeName)
	 */
	String getProfileTypeName();
	

  
    /**  
     * Set the value related to the column: profileTypeName.
	 * @param profileTypeName the profileTypeName value you wish to set
	 */
	void setProfileTypeName(final String profileTypeName);

    /**
     * Return the value associated with the column: profileTypeSchema.
	 * @return A String object (this.profileTypeSchema)
	 */
	String getProfileTypeSchema();
	

  
    /**  
     * Set the value related to the column: profileTypeSchema.
	 * @param profileTypeSchema the profileTypeSchema value you wish to set
	 */
	void setProfileTypeSchema(final String profileTypeSchema);

	// end of interface
}
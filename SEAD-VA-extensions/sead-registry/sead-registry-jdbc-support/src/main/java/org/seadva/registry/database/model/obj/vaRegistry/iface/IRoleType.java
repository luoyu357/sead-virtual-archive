package org.seadva.registry.database.model.obj.vaRegistry.iface;
import java.util.Set;
import javax.persistence.Basic;
import org.seadva.registry.database.model.obj.vaRegistry.AgentRole;


/** 
 * Object interface mapping for hibernate-handled table: role_type.
 * @author autogenerated
 */

public interface IRoleType {



    /**
     * Return the value associated with the column: agentRole.
	 * @return A Set&lt;AgentRole&gt; object (this.agentRole)
	 */
	Set<AgentRole> getAgentRoles();
	
	/**
	 * Adds a bi-directional link of type AgentRole to the agentRoles set.
	 * @param agentRole item to add
	 */
	void addAgentRole(AgentRole agentRole);

  
    /**  
     * Set the value related to the column: agentRole.
	 * @param agentRole the agentRole value you wish to set
	 */
	void setAgentRoles(final Set<AgentRole> agentRole);

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
     * Return the value associated with the column: roleDescription.
	 * @return A String object (this.roleDescription)
	 */
	String getRoleDescription();
	

  
    /**  
     * Set the value related to the column: roleDescription.
	 * @param roleDescription the roleDescription value you wish to set
	 */
	void setRoleDescription(final String roleDescription);

    /**
     * Return the value associated with the column: roleTypeName.
	 * @return A String object (this.roleTypeName)
	 */
	String getRoleTypeName();
	

  
    /**  
     * Set the value related to the column: roleTypeName.
	 * @param roleTypeName the roleTypeName value you wish to set
	 */
	void setRoleTypeName(final String roleTypeName);

	// end of interface
}
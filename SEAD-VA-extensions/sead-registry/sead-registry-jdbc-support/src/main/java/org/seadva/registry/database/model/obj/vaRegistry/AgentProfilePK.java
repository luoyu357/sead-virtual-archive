package org.seadva.registry.database.model.obj.vaRegistry;

import com.google.gson.annotations.Expose;
import org.seadva.registry.database.model.obj.vaRegistry.iface.IAgentProfilePK;


/** 
 * Object mapping for hibernate-handled table: agent_profile.
 * @author autogenerated
 */

public class AgentProfilePK implements IAgentProfilePK {

	/** Serial Version UID. */
	private static final long serialVersionUID = -559002657L;

	

	/** Field mapping. */
	private Agent agent;

    @Expose
	/** Field mapping. */
	private ProfileType profileType;

 


 
	/** Return the type of this class. Useful for when dealing with proxies.
	* @return Defining class.
	*/
	public Class<?> getClassType() {
		return AgentProfilePK.class;
	}
 

    /**
     * Return the value associated with the column: agent.
	 * @return A Agent object (this.agent)
	 */
	public Agent getAgent() {
		return this.agent;
		
	}
	

  
    /**  
     * Set the value related to the column: agent.
	 * @param agent the agent value you wish to set
	 */
	public void setAgent(final Agent agent) {
		this.agent = agent;
	}

    /**
     * Return the value associated with the column: profileType.
	 * @return A ProfileType object (this.profileType)
	 */
	public ProfileType getProfileType() {
		return this.profileType;
		
	}
	

  
    /**  
     * Set the value related to the column: profileType.
	 * @param profileType the profileType value you wish to set
	 */
	public void setProfileType(final ProfileType profileType) {
		this.profileType = profileType;
	}


   /**
    * Deep copy.
	* @return cloned object
	* @throws CloneNotSupportedException on error
    */
    @Override
    public AgentProfilePK clone() throws CloneNotSupportedException {
		
        final AgentProfilePK copy = (AgentProfilePK)super.clone();

		return copy;
	}
	


	/** Provides toString implementation.
	 * @see Object#toString()
	 * @return String representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();		
	}


	/** Equals implementation. 
	 * @see Object#equals(Object)
	 * @param aThat Object to compare with
	 * @return true/false
	 */
	@Override
	public boolean equals(final Object aThat) {
		Object proxyThat = aThat;
		
		if ( this == aThat ) {
			 return true;
		}

		if (aThat == null)  {
			 return false;
		}
		
		final AgentProfilePK that; 
		try {
			that = (AgentProfilePK) proxyThat;
			if ( !(that.getClassType().equals(this.getClassType()))){
				return false;
			}
		} catch (org.hibernate.ObjectNotFoundException e) {
				return false;
		} catch (ClassCastException e) {
				return false;
		}
		
		
		boolean result = true;
		result = result && (((getAgent() == null) && (that.getAgent() == null)) || (getAgent() != null && getAgent().getId().equals(that.getAgent().getId())));	
		result = result && (((getProfileType() == null) && (that.getProfileType() == null)) || (getProfileType() != null && getProfileType().getId().equals(that.getProfileType().getId())));	
		return result;
	}
	
	/** Calculate the hashcode.
	 * @see Object#hashCode()
	 * @return a calculated number
	 */
	@Override
	public int hashCode() {
	int hash = 0;
	//	hash = hash + getAgent().hashCode();
		hash = hash + getProfileType().hashCode();
	return hash;
	}
	

	
}

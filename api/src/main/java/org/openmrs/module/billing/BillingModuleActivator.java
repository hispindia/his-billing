/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Billing module.
 *
 *  Billing module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Billing module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Billing module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.billing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.billing.util.Maintainer;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class BillingModuleActivator implements ModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see org.openmrs.module.Activator#startup()
	 */
	public void startup() {
		log.info("Starting Billing Module");		
	}
	
	/**
	 * @see org.openmrs.module.Activator#shutdown()
	 */
	public void shutdown() {
		log.info("Shutting down Billing Module");
	}

	public void contextRefreshed() {
		// TODO Auto-generated method stub
		
	}

	public void started() {
		Maintainer.maintain();	
	}

	public void stopped() {
		// TODO Auto-generated method stub
		
	}

	public void willRefreshContext() {
		// TODO Auto-generated method stub
		
	}

	public void willStart() {
		// TODO Auto-generated method stub
		
	}

	public void willStop() {
		// TODO Auto-generated method stub
		
	}
	
}

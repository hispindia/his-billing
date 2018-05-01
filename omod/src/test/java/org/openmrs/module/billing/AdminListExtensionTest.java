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

import java.util.Map;

import junit.framework.TestCase;

import org.openmrs.module.Extension.MEDIA_TYPE;
import org.openmrs.module.billing.extension.html.AdminList;

/**
 * This test validates the AdminList extension class
 */
public class AdminListExtensionTest extends TestCase {

	/**
	 * Get the links for the extension class
	 */
	public void testValidatesLinks() {
		AdminList ext = new AdminList();
		
		Map<String, String> links = ext.getLinks();
		
		assertNotNull("Some links should be returned", links);
		
		assertTrue("There should be a positive number of links", links.values().size() > 0);
	}
	
	/**
	 * Check the media type of this extension class
	 */
	public void testMediaTypeIsHtml() {
		AdminList ext = new AdminList();
		
		assertTrue("The media type of this extension should be html", ext.getMediaType().equals(MEDIA_TYPE.html));
	}
	
}

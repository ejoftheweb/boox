/*
 * uk.co.platosys.boox.core.Auditable
 * 
    Copyright (C) 2008  Edward Barrow

    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU  General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this program; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 
 * Auditable.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 */


package uk.co.platosys.boox.core;

import java.util.List;

import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;

/**
 * Account, Ledger and Journal all implement this interface
 * Its single audit() method returns a List of AuditElements.
 * Paradoxically, but with good reason, given the hierarchical
 * nature of accounts in Boox, it extends AuditElement.
 * 
 * 
 * @author edward
 */
public interface Auditable extends AuditElement {
     public List <AuditElement> audit(Enterprise enterprise, Clerk clerk) throws PermissionsException, BooxException;
}

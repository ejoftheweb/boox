 /*
  * uk.co.platosys.boox.core.AuditElement
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
 
 * AuditElement.java
 *
 * Created on 24 March 2007, 14:02
 *
 * 
 */

package uk.co.platosys.boox.core;

import java.util.Date;

import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Currency;
import uk.co.platosys.boox.money.Money;

/**
 * The AuditElement interface is key to Boox' auditing system.
 *
 * Ledger, Account and Transaction each implement AuditElement.
 *
 * @author edward
 */
public interface AuditElement {
   public Money getBalance(Enterprise enterprise, Clerk clerk) throws PermissionsException;
   public String getName();
   public Currency getCurrency();
   public Date getDate();
   
}

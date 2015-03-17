# boox
Boox is a project to create a java library for double entry book-keeping functionality

It works, but it isn't finished. The core functionality, creating transactions and posting them in three places - a debit account, a credit account and sequential journal - works fine. 
Accounts are organised into a hierarchy of ledgers to which a permissions system is attached. Users map to a "Clerk" object and permissions appertain to a clerk and a ledger. For each clerk/ledger combination there is an associated set of permissions which can be one or more of: balance, read, audit, credit, debit, post. 
The idea is to allow stakeholders to browse a set of accounts down to an appropriate level of detail.

The core code is in the uk.co.platosys.boox package

There is a dependency on uk.co.platosys.utils (Utils)

Boox further depends on postgresql; accounts are stored in a postgresql database. Each account is a separate table 
in the database. So you will need to have a working instance of postgresql and the associated jdbc driver.

Utils handles the database connections and uses Apache Commons connection pooling - so there is another dependency on the 
Apache Commons pooling packages.

The final dependency is on jdom2, which is used for xml files. Boox creates documents such as invoices as xml files
to which you can then apply an appropriate xsl transform to create readable formats. 

The Platax package, also in this repository for the time being, contains various GWT files to create a GWT front-end for Boox. This is more of a work-in-progress. There is no dependency on Platax in Boox, but Platax does depend on Boox.

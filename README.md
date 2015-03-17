# boox
Boox is a project to create a java library for double entry book-keeping functionality


The core code is in the uk.co.platosys.boox package

There is a dependency on uk.co.platosys.utils (Utils)

Boox further depends on postgresql; accounts are stored in a postgresql database. Each account is a separate table 
in the database. So you will need to have a working instance of postgresql and the associated jdbc driver.

Utils handles the database connections and uses Apache Commons connection pooling - so there is another dependency on the 
Apache Commons pooling packages.

The final dependency is on jdom2, which is used for xml files. Boox creates documents such as invoices as xml files
to which you can then apply an appropriate xsl transform to create readable formats. 

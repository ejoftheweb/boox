package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

public interface GWTDirectoryObject extends Serializable {
String getSysname();
GWTDirectoryEntry getDirectoryEntry();
void  setDirectoryEntry(GWTDirectoryEntry direntry);
}

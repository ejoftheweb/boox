package uk.co.platosys.db;

public interface SerialTable extends Table {
	public long addSerialRow(String primaryKeyColumName, String columnName, String columnValue) throws PlatosysDBException;
	public long addSerialRow();
}

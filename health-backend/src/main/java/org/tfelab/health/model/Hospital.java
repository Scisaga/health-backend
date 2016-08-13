package org.tfelab.health.model;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.DBName;
import org.tfelab.common.db.OrmLiteDaoManager;
import org.tfelab.common.json.JSON;
import org.tfelab.common.json.JSONable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "hospitals")
@DBName(value = "health")
public class Hospital implements JSONable<Hospital>{
	
	private static final Logger logger = LogManager.getLogger(Hospital.class.getName());
	
	@DatabaseField(columnName = "id", dataType = DataType.INTEGER, canBeNull = false, generatedId = true)
	public int id;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = false)
	public String name;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String city;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String district;
	
	@DatabaseField(dataType = DataType.STRING, width = 1024, canBeNull = true)
	public String address;
	
	@DatabaseField(dataType = DataType.STRING, width = 16, canBeNull = true)
	public String zip_code;
	
	@DatabaseField(dataType = DataType.FLOAT, canBeNull = true)
	public float longitude = 0;
	
	@DatabaseField(dataType = DataType.FLOAT, canBeNull = true)
	public float latitude = 0;
	
	@DatabaseField(columnName = "insert_time", dataType = DataType.DATE, canBeNull = false)
	public Date insert_time = new Date();
	
	@DatabaseField(dataType = DataType.DATE, columnName = "update_time", canBeNull = false)
    public Date update_time = new Date();
	
	public Hospital(){}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean insert() throws Exception{
		
		Dao<Hospital, String> dao = OrmLiteDaoManager.getDao(Hospital.class);
		
		if (dao.create(this) == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean update(){
		
		this.update_time = new Date();
		
		Dao<Hospital, String> dao;
		
		try {
			
			dao = OrmLiteDaoManager.getDao(Hospital.class);
			
			if (dao.update(this) == 1) {
				
				return true;
			}
			
		} catch (Exception e) {
			logger.error("Error Update doctor.", e);
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean delete() throws Exception{
		
		Dao<Hospital, String> dao = OrmLiteDaoManager.getDao(Hospital.class);
		
		if (dao.delete(this) == 1) {
			return true;
		}
		
		return false;
	}

	@Override
	public String toJSON() {
		return JSON.toJSON(this);
	}
	
}

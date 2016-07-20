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

@DatabaseTable(tableName = "users")
@DBName(value = "health")
public class User implements JSONable<User>{
	
	private static final Logger logger = LogManager.getLogger(User.class.getName());
	
	@DatabaseField(id = true, columnName = "id", dataType = DataType.INTEGER, canBeNull = false)
	public String id;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = false)
	public String name;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String nick_name;
	
	@DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
	public int gender = 1;
	
	@DatabaseField(dataType = DataType.INTEGER, canBeNull = false)
	public int age = 25;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String password;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String cell;
	
	@DatabaseField(dataType = DataType.STRING, width = 64, canBeNull = true)
	public String weixin;
	
	@DatabaseField(columnName = "insert_time", dataType = DataType.DATE, canBeNull = false)
	public Date insert_time = new Date();
	
	@DatabaseField(dataType = DataType.DATE, columnName = "update_time", canBeNull = false)
    public Date update_time = new Date();
	
	public User(){}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean insert() throws Exception{
		
		Dao<User, String> dao = OrmLiteDaoManager.getDao(User.class);
		
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
		
		Dao<User, String> dao;
		
		try {
			
			dao = OrmLiteDaoManager.getDao(User.class);
			
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
		
		Dao<User, String> dao = OrmLiteDaoManager.getDao(User.class);
		
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

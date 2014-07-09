package org.seadva.registry.database.model.dao.vaRegistry.impl;

import org.apache.log4j.Logger;
import org.seadva.registry.database.common.DBConnectionPool;
import org.seadva.registry.database.common.ObjectPool;
import org.seadva.registry.database.model.dao.vaRegistry.BaseEntityDao;
import org.seadva.registry.database.model.dao.vaRegistry.DataIdentifierDao;
import org.seadva.registry.database.model.dao.vaRegistry.DataLocationDao;
import org.seadva.registry.database.model.dao.vaRegistry.PropertyDao;
import org.seadva.registry.database.model.obj.vaRegistry.BaseEntity;
import org.seadva.registry.database.model.obj.vaRegistry.DataIdentifier;
import org.seadva.registry.database.model.obj.vaRegistry.DataLocation;
import org.seadva.registry.database.model.obj.vaRegistry.Property;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DAO for table: BaseEntity.
 * @author autogenerated
 */

public class BaseEntityDaoImpl implements BaseEntityDao {

    private static Logger log = Logger.getLogger(FixityDaoImpl.class);
    static PropertyDao propertyDao;
    static DataIdentifierDao dataIdentifierDao;
    static DataLocationDao dataLocationDao;

    protected Connection getConnection() throws SQLException {
        return connectionPool.getEntry();
    }
    public BaseEntityDaoImpl(){
        connectionPool = DBConnectionPool.getInstance();
        propertyDao = new PropertyDaoImpl();
        dataIdentifierDao = new DataIdentifierDaoImpl();
        dataLocationDao = new DataLocationDaoImpl();
    }


    protected ObjectPool<Connection> connectionPool = null;

    @Override
    public BaseEntity getBaseEntity(String entityId) {
        Connection connection = null;
        PreparedStatement statement = null;
        BaseEntity baseEntity = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("Select * from base_entity where entity_id=?");
            statement.setString(1, entityId);
            ResultSet resultSet = null;
            resultSet = statement.executeQuery();


            while (resultSet.next()) {
                baseEntity = new BaseEntity();
                baseEntity.setId( resultSet.getString("entity_id"));
                baseEntity.setEntityName(resultSet.getString("entity_name"));
                baseEntity.setEntityCreatedTime(resultSet.getTimestamp("entity_created_time"));
                baseEntity.setEntityLastUpdatedTime(resultSet.getTimestamp("entity_last_updated_time"));
                for(Property property:propertyDao.getProperties(entityId))
                    baseEntity.addProperty(property);
                for(DataIdentifier dataIdentifier:dataIdentifierDao.getDataIdentifiers(entityId))
                    baseEntity.addDataIdentifier(dataIdentifier);
                for(DataLocation dataLocation:dataLocationDao.getDataLocations(entityId))
                    baseEntity.addDataLocation(dataLocation);

                break;
            }


        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.warn("Unable to close statement", e);
                }
                statement = null;
            }
            connectionPool.releaseEntry(connection);

        }
        return baseEntity;
    }

    @Override
    public boolean insertEntity(BaseEntity baseEntity){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();

            statement = connection.prepareStatement("INSERT INTO base_entity values(?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE " +
                    "entity_name=?," +
                    "entity_last_updated_time=?");

            statement.setString(1, baseEntity.getId());
            statement.setString(2, baseEntity.getEntityName());
            statement.setTimestamp(3, new java.sql.Timestamp(baseEntity.getEntityCreatedTime().getTime()));
            statement.setTimestamp(4, new java.sql.Timestamp(baseEntity.getEntityLastUpdatedTime().getTime()));

            statement.setString(5, baseEntity.getEntityName());
            statement.setTimestamp(6, new java.sql.Timestamp(baseEntity.getEntityLastUpdatedTime().getTime()));
            statement.executeUpdate();
            statement.close();

            propertyDao.putProperties(baseEntity.getId(), baseEntity.getProperties());
            dataIdentifierDao.putDataIdentifiers(baseEntity.getId(), baseEntity.getDataIdentifiers());
            dataLocationDao.putDataLocations(baseEntity.getId(), baseEntity.getDataLocations());

            log.debug("Done resetting unfinished raw notifications");
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.warn("Unable to close statement", e);
                }
                statement = null;
            }
            connectionPool.releaseEntry(connection);

        }

        return true;
    }

    @Override
    public boolean updateEntity(String entityId, int isObsolete){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("UPDATE collection SET is_obsolete=? WHERE entity_id=?");
            statement.setInt(1, isObsolete);
            statement.setString(2, entityId);
            statement.executeUpdate();
            statement.close();
            log.debug("Done resetting unfinished raw notifications");
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.warn("Unable to close statement", e);
                }
                statement = null;
            }
            connectionPool.releaseEntry(connection);

        }
        return true;
    }
}


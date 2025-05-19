//package com.jaydee.School.util;
//
//import java.io.Serializable;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Properties;
//
//import org.hibernate.HibernateException;
//import org.hibernate.MappingException;
//import org.hibernate.engine.spi.SharedSessionContractImplementator;
//import org.hibernate.id.Configurable;
//import org.hibernate.id.IdentifierGenerator;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.type.Type;
//
///**
// * Custom ID generator for creating formatted string IDs.
// * @deprecated This class is no longer in use as the system has been migrated to use standard Long IDs with auto-increment.
// * Kept for reference purposes only.
// */
//@Deprecated
//public class CustomIdGenerator implements IdentifierGenerator, Configurable {
//
//    private String prefix;
//    private String entityName;
//    private int paddingSize = 4; // Default padding size for numeric part
//    
//    @Override
//    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
//        this.prefix = params.getProperty("prefix");
//        if (prefix == null) {
//            throw new MappingException("Prefix parameter not specified for CustomIdGenerator");
//        }
//        
//        this.entityName = params.getProperty("entity_name");
//        
//        String paddingSizeStr = params.getProperty("padding_size");
//        if (paddingSizeStr != null) {
//            try {
//                this.paddingSize = Integer.parseInt(paddingSizeStr);
//            } catch (NumberFormatException e) {
//                throw new MappingException("Invalid padding_size parameter for CustomIdGenerator", e);
//            }
//        }
//    }
//
//    @Override
//    public Serializable generate(SharedSessionContractImplementator session, Object object) throws HibernateException {
//        String tableName = entityName != null ? entityName : object.getClass().getSimpleName().toLowerCase();
//        
//        String query = String.format("SELECT MAX(CAST(SUBSTRING_INDEX(%s_id, '%s', -1) AS UNSIGNED)) FROM %s",
//                tableName, prefix, tableName);
//        
//        Connection connection = session.getJdbcConnectionAccess().obtainConnection();
//        try {
//            PreparedStatement ps = connection.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            int nextVal = 1;
//            if (rs.next()) {
//                try {
//                    Integer lastValue = rs.getInt(1);
//                    if (!rs.wasNull()) {
//                        nextVal = lastValue + 1;
//                    }
//                } catch (SQLException e) {
//                    // If parsing fails, start from 1
//                    nextVal = 1;
//                }
//            }
//            
//            // Format: prefix + padded number (e.g., U0001)
//            String paddingFormat = "%0" + paddingSize + "d";
//            return prefix + String.format(paddingFormat, nextVal);
//            
//        } catch (SQLException e) {
//            throw new HibernateException("Unable to generate custom ID", e);
//        } finally {
//            try {
//                if (connection != null) {
//                    session.getJdbcConnectionAccess().releaseConnection(connection);
//                }
//            } catch (SQLException e) {
//                throw new HibernateException("Unable to release JDBC connection", e);
//            }
//        }
//    }
//}

package com.dbms.dbms.dao;

import com.dbms.dbms.dto.RoomEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDao {
    private static final Logger logger = LoggerFactory.getLogger(RoomDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RoomEntry> getAllRoomsData() {
        String sql = "select * from ward_details";

        return jdbcTemplate.query(sql, mapRoomEntry());
    }

    public List<RoomEntry> getUnfilledRooms() {
        String sql = "select * from ward_details where filled=false";
        return jdbcTemplate.query(sql, mapRoomEntry());
    }

    private RowMapper<RoomEntry> mapRoomEntry() {
        return (resultSet, i) -> {
            RoomEntry r = new RoomEntry();

            r.setRoomNo(resultSet.getInt("room_no"));
            r.setEquipment(resultSet.getString("equipment_available"));
            r.setFilled(resultSet.getBoolean("filled"));
            r.setCostPerDay(resultSet.getInt("cost_per_day"));
            r.setWardName(resultSet.getString("ward_name"));
            r.setRoomId(resultSet.getInt("room_id"));

            return r;
        };
    }
}

package com.dbms.dbms.service;

import com.dbms.dbms.dao.RoomDao;
import com.dbms.dbms.dto.RoomEntry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomDao roomDao;

    public RoomService(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomEntry> getAllRooms() {
        return roomDao.getAllRoomsData();
    }

    public List<RoomEntry> getUnfilledRooms() {
        return roomDao.getUnfilledRooms();
    }
}

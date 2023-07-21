package dev.cybo.devroomquizgame.database;

import java.util.HashMap;
import java.util.Map;

public class DatabaseRow {

    private final Map<String, Object> cells = new HashMap<>();

    public void addCell(String columnName, Object value) {
        cells.put(columnName, value);
    }

    public Object getCellValue(String columnName) {
        return cells.get(columnName);
    }

    public String getString(String columnName) {
        return (String) cells.get(columnName);
    }

    public Integer getInt(String columnName) {
        return (Integer) cells.get(columnName);
    }

    public Long getLong(String columnName) {
        return (Long) cells.get(columnName);
    }

    public Boolean getBoolean(String columnName) {
        return (Boolean) cells.get(columnName);
    }

    public Double getDouble(String columnName) {
        return (Double) cells.get(columnName);
    }

    public Float getFloat(String columnName) {
        return (Float) cells.get(columnName);
    }

    public byte[] getBlob(String columnName) {
        return (byte[]) cells.get(columnName);
    }

}

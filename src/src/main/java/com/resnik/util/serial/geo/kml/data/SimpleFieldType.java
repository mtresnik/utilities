package com.resnik.util.serial.geo.kml.data;

public enum SimpleFieldType {
    _string, _int, _uint, _short, _ushort, _float, _double, _bool;

    @Override
    public String toString(){
        return super.toString().replaceAll("_", "");
    }
}

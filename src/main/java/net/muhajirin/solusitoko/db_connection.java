/*
 * %W% %E%
 * 
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Oracle or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 * %W% %E%
 */

/**
 * Part: The TableModel adaptor (transforming the JDBC interface to the TableModel interface).
 *
 * @version 1.20 09/25/97
 * @author Philip Milne
 */

/**
 * Part: others of above ...
 *
 * @version 1.0 06/17/2014
 * @author Rafik, http://solusiprogram.com/
 */


package net.muhajirin.solusitoko;

import java.sql.*;
import javax.sql.*;
//cannot be found on android?! import javax.sql.rowset.*;
import javax.sql.RowSet.*;
//import com.sun.rowset.*;

import java.io.File;
import java.io.FileReader;
//import java.io.IOException;
//import java.io.FileNotFoundException;
import java.io.BufferedReader;
//import javax.swing.JOptionPane;
import java.util.*;
 
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.ArrayList;

/* ?????
import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelEvent; 

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JComponent;
import javax.swing.JComboBox;
*/


import java.math.BigDecimal;
import java.io.InputStream;
import java.io.Reader;

/* ga jadi ah, hanya buat ngebela2in r.size() yg hanya bisa bernilai 1 ...
class my_ResultSet implements ResultSet {
    // d most implementations to the underlying database result set:
    private final ResultSet d;
    private boolean already_moved_by_size_method = false;
    public my_ResultSet( ResultSet d ) {
        this.d = d;
        already_moved_by_size_method = false;
    }

    @Override public boolean next() throws SQLException {
        boolean ret;
        if( already_moved_by_size_method && isFirst() ) ret = true;    //jgn dimove lagi ke record kedua ...
        else                                            ret = d.next();
        already_moved_by_size_method = false;
        return ret;
    }

    @Override public void close() throws SQLException {
        already_moved_by_size_method = false;
        d.close();
    }

    public int size() {    //utk antisipasi sebagian kode program yg masih memanggil method ini (tadinya memang ada di cachedrowset), anyway ini hanya bisa mereturn 1 jika records ada, jadi tidak bisa menghitung size yg sebenarnya. Bisa juga dengan me-moveLast() trus moveBeforeFirst() ... tapi mungkin rada berat dan mungkin juga ga disupport mysqljdbc
        int ret=0;
        try {
            if( d.isBeforeFirst() ) {    //jika empty result, inipun false juga
                if( d.next() ) {
                    ret=1;
                    already_moved_by_size_method = true;
                }
            } else if( d.getRow() > 0 )
                ret=1;
        } catch( SQLException e ) {}
        return ret;
    }


    @Override public boolean isWrapperFor(Class<?> iface) throws SQLException {  return d.isWrapperFor(iface);  }; 
    @Override public <T> T unwrap(Class<T> iface) throws SQLException {  return d.unwrap(iface);  }; 


    //@Override public boolean next() throws SQLException {  return d.next();  }; 
    //@Override public void close() throws SQLException {  d.close();  }; 
    @Override public boolean wasNull() throws SQLException {  return d.wasNull();  }; 
    @Override public String getString(int columnIndex) throws SQLException {  return d.getString(columnIndex);  }; 
    @Override public boolean getBoolean(int columnIndex) throws SQLException {  return d.getBoolean(columnIndex);  }; 
    @Override public byte getByte(int columnIndex) throws SQLException {  return d.getByte(columnIndex);  }; 
    @Override public short getShort(int columnIndex) throws SQLException {  return d.getShort(columnIndex);  }; 
    @Override public int getInt(int columnIndex) throws SQLException {  return d.getInt(columnIndex);  }; 
    @Override public long getLong(int columnIndex) throws SQLException {  return d.getLong(columnIndex);  }; 
    @Override public float getFloat(int columnIndex) throws SQLException {  return d.getFloat(columnIndex);  }; 
    @Override public double getDouble(int columnIndex) throws SQLException {  return d.getDouble(columnIndex);  }; 
    @Override public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {  return d.getBigDecimal(columnIndex, scale);  }; 
    @Override public byte[] getBytes(int columnIndex) throws SQLException {  return d.getBytes(columnIndex);  }; 
    @Override public java.sql.Date getDate(int columnIndex) throws SQLException {  return d.getDate(columnIndex);  }; 
    @Override public java.sql.Time getTime(int columnIndex) throws SQLException {  return d.getTime(columnIndex);  }; 
    @Override public java.sql.Timestamp getTimestamp(int columnIndex) throws SQLException {  return d.getTimestamp(columnIndex);  }; 
    @Override public java.io.InputStream getAsciiStream(int columnIndex) throws SQLException {  return d.getAsciiStream(columnIndex);  }; 
    @Override public java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException {  return d.getUnicodeStream(columnIndex);  }; 
    @Override public java.io.InputStream getBinaryStream(int columnIndex) throws SQLException {  return d.getBinaryStream(columnIndex);  }; 
    @Override public String getString(String columnLabel) throws SQLException {  return d.getString(columnLabel);  }; 
    @Override public boolean getBoolean(String columnLabel) throws SQLException {  return d.getBoolean(columnLabel);  }; 
    @Override public byte getByte(String columnLabel) throws SQLException {  return d.getByte(columnLabel);  }; 
    @Override public short getShort(String columnLabel) throws SQLException {  return d.getShort(columnLabel);  }; 
    @Override public int getInt(String columnLabel) throws SQLException {  return d.getInt(columnLabel);  }; 
    @Override public long getLong(String columnLabel) throws SQLException {  return d.getLong(columnLabel);  }; 
    @Override public float getFloat(String columnLabel) throws SQLException {  return d.getFloat(columnLabel);  }; 
    @Override public double getDouble(String columnLabel) throws SQLException {  return d.getDouble(columnLabel);  }; 
    @Override public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {  return d.getBigDecimal(columnLabel, scale);  }; 
    @Override public byte[] getBytes(String columnLabel) throws SQLException {  return d.getBytes(columnLabel);  }; 
    @Override public java.sql.Date getDate(String columnLabel) throws SQLException {  return d.getDate(columnLabel);  }; 
    @Override public java.sql.Time getTime(String columnLabel) throws SQLException {  return d.getTime(columnLabel);  }; 
    @Override public java.sql.Timestamp getTimestamp(String columnLabel) throws SQLException {  return d.getTimestamp(columnLabel);  }; 
    @Override public java.io.InputStream getAsciiStream(String columnLabel) throws SQLException {  return d.getAsciiStream(columnLabel);  }; 
    @Override public java.io.InputStream getUnicodeStream(String columnLabel) throws SQLException {  return d.getUnicodeStream(columnLabel);  }; 
    @Override public java.io.InputStream getBinaryStream(String columnLabel) throws SQLException {  return d.getBinaryStream(columnLabel);  }; 
    @Override public SQLWarning getWarnings() throws SQLException {  return d.getWarnings();  }; 
    @Override public void clearWarnings() throws SQLException {  d.clearWarnings();  }; 
    @Override public String getCursorName() throws SQLException {  return d.getCursorName();  }; 
    @Override public ResultSetMetaData getMetaData() throws SQLException {  return d.getMetaData();  }; 
    @Override public Object getObject(int columnIndex) throws SQLException {  return d.getObject(columnIndex);  }; 
    @Override public Object getObject(String columnLabel) throws SQLException {  return d.getObject(columnLabel);  }; 
    @Override public int findColumn(String columnLabel) throws SQLException {  return d.findColumn(columnLabel);  }; 
    @Override public java.io.Reader getCharacterStream(int columnIndex) throws SQLException {  return d.getCharacterStream(columnIndex);  }; 
    @Override public java.io.Reader getCharacterStream(String columnLabel) throws SQLException {  return d.getCharacterStream(columnLabel);  }; 
    @Override public BigDecimal getBigDecimal(int columnIndex) throws SQLException {  return d.getBigDecimal(columnIndex);  }; 
    @Override public BigDecimal getBigDecimal(String columnLabel) throws SQLException {  return d.getBigDecimal(columnLabel);  }; 
    @Override public boolean isBeforeFirst() throws SQLException {  return d.isBeforeFirst();  }; 
    @Override public boolean isAfterLast() throws SQLException {  return d.isAfterLast();  }; 
    @Override public boolean isFirst() throws SQLException {  return d.isFirst();  }; 
    @Override public boolean isLast() throws SQLException {  return d.isLast();  }; 
    @Override public void beforeFirst() throws SQLException {  d.beforeFirst();  }; 
    @Override public void afterLast() throws SQLException {  d.afterLast();  }; 
    @Override public boolean first() throws SQLException {  return d.first();  }; 
    @Override public boolean last() throws SQLException {  return d.last();  }; 
    @Override public int getRow() throws SQLException {  return d.getRow();  }; 
    @Override public boolean absolute( int row ) throws SQLException {  return d.absolute(row );  }; 
    @Override public boolean relative( int rows ) throws SQLException {  return d.relative(rows );  }; 
    @Override public boolean previous() throws SQLException {  return d.previous();  }; 
    @Override public void setFetchDirection(int direction) throws SQLException {  d.setFetchDirection(direction);  }; 
    @Override public int getFetchDirection() throws SQLException {  return d.getFetchDirection();  }; 
    @Override public void setFetchSize(int rows) throws SQLException {  d.setFetchSize(rows);  }; 
    @Override public int getFetchSize() throws SQLException {  return d.getFetchSize();  }; 
    @Override public int getType() throws SQLException {  return d.getType();  }; 
    @Override public int getConcurrency() throws SQLException {  return d.getConcurrency();  }; 
    @Override public boolean rowUpdated() throws SQLException {  return d.rowUpdated();  }; 
    @Override public boolean rowInserted() throws SQLException {  return d.rowInserted();  }; 
    @Override public boolean rowDeleted() throws SQLException {  return d.rowDeleted();  }; 
    @Override public void updateNull(int columnIndex) throws SQLException {  d.updateNull(columnIndex);  }; 
    @Override public void updateBoolean(int columnIndex, boolean x) throws SQLException {  d.updateBoolean(columnIndex, x);  }; 
    @Override public void updateByte(int columnIndex, byte x) throws SQLException {  d.updateByte(columnIndex, x);  }; 
    @Override public void updateShort(int columnIndex, short x) throws SQLException {  d.updateShort(columnIndex, x);  }; 
    @Override public void updateInt(int columnIndex, int x) throws SQLException {  d.updateInt(columnIndex, x);  }; 
    @Override public void updateLong(int columnIndex, long x) throws SQLException {  d.updateLong(columnIndex, x);  }; 
    @Override public void updateFloat(int columnIndex, float x) throws SQLException {  d.updateFloat(columnIndex, x);  }; 
    @Override public void updateDouble(int columnIndex, double x) throws SQLException {  d.updateDouble(columnIndex, x);  }; 
    @Override public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {  d.updateBigDecimal(columnIndex, x);  }; 
    @Override public void updateString(int columnIndex, String x) throws SQLException {  d.updateString(columnIndex, x);  }; 
    @Override public void updateBytes(int columnIndex, byte x[]) throws SQLException {  d.updateBytes(columnIndex, x);  }; 
    @Override public void updateDate(int columnIndex, java.sql.Date x) throws SQLException {  d.updateDate(columnIndex, x);  }; 
    @Override public void updateTime(int columnIndex, java.sql.Time x) throws SQLException {  d.updateTime(columnIndex, x);  }; 
    @Override public void updateTimestamp(int columnIndex, java.sql.Timestamp x) throws SQLException {  d.updateTimestamp(columnIndex, x);  }; 
    @Override public void updateAsciiStream(int columnIndex, 			 java.io.InputStream x, 			 int length) throws SQLException {  d.updateAsciiStream(columnIndex, 			 x, 			 length);  }; 
    @Override public void updateBinaryStream(int columnIndex, 			 java.io.InputStream x, 			 int length) throws SQLException {  d.updateBinaryStream(columnIndex, 			 x, 			 length);  }; 
    @Override public void updateCharacterStream(int columnIndex, 			 java.io.Reader x, 			 int length) throws SQLException {  d.updateCharacterStream(columnIndex, 			 x, 			 length);  }; 
    @Override public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {  d.updateObject(columnIndex, x, scaleOrLength);  }; 
    @Override public void updateObject(int columnIndex, Object x) throws SQLException {  d.updateObject(columnIndex, x);  }; 
    @Override public void updateNull(String columnLabel) throws SQLException {  d.updateNull(columnLabel);  }; 
    @Override public void updateBoolean(String columnLabel, boolean x) throws SQLException {  d.updateBoolean(columnLabel, x);  }; 
    @Override public void updateByte(String columnLabel, byte x) throws SQLException {  d.updateByte(columnLabel, x);  }; 
    @Override public void updateShort(String columnLabel, short x) throws SQLException {  d.updateShort(columnLabel, x);  }; 
    @Override public void updateInt(String columnLabel, int x) throws SQLException {  d.updateInt(columnLabel, x);  }; 
    @Override public void updateLong(String columnLabel, long x) throws SQLException {  d.updateLong(columnLabel, x);  }; 
    @Override public void updateFloat(String columnLabel, float x) throws SQLException {  d.updateFloat(columnLabel, x);  }; 
    @Override public void updateDouble(String columnLabel, double x) throws SQLException {  d.updateDouble(columnLabel, x);  }; 
    @Override public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {  d.updateBigDecimal(columnLabel, x);  }; 
    @Override public void updateString(String columnLabel, String x) throws SQLException {  d.updateString(columnLabel, x);  }; 
    @Override public void updateBytes(String columnLabel, byte x[]) throws SQLException {  d.updateBytes(columnLabel, x);  }; 
    @Override public void updateDate(String columnLabel, java.sql.Date x) throws SQLException {  d.updateDate(columnLabel, x);  }; 
    @Override public void updateTime(String columnLabel, java.sql.Time x) throws SQLException {  d.updateTime(columnLabel, x);  }; 
    @Override public void updateTimestamp(String columnLabel, java.sql.Timestamp x) throws SQLException {  d.updateTimestamp(columnLabel, x);  }; 
    @Override public void updateAsciiStream(String columnLabel, 			 java.io.InputStream x, 			 int length) throws SQLException {  d.updateAsciiStream(columnLabel, 			 x, 			 length);  }; 
    @Override public void updateBinaryStream(String columnLabel, 			 java.io.InputStream x, 			 int length) throws SQLException {  d.updateBinaryStream(columnLabel, 			 x, 			 length);  }; 
    @Override public void updateCharacterStream(String columnLabel, 			 java.io.Reader reader, 			 int length) throws SQLException {  d.updateCharacterStream(columnLabel, 			 reader, 			 length);  }; 
    @Override public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {  d.updateObject(columnLabel, x, scaleOrLength);  }; 
    @Override public void updateObject(String columnLabel, Object x) throws SQLException {  d.updateObject(columnLabel, x);  }; 
    @Override public void insertRow() throws SQLException {  d.insertRow();  }; 
    @Override public void updateRow() throws SQLException {  d.updateRow();  }; 
    @Override public void deleteRow() throws SQLException {  d.deleteRow();  }; 
    @Override public void refreshRow() throws SQLException {  d.refreshRow();  }; 
    @Override public void cancelRowUpdates() throws SQLException {  d.cancelRowUpdates();  }; 
    @Override public void moveToInsertRow() throws SQLException {  d.moveToInsertRow();  }; 
    @Override public void moveToCurrentRow() throws SQLException {  d.moveToCurrentRow();  }; 
    @Override public Statement getStatement() throws SQLException {  return d.getStatement();  }; 
    @Override public Object getObject(int columnIndex, java.util.Map<String,Class<?>> map) throws SQLException {  return d.getObject(columnIndex, map);  }; 
    @Override public Ref getRef(int columnIndex) throws SQLException {  return d.getRef(columnIndex);  }; 
    @Override public Blob getBlob(int columnIndex) throws SQLException {  return d.getBlob(columnIndex);  }; 
    @Override public Clob getClob(int columnIndex) throws SQLException {  return d.getClob(columnIndex);  }; 
    @Override public Array getArray(int columnIndex) throws SQLException {  return d.getArray(columnIndex);  }; 
    @Override public Object getObject(String columnLabel, java.util.Map<String,Class<?>> map) throws SQLException {  return d.getObject(columnLabel, map);  }; 
    @Override public Ref getRef(String columnLabel) throws SQLException {  return d.getRef(columnLabel);  }; 
    @Override public Blob getBlob(String columnLabel) throws SQLException {  return d.getBlob(columnLabel);  }; 
    @Override public Clob getClob(String columnLabel) throws SQLException {  return d.getClob(columnLabel);  }; 
    @Override public Array getArray(String columnLabel) throws SQLException {  return d.getArray(columnLabel);  }; 
    @Override public java.sql.Date getDate(int columnIndex, Calendar cal) throws SQLException {  return d.getDate(columnIndex, cal);  }; 
    @Override public java.sql.Date getDate(String columnLabel, Calendar cal) throws SQLException {  return d.getDate(columnLabel, cal);  }; 
    @Override public java.sql.Time getTime(int columnIndex, Calendar cal) throws SQLException {  return d.getTime(columnIndex, cal);  }; 
    @Override public java.sql.Time getTime(String columnLabel, Calendar cal) throws SQLException {  return d.getTime(columnLabel, cal);  }; 
    @Override public java.sql.Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {  return d.getTimestamp(columnIndex, cal);  }; 
    @Override public java.sql.Timestamp getTimestamp(String columnLabel, Calendar cal)	 throws SQLException {  return d.getTimestamp(columnLabel, cal);  }; 
    @Override public java.net.URL getURL(int columnIndex) throws SQLException {  return d.getURL(columnIndex);  }; 
    @Override public java.net.URL getURL(String columnLabel) throws SQLException {  return d.getURL(columnLabel);  }; 
    @Override public void updateRef(int columnIndex, java.sql.Ref x) throws SQLException {  d.updateRef(columnIndex, x);  }; 
    @Override public void updateRef(String columnLabel, java.sql.Ref x) throws SQLException {  d.updateRef(columnLabel, x);  }; 
    @Override public void updateBlob(int columnIndex, java.sql.Blob x) throws SQLException {  d.updateBlob(columnIndex, x);  }; 
    @Override public void updateBlob(String columnLabel, java.sql.Blob x) throws SQLException {  d.updateBlob(columnLabel, x);  }; 
    @Override public void updateClob(int columnIndex, java.sql.Clob x) throws SQLException {  d.updateClob(columnIndex, x);  }; 
    @Override public void updateClob(String columnLabel, java.sql.Clob x) throws SQLException {  d.updateClob(columnLabel, x);  }; 
    @Override public void updateArray(int columnIndex, java.sql.Array x) throws SQLException {  d.updateArray(columnIndex, x);  }; 
    @Override public void updateArray(String columnLabel, java.sql.Array x) throws SQLException {  d.updateArray(columnLabel, x);  }; 
    @Override public RowId getRowId(int columnIndex) throws SQLException {  return d.getRowId(columnIndex);  }; 
    @Override public RowId getRowId(String columnLabel) throws SQLException {  return d.getRowId(columnLabel);  }; 
    @Override public void updateRowId(int columnIndex, RowId x) throws SQLException {  d.updateRowId(columnIndex, x);  }; 
    @Override public void updateRowId(String columnLabel, RowId x) throws SQLException {  d.updateRowId(columnLabel, x);  }; 
    @Override public int getHoldability() throws SQLException {  return d.getHoldability();  }; 
    @Override public boolean isClosed() throws SQLException {  return d.isClosed();  }; 
    @Override public void updateNString(int columnIndex, String nString) throws SQLException {  d.updateNString(columnIndex, nString);  }; 
    @Override public void updateNString(String columnLabel, String nString) throws SQLException {  d.updateNString(columnLabel, nString);  }; 
    @Override public void updateNClob(int columnIndex, NClob nClob) throws SQLException {  d.updateNClob(columnIndex, nClob);  }; 
    @Override public void updateNClob(String columnLabel, NClob nClob) throws SQLException {  d.updateNClob(columnLabel, nClob);  }; 
    @Override public NClob getNClob(int columnIndex) throws SQLException {  return d.getNClob(columnIndex);  }; 
    @Override public NClob getNClob(String columnLabel) throws SQLException {  return d.getNClob(columnLabel);  }; 
    @Override public SQLXML getSQLXML(int columnIndex) throws SQLException {  return d.getSQLXML(columnIndex);  }; 
    @Override public SQLXML getSQLXML(String columnLabel) throws SQLException {  return d.getSQLXML(columnLabel);  }; 
    @Override public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {  d.updateSQLXML(columnIndex, xmlObject);  }; 
    @Override public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {  d.updateSQLXML(columnLabel, xmlObject);  }; 
    @Override public String getNString(int columnIndex) throws SQLException {  return d.getNString(columnIndex);  }; 
    @Override public String getNString(String columnLabel) throws SQLException {  return d.getNString(columnLabel);  }; 
    @Override public java.io.Reader getNCharacterStream(int columnIndex) throws SQLException {  return d.getNCharacterStream(columnIndex);  }; 
    @Override public java.io.Reader getNCharacterStream(String columnLabel) throws SQLException {  return d.getNCharacterStream(columnLabel);  }; 
    @Override public void updateNCharacterStream(int columnIndex, 			 java.io.Reader x, 			 long length) throws SQLException {  d.updateNCharacterStream(columnIndex, 			 x, 			 length);  }; 
    @Override public void updateNCharacterStream(String columnLabel, 			 java.io.Reader reader, 			 long length) throws SQLException {  d.updateNCharacterStream(columnLabel, 			 reader, 			 length);  }; 
    @Override public void updateAsciiStream(int columnIndex, 			 java.io.InputStream x, 			 long length) throws SQLException {  d.updateAsciiStream(columnIndex, 			 x, 			 length);  }; 
    @Override public void updateBinaryStream(int columnIndex, 			 java.io.InputStream x, 			 long length) throws SQLException {  d.updateBinaryStream(columnIndex, 			 x, 			 length);  }; 
    @Override public void updateCharacterStream(int columnIndex, 			 java.io.Reader x, 			 long length) throws SQLException {  d.updateCharacterStream(columnIndex, 			 x, 			 length);  }; 
    @Override public void updateAsciiStream(String columnLabel, 			 java.io.InputStream x, 			 long length) throws SQLException {  d.updateAsciiStream(columnLabel, 			 x, 			 length);  }; 
    @Override public void updateBinaryStream(String columnLabel, 			 java.io.InputStream x, 			 long length) throws SQLException {  d.updateBinaryStream(columnLabel, 			 x, 			 length);  }; 
    @Override public void updateCharacterStream(String columnLabel, 			 java.io.Reader reader, 			 long length) throws SQLException {  d.updateCharacterStream(columnLabel, 			 reader, 			 length);  }; 
    @Override public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {  d.updateBlob(columnIndex, inputStream, length);  }; 
    @Override public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {  d.updateBlob(columnLabel, inputStream, length);  }; 
    @Override public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {  d.updateClob(columnIndex, reader, length);  }; 
    @Override public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {  d.updateClob(columnLabel, reader, length);  }; 
    @Override public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {  d.updateNClob(columnIndex, reader, length);  }; 
    @Override public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {  d.updateNClob(columnLabel, reader, length);  }; 
    @Override public void updateNCharacterStream(int columnIndex, 			 java.io.Reader x) throws SQLException {  d.updateNCharacterStream(columnIndex, 			 x);  }; 
    @Override public void updateNCharacterStream(String columnLabel, 			 java.io.Reader reader) throws SQLException {  d.updateNCharacterStream(columnLabel, 			 reader);  }; 
    @Override public void updateAsciiStream(int columnIndex, 			 java.io.InputStream x) throws SQLException {  d.updateAsciiStream(columnIndex, 			 x);  }; 
    @Override public void updateBinaryStream(int columnIndex, 			 java.io.InputStream x) throws SQLException {  d.updateBinaryStream(columnIndex, 			 x);  }; 
    @Override public void updateCharacterStream(int columnIndex, 			 java.io.Reader x) throws SQLException {  d.updateCharacterStream(columnIndex, 			 x);  }; 
    @Override public void updateAsciiStream(String columnLabel, 			 java.io.InputStream x) throws SQLException {  d.updateAsciiStream(columnLabel, 			 x);  }; 
    @Override public void updateBinaryStream(String columnLabel, 			 java.io.InputStream x) throws SQLException {  d.updateBinaryStream(columnLabel, 			 x);  }; 
    @Override public void updateCharacterStream(String columnLabel, 			 java.io.Reader reader) throws SQLException {  d.updateCharacterStream(columnLabel, 			 reader);  }; 
    @Override public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {  d.updateBlob(columnIndex, inputStream);  }; 
    @Override public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {  d.updateBlob(columnLabel, inputStream);  }; 
    @Override public void updateClob(int columnIndex, Reader reader) throws SQLException {  d.updateClob(columnIndex, reader);  }; 
    @Override public void updateClob(String columnLabel, Reader reader) throws SQLException {  d.updateClob(columnLabel, reader);  }; 
    @Override public void updateNClob(int columnIndex, Reader reader) throws SQLException {  d.updateNClob(columnIndex, reader);  }; 
    @Override public void updateNClob(String columnLabel, Reader reader) throws SQLException {  d.updateNClob(columnLabel, reader);  }; 


}
*/


public class db_connection /* extends AbstractTableModel */ {

/*
Modifier 	Class 	Package Subclass World
public 		Y 	Y 	Y 	Y
protected 	Y 	Y 	Y 	N
no modifier 	Y 	Y 	N 	N
private 	Y 	N 	N 	N

final: tidak bisa diubah oleh subclass
static: bisa diakses sebagai elemen class, tidak harus object.
*/
    Connection con = null;
    Statement cmd = null;
    PreparedStatement cmdp = null;
    //CachedRowSet does not exist in android, then keep using ResultSet >>    CachedRowSet rs = null; //utk menyimpan hasil SELECT
    ResultSet rs = null;    //my_ResultSet rs = null;    //ResultSet rs_temp = null;
    int affected = -1; //utk menyimpan hasil UPDATE atau INSERT
    int last_inserted_id;
    private Boolean record_found;

    String[]             columnNames = {};
    ArrayList<ArrayList> rows = new ArrayList<ArrayList>();    //perlu diinitiate agar (at least) rows.size() di getRowCount tidak return null :p
//is_android ...
    ArrayList<ArrayList> rows_backup = new ArrayList<ArrayList>();
    Boolean enable_filter;    //set by the caller who need searchview + filter and utilizing rows_backup
    int editor_barcode_target = -1;

    ResultSetMetaData    metaData;

//is_android ...    TableColumnModel     column_model;
    android.view.View[]   col_editor = {};    //is_android ...    JComponent[]         col_editor = {};
    int[]                col_width = {};

    //for the no db metaData:)
    String[]             col_label = {};
    int[]                col_type = {};
    int[]                col_precision = {};

    String last_sql = "";    String last_sql_read = "";
    String err_msg = "";
    String table_name = "";
    Boolean[] isSigned = {};

    int in_progress=0;

    private static String IV = "solusiprogam.com";
    protected String encryptionKey = "harus16digit:nih";

    public db_connection(/*JComponent parent*/) {
        read_cfg(); //ditaruh di sini memang ada resiko gak uptodate kecuali program ditutup :)
    }

    Map<String, String> cfg = new HashMap<String, String>() {{ //java does not support associative arrays :p
        put( "url_client_login"             , "" );
        put( "url_user_login"               , "" );
        put( "url_user_logout"              , "" );
        put( "url_product_index"            , "" );
        put( "url_customer_index"           , "" );
        put( "url_customer_create"          , "" );
        put( "url_order_create"             , "" );
        put( "url_order_index"              , "" );
        put( "client_token"             , "" );

        put( "database"                 , "" );
        put( "user"                     , "" );
        put( "password"                 , "" );
        put( "host"                     , "" );
        put( "socket"                   , "" );
        put( "dbms"                     , "" );
        put( "user_login_default"       , "" );
        put( "password_login_default"   , "" );
        put( "pembulatan_rupiah"        , "100" );
        put( "barcode_delay"            , "100" );
        put( "panjang_barcode_masuk_history_tambah_barang" , "1000" );
        put( "direktori_gambar"         , "img/file_gambar/" );
        put( "font_di_tabel_transaksi"  , "Lucida Sans Unicode" );
        put( "font_size_di_tabel_transaksi" , "14" );
        put( "font_bold_di_tabel_transaksi" , "ya" );
        put( "font_italic_di_tabel_transaksi" , "tidak" );
        put( "tinggi_baris_di_tabel_transaksi" , "" );
        put( "lebar_kode_di_tabel_transaksi" , "" );
        put( "file_konfigurasi"         , retail.file_konfigurasi );
        put( "aktifkan_suara"           , "ya" );
        put( "ppn_aktif_secara_default" , "tidak" );
        put( "ppn_ditampilkan"          , "ya" );
    }};

    protected void read_cfg() {
        File file = new File( cfg.get("file_konfigurasi") );
        if( !file.exists() ) {
            try {
                retail.show_error( "File Konfigurasi \"" +file.getCanonicalPath()+ "\" tidak ditemukan!\nMohon klik tombol \"Setting\"", "Konfigurasi File" );
                //ga ngaruh >> JOptionPane.showMessageDialog(retail.f, "File Konfigurasi \"" +file.getCanonicalPath()+ "\" tidak ditemukan!\nMohon klik tombol \"Database\"", "Konfigurasi File", JOptionPane.ERROR_MESSAGE, retail.icon_exit );
            } catch (Exception e) {}
            return;
        }
        String line = null;
        int pos;
        String var   = "";
        String value = "";
        try {
            BufferedReader br = new BufferedReader( new FileReader(file) );
            while ((line = br.readLine()) != null) {
                line = line.trim();    //.replaceAll("[ \x0B\f\r]","")
                //escape character
                if( line.indexOf("//") == 0 ) continue;
                if( line.indexOf("#" ) == 0 ) continue;
                //trim right escape character hanya jika ada spasi di depannya:p, otherwise, ia bagian dari data
                pos = line.indexOf(" //"); if( pos >= 0 ) line = line.substring(0, pos).trim();
                pos = line.indexOf(" #" ); if( pos >= 0 ) line = line.substring(0, pos).trim();

                pos = line.indexOf(":"); //the data delimiter
                if( pos <= 0 ) continue;
                var   = line.substring(0, pos).trim().toLowerCase().replace(" ","_");//get the identifier found on the left
                value = line.substring(pos+1,line.length()).trim();  //ini sama tapi biar umum aja sama dgn other languages:D >> line.substring(pos+1).trim();
                if( var.indexOf("password")>=0 ) {
                    value = value.substring(1,value.length()) + System.getProperty("line.separator");    //"\n";    //hasil dari encryptku dianggap newline oleh android!!    //enclosed with '"' agar tab ga ketrim :p
                }
                //?? tp ini ga bisa utk deteksi hak akses customer terhadap fitur ppn krn read_cfg tuh sebelum baca db:p >> if( retail.hak_fitur???.indexOf("'blabla'") >= 0 ) {

                cfg.put( var, value );   //switch(var) //(only) In the JDK 7 release, you can use a String object in the expression of a switch statement:
//retail.show_error( "var=" + var + "   value=" + value, "Konfigurasi File" );
            }
            br.close();
            close();    //to force reset when user change connection string :p
        } catch (Exception e) {
            err_msg += "Pembacaan File '" +cfg.get("file_konfigurasi")+ "' Gagal!\n" + e.getMessage() ;
        //} finally { br.close();
        }
    }

    //Boolean timed_out;
    protected void open() {
//        in_progress=true;
    new Thread(new Runnable() { public void run() { while ( !Thread.interrupted() ) {
        try { Thread.sleep(100); } catch (InterruptedException e1) {}
        open_thread();
        break;    //return;
    }}}).start();
    }


    //Boolean timed_out;
    protected void open_thread() {
        //read_cfg();
        try {
            //just retry a blank passwd:p
            //String url = "jdbc:mysql://localhost:3306/myDatabaseName";
//            if( con == null || con.isClosed() ) con = DriverManager.getConnection( "jdbc:" +cfg.get("dbms")+ "://" +cfg.get("host").replace("localhost","127.0.0.1")+ ":" +cfg.get("socket")+ "/" +"mysql", cfg.get("user"), "" );  //    //tanpa jdbcCompliantTruncation=false ... muncul warning truncate dari db saat integer diisi "":p
//android.util.Log.e("open: ", " 1 in_progress=" + in_progress );
            if( con == null || con.isClosed() ) con = DriverManager.getConnection( "jdbc:" +cfg.get("dbms")+ "://" +cfg.get("host")+ ":" +cfg.get("socket")+ "/" +cfg.get("database") + "?jdbcCompliantTruncation=false&user=" +cfg.get("user")+ "&password=" +decrypt( cfg.get("password"), encryptionKey ) );  //    //tanpa jdbcCompliantTruncation=false ... muncul warning truncate dari db saat integer diisi "":p

//android.util.Log.e("open: ", " 2");
//            if( con == null || con.isClosed() ) con = DriverManager.getConnection( "jdbc:" +cfg.get("dbms")+ "://" +cfg.get("host").replace("localhost","127.0.0.1")+ ":" +cfg.get("socket")+ "/" +cfg.get("database") + "?jdbcCompliantTruncation=false&user=" +cfg.get("user")+ "&useUnicode=true&characterEncoding=UTF-8" );  //    //tanpa jdbcCompliantTruncation=false ... muncul warning truncate dari db saat integer diisi "":p
//            if( con == null || con.isClosed() ) con = DriverManager.getConnection( "jdbc:" +cfg.get("dbms")+ "://" +cfg.get("host").replace("localhost","127.0.0.1")+ ":" +cfg.get("socket")+ "/" +cfg.get("database") + "?jdbcCompliantTruncation=false&user=" +cfg.get("user")+ "&password=adminadmin"+ "&useUnicode=true&characterEncoding=UTF-8" );  //    //tanpa jdbcCompliantTruncation=false ... muncul warning truncate dari db saat integer diisi "":p

              /* Can't create handler inside thread that has not called Looper.prepare()
              timed_out=false;
              android.os.CountDownTimer timer_timed_out = new android.os.CountDownTimer(9000, 9000) {
                  public void onFinish() {
                      timed_out=true;
                  }
                  public void onTick( long millisUntilFinished ) {}
              };    timer_timed_out.start();
              while( con != null && !con.isClosed() || timed_out ) {    timer_timed_out.cancel();    return;    }

              */

              //break;

        } catch( Exception e ) {
//android.util.Log.e("open error: ", e.getMessage());
            err_msg += "Koneksi ke Database Gagal!\n";
            if( e.getMessage().indexOf("Access denied") >= 0) err_msg += "Mohon klik tombol \"Setting\" dan lakukan \"Test Koneksi\"!\n\n\n";
            else if( e.getMessage().indexOf("Unknown database") >= 0 /*|| e.getMessage().indexOf("The last packet sent successfully to the server was 0 milliseconds ago") >= 0*/ ) {    //ga jadi, ternyata ga boleh ada skip_networking di my.ini  >> di android x86 ku pesannya bukan unknown db, so terpaksa biarin dia loop sampe timeout sendiri :(

String debug="";
/*
                try {
                    in_progress += 1000;    //in_progress++;    //block exec thread
                    int thread_level = in_progress;
                    con = DriverManager.getConnection( "jdbc:" +cfg.get("dbms")+ "://" +cfg.get("host")+ ":" +cfg.get("socket")+ "/" +"mysql"+ "?jdbcCompliantTruncation=false&user=" +cfg.get("user")+ "&password=" +decrypt( cfg.get("password"), encryptionKey ) );  //connect to (any existing) mysql database
                    retail.load_sql_file("db_init.sql") ;
                    con.close();
                    open_thread();    //back to normal (retail) database
                    in_progress -= 1001;    //if( in_progress>=thread_level ) in_progress--;    //release exec thread
                } catch( Exception es ) {
                    err_msg += "from load sql:'" + es.getMessage() + "' debug=" + debug;
                    es.printStackTrace();
                }
*/

                return;
            } else
                err_msg += "Pastikan server database sudah dijalankan!\n\n\n";
//
            err_msg += e;

//android.util.Log.e("err_msg: ", err_msg);

            if( con != null ) close(); //if( !con.isClosed() ) con.close();  // dispose the connection to avoid connections leak
        }

//        in_progress=false;

    }

    public void re_exec( String sql, String[] parameters, Boolean keep_open, Boolean to_table, int thread_level ) {
        last_sql = "";
        exec( sql, parameters, keep_open, to_table, thread_level );
    }
    public void re_exec( String sql, String[] parameters, Boolean keep_open, Boolean to_table ) {
        re_exec( sql, parameters, keep_open, to_table, 0 );
    }

    public void exec( String sql, String[] parameters, Boolean keep_open, Boolean to_table ) {
        exec( sql, parameters, keep_open, to_table, 0 );
    }

    public void exec( final String final_sql, final String[] parameters, final Boolean keep_open, final Boolean to_table, int thread_level ) {
//android.util.Log.e("exec: ", " 1 thread_level=" + thread_level + "  in_progress=" + in_progress + "    "+ final_sql);
        if( thread_level>in_progress )
            in_progress++;    //set new thread_level
        else
            try { while( thread_level<in_progress ) Thread.sleep(100); } catch (InterruptedException e1) {}
//android.util.Log.e("exec: ", " 2 thread_level=" + thread_level + "  in_progress=" + in_progress + "    " );
        in_progress++;    //in_progress=true;
        err_msg = ""; //need to reset
        Boolean need_to_connect = con == null;
        if( !need_to_connect ) try {   need_to_connect = con.isClosed();  } catch( SQLException e1) {}
        if( need_to_connect ) {

                    open();


        try {
            Boolean connected=false;    //other way: using thread.isAlive() is not safe :p
            for( int i=0; i<1000; i++ ) {
                  Thread.sleep(100);
                  if( con != null && !con.isClosed() && in_progress<1000 ) {    connected=true; break;    }
            }

            if( !connected ) {    //while( con == null || con.isClosed() ) {
                Thread.sleep(100);
                if( err_msg.length()>0 ) {
                    /*if( in_progress>0 )*/ in_progress--;
                    return;
                }
            }
        } catch (InterruptedException e1) {} catch( SQLException e1) {}



        } //else

    //mestinya hanya saat koneksi ke db sehingga err_msg bisa ditangkap dari thread utama >> new Thread(new Runnable() { public void run() { while ( !Thread.interrupted() ) {
                     exec_sync( final_sql, parameters, keep_open, to_table );
                     //break;
    //}}}).start();

    }


    public void exec_sync( final String final_sql, final String[] parameters, final Boolean keep_open, final Boolean to_table ) {
//android.util.Log.e("sync: ", " 1   in_progress=" + in_progress + "    "+ final_sql );

//        in_progress=true;

/*
        err_msg = ""; //need to reset
        //gak perlu:p Justru cmdp sebaiknya live >> cmd = null; cmdp = null;
        open();
*/

//    try { while( con == null || con.isClosed() ) { /*try { Thread.sleep(100); } catch (InterruptedException e1) {} */} } catch (Exception e1) {}

        //in_progress=true;
//    new Thread(new Runnable() { public void run() { while ( !Thread.interrupted()) {
        String sql = final_sql;
        if( !last_sql.equals(sql) )
            try {
//android.util.Log.e("sync: ", " 2  in_progress=" + in_progress + "    "+ final_sql );
                    sql = sql.replace("  "," ");    //biar ngirit karakter yg dikirim ke server ... haha
                    if( parameters == null                                   ) cmd  = con.createStatement();
                    else if( sql.trim().toUpperCase().indexOf("INSERT") == 0 ) cmdp = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //ini jg bisa: con.prepareStatement(sql, new String[]{"id"});  //to get last inserted id
                    else                                                       cmdp = con.prepareStatement(sql);

            } catch (Exception e) {
            }

        last_sql = sql;
//android.util.Log.e("sync: ", " 3" + final_sql );

        if( sql.trim().toUpperCase().indexOf("SELECT") == 0 || sql.trim().toUpperCase().indexOf("CHECK") == 0 ) exec_read  ( sql, parameters, keep_open, to_table );  //hehhee
        else                                                  exec_update( sql, parameters, keep_open );  //hehhee
//android.util.Log.e("sync: ", " 4   in_progress=" + in_progress + "    "+ final_sql );

        if( err_msg.indexOf("is longer than the server configured value of 'wait_timeout'") >= 0 ) {
            re_exec( sql, parameters, keep_open, to_table );
        }


//well, i use connected resultset now rather than cachedrowset ... then make sure u close it on all classes!!!. Anyway, sbaiknya kan ga sah di-con.close ya ... need to test nih >>       if( !keep_open ) close();

/*
        try {
            Thread.sleep(10);
        } catch (InterruptedException e1) {
            retail.show_error( e1.getMessage(), "Error dari exec()" );
        }
*/

        in_progress--;

//android.util.Log.e("sync: ", " 5   in_progress=" + in_progress + "    "+ final_sql );
//    }}}).start();
    }
    //overload method
    public void exec( String sql) {
        String[] parameters = null; Boolean keep_open = false; Boolean to_table = false;
        exec(sql, parameters, keep_open, to_table);
    }
    //overload method
    public void exec( String sql, String[] parameters) {
        Boolean keep_open = false; Boolean to_table = false;
        exec(sql, parameters, keep_open, to_table);
    }
    //overload method
    public void exec( String sql, String[] parameters, Boolean keep_open ) {
        Boolean to_table = false;
        exec(sql, parameters, keep_open, to_table);
    }

    public void exec_read( final String sql, final String[] parameters, Boolean keep_open, Boolean to_table ) {
        //CachedRowSet does not exist in android, then keep using only ResultSet rs >> ResultSet rs_temp = null;
        rs = null;
        if( !err_msg.equals("") ) return;

        try {
            /* CachedRowSet does not exist in android, then keep using ResultSet
            //crs = new CachedRowSetImpl();      //warning: CachedRowSetImpl is internal proprietary API and may be removed in a future release
            // Create the class with class.forName to avoid importing from the unsupported com.sun packages.
            Class c = Class.forName( "com.sun.rowset.CachedRowSetImpl" );
            rs = (CachedRowSet) c.newInstance();

            //RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            //CachedRowSet crs = rowSetFactory.createCachedRowSetImpl();
            */

    new Thread(new Runnable() { public void run() { while ( !Thread.interrupted() ) {
            try { Thread.sleep(100); } catch (InterruptedException e1) {}
            try{
            if (parameters == null) {
                rs = cmd.executeQuery(sql);    //new my_ResultSet( (ResultSet) cmd.executeQuery(sql) );    //rs_temp = cmd.executeQuery(sql);
            } else {
                for (int i = 0; i < parameters.length; i++) cmdp.setString( i+1, parameters[i].trim() );
                rs = cmdp.executeQuery();    //new my_ResultSet( (ResultSet) cmdp.executeQuery() );    //rs_temp = cmdp.executeQuery();
            }

            } catch( SQLException e1) {
              err_msg += "\nData Gagal Dibaca!\n"
                      +  "\nPesan Error SQLException: " + e1//.getMessage()
                      +  "\nSQL: " + sql
                      ;
            }

            break;
    }}}).start();

        try {
            for( int i=0; i<600; i++ ) {
                  Thread.sleep(100);
                  if( rs != null || err_msg.length()>0 ) break;
//android.util.Log.e("read: ", " 5 i=" + i );
            }
        } catch (InterruptedException e1) {}
        Thread.sleep(100);

        if( err_msg.length()>0 ) return;

            //rs.populate ini bikin sqlexception (misalnya): Value '0000-00-00 00:00:00' can not be represent as java.sql.TimeStamp ... Jadi, harus diupdate dulu datanya:p, atau defaultnya harus CURRENT_TIMESTAMP:p
            //CachedRowSet does not exist in android, then keep using ResultSet >> rs.populate(rs_temp);  rs_temp.close();  rs_temp = null;

//well, i use connected resultset now rather than cachedrowset ... then make sure u close it on all classes!!!. Anyway, sbaiknya kan ga sah di-con.close ya ... need to test nih >>       if( !keep_open ) close();
            //if( !keep_open ) close();   //frees connection resources

            record_found = false;    //agar tetap kereset jika ini bukan show to_table
            if(to_table) {
                record_found = rs.next();
                show_table(false, rs, !record_found /*rs.size()==0*/ && col_type!=null && col_type.length>0 && col_type[0]!=0 );    //untested parameter kedua, gunanya agar tidak error saat view table kosong    //prepare data for viewing table
                rs.close();   //frees only the data on rs resources
            }
            last_sql_read = sql;    //utk direfresh:) sehingga form2 ga perlu menconstruct ulang sql lagi.
        } catch (Exception e) {
              err_msg += "\nData Gagal Dibaca!\n"
                      +  "\nPesan Error: " + e//.getMessage()
                      //+  "\nSQL: " + sql
                      ;
        }
    }

    public void show_table( Boolean async, ResultSet crs ) {    //my_ResultSet crs    //CachedRowSet crs
        show_table( async, crs, true );
    }
    public void show_table( ResultSet crs ) {    //my_ResultSet crs    //CachedRowSet crs
        show_table( crs, true );
    }

/*
later:  anyway, kayaknya saya harus hindari cachedrowset deh ... di ( gak yakin, kecuali utk mendapatkan rs.size() >> java maupun )android krn gak perlu ...
public class CachedRowSetFactory implements RowSetFactory {


    public CachedRowSet createCachedRowSet() throws SQLException {
        return new CachedRowSetImpl();
    }

    public FilteredRowSet createFilteredRowSet() throws SQLException {
        return null;
    }

    public JdbcRowSet createJdbcRowSet() throws SQLException {
        return null;
    }

    public JoinRowSet createJoinRowSet() throws SQLException {
        return null;
    }

    public WebRowSet createWebRowSet() throws SQLException {
        return null;
    }
}

And then used it like this:

CachedRowSet crs = new CachedRowSetFactory().createCachedRowSet();

*/




Boolean is_editable = false;
    public void show_table( ResultSet crs, Boolean init_new_row ) {
        show_table( true, crs, init_new_row );
    }
    public void show_table( final Boolean async, final ResultSet crs, final Boolean init_new_row ) {
        if( async )
            new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {
                show_table_sync( crs, init_new_row );
                return null;
            }}.execute();
        else
                show_table_sync( crs, init_new_row );
    }
    public void show_table_sync( ResultSet crs, Boolean init_new_row ) {    //my_ResultSet crs    //CachedRowSet crs
        //if( err_msg != "" ) return;
String debug="";
        try {
            int numberOfColumns = col_width.length;    //metaData.getColumnCount();
//debug+="1";    android.util.Log.e("show_table: ", debug);

            if( crs!=null ) metaData = crs.getMetaData();       //RowSetMetaData rsmd = (RowSetMetaData)crs.getMetaData();
            else { //don't contact the db, just create my own RowSetMetaData:)
//debug+="2";    android.util.Log.e("show_table: ", debug);
                RowSetMetaData md = new RowSetMetaDataImpl();
                md.setColumnCount( numberOfColumns );
                for( int i=0; i<numberOfColumns; i++ ) {
                    md.setColumnName (i+1, col_label[i]);
                    md.setColumnLabel(i+1, col_label[i]);
                    md.setColumnType (i+1, col_type[i]);
                    md.setTableName  (i+1, "no_db");
                    md.setPrecision  (i+1, col_precision[i]);
                    md.setSigned     (i+1, false);
                }
                metaData = (ResultSetMetaData) md;
            }
//debug+="3";    android.util.Log.e("show_table: ", debug);

            table_name = metaData.getTableName(1);    //supaya ga harus pake try{} mulu...
//debug+="4";    android.util.Log.e("show_table: ", debug);

            columnNames = new String[numberOfColumns];  //set column label
//debug+="5";    android.util.Log.e("show_table: ", debug);
            isSigned    = new Boolean[numberOfColumns];  //set column isSigned. Supaya ga harus pake try{} mulu...
//debug+="6";    android.util.Log.e("show_table: ", debug);
            is_editable = false;
            for( int i=0; i<numberOfColumns; i++ ) {
                columnNames[i] = metaData.getColumnLabel(i+1);
                isSigned[i]    = metaData.isSigned(i+1);
                if( !is_editable ) is_editable = col_width[i]>0;
            }

            init_new_row = is_editable;

//debug+="7";    android.util.Log.e("show_table: ", debug);
            rows = new ArrayList<ArrayList>();    rows_backup = new ArrayList<ArrayList>();
//debug+="8";    android.util.Log.e("show_table: ", debug);
            if( crs==null || !record_found /*crs.size()==0*/ && init_new_row ) {    //jika crs.size()==0 && !init_new_row, ini berarti lupa ngeset col_type, jadi biar masuk ke else
//debug+="9";    android.util.Log.e("show_table: ", debug);
                //if(init_new_row) addRow();  // just add new blank row
//debug+="10";    android.util.Log.e("show_table: ", debug);
            } else {                        // Get all rows.
//debug+="11";    android.util.Log.e("show_table: ", debug);
                Object[] rs_row = new Object[numberOfColumns];
//debug+="12";    android.util.Log.e("show_table: ", debug);
                col_type        = new int[numberOfColumns];
                do {    //sudah di .next() di depan krn ga bisa cek crs.size() :p    >>while( crs.next() ) {
//debug+="crs";    android.util.Log.e("show_table: ", debug);
                    for( int i=0; i<numberOfColumns; i++ ) {  //klo ga gini, kebanyakan try{} ntar
                        if( record_found ) rs_row[i] = crs.getObject(i+1);
                        if(col_type[i]==0) col_type[i] = metaData.getColumnType(i+1);
                    }
                    if( record_found ) addRow(false,rs_row,col_type);
                } while( crs.next() ) ;
            }

            if(init_new_row) addRow(false);  // just add new blank row
//debug+="13";    android.util.Log.e("show_table: ", debug + "  init_new_row=" + init_new_row );

            //di constructor JTable aja karena adapter belum dicreate di sini >>if( adapter!=null ) adapter.notifyDataSetChanged();    //fireTableChanged(null); // Tell the listeners a new table has arrived.

//debug+="14";    android.util.Log.e("show_table: ", debug);


        } catch (Exception e) {
              err_msg += "\nProses penampilan tabel gagal!\n"
                      +  "\nPesan Error: " + e + debug//.getMessage()
                      ;


android.util.Log.e( "show_table: ", err_msg);

        }
    }

Boolean thread_finished;
    public void exec_update( final String sql, final String[] parameters, final Boolean keep_open ) {

    thread_finished=false;
    new Thread(new Runnable() { public void run() { while ( !Thread.interrupted() ) {

        try {
            Thread.sleep(100);
            affected = 0;
            if( err_msg != "" ) return;
            if( parameters == null ) {
                if( sql.indexOf(";\n") > 0 ) {    //execute batch sql
                    for( String sqlb : sql.split(";\n") ) if( !sqlb.trim().equals("") ) cmd.addBatch(sqlb);
                    int[] returnb = cmd.executeBatch();    //hati2 .. dia tetap lanjut walau error :p :)   //anyway, ini tetap tidak atomic:) unless u apply START TRANSACTION with innodb
                    affected = returnb[ returnb.length-1 ];
                    //for (int code : returnb) err_msg += code + ", batch   ";
                    //static int EXECUTE_FAILED >> The constant indicating that an error occured while executing a batch statement.
                } else
                    affected = cmd.executeUpdate(sql);
            } else {
                Boolean insert_query = sql.trim().toUpperCase().indexOf("INSERT") == 0 ;
                for (int i = 0; i < parameters.length; i++) {  //if (parameters[i] == "NULL") cmd.Parameters.Add(new SqlParameter("@param" + (i + 1), DBNull.Value)); else 
                    if( insert_query && retail.is_number( parameters[i] ) ) parameters[i] = parameters[i].replace( retail.digit_separator, "" );    //bonus untuk Ftambah:)    //if( col_type[i]==Types.INTEGER || col_type[i]==Types.SMALLINT || col_type[i]==Types.BIGINT )
                    cmdp.setString( i+1, parameters[i].trim() ); //cmd.Parameters.Add(new SqlParameter("@param" + (i + 1), parameters[i].Trim().Replace("\n", " "))); //Replace is for text area //Replace("\r", "").Replace("\n", "<br/>")
                }
                affected = cmdp.executeUpdate();
                if(insert_query) {    //get last inserted id
                    last_inserted_id = -100;
                    ResultSet keys = cmdp.getGeneratedKeys();
                    if( keys.next() ) last_inserted_id = keys.getInt(1);    //if( keys.getFetchSize()>0 )
                    //from mysql manual: For the benefit of some ODBC applications (at least Delphi and Access), the following query can be used to find a newly inserted row:
                    //SELECT * FROM tbl_name WHERE autoincrement_column IS NULL;
                    //and this maybe useful: LAST_INSERT_ID() now returns 0 if the last INSERT statement didn't insert any rows.
                }
            }
                                                           //supaya tidak muncul pesan error "tidak ada data yg bisa diupdate" saat ALTER TABLE jual_faktur AUTO_INCREMENT sebelumnya sudah benar :D
            if( affected <= 0 && sql.toUpperCase().indexOf("ALTER TABLE ") < 0  && sql.toUpperCase().indexOf("SET ")!=0 ) err_msg += "\nMaaf, tidak ada data yang bisa diupdate!\n\n\n\n\n";

            String table_name = sql.toLowerCase().indexOf("into barang ") >= 0 ? "barang" : sql.toLowerCase().indexOf("into pelanggan ") >= 0 ? "pelanggan" : sql.toLowerCase().indexOf("into supplier ") >= 0 ? "supplier" : "" ;
            if( table_name.equals("") ) table_name = sql.toLowerCase().indexOf("update barang ") >= 0 && sql.toLowerCase().indexOf("set stok") < 0 ? "barang" : sql.toLowerCase().indexOf("update pelanggan ") >= 0 && sql.toLowerCase().indexOf("set poin") < 0? "pelanggan" : sql.toLowerCase().indexOf("update supplier ") >= 0 ? "supplier" : "" ;
            if( !table_name.equals("") && retail.setting.get("Auto Refresh Delay Item & Barang Di Transaksi") != null ) {
                Boolean cmd_is_null = cmd==null || ( BuildConfig.VERSION_CODE < 9 ? false : cmd.isClosed() ) ;
                if( cmd_is_null ) cmd  = con.createStatement();
                cmd.executeUpdate( "REPLACE INTO last_modified (table_name) VALUES ('" +table_name+ "')" );
                if( cmd_is_null ) cmd.close();
            }
        } catch( Exception e ) {
            if( e.getMessage().indexOf("Duplicate entry") >= 0) err_msg += "\nMaaf, ada duplikasi data!\nIsian " + e.getMessage().substring( e.getMessage().indexOf("'"), e.getMessage().lastIndexOf("'")+1 ) + " sudah ada.\nMohon perbaiki isian Saudara!\n\n\n\n\n";
            else                                                err_msg += "\nMaaf, data gagal diupdate!\nPesan kesalahan: " + e.getMessage() + ".\nTolong Hubungi Administrator!\n\n\n\n\n";
        }
        //if( affected <= 0 ) err_msg += "\nMaaf, Tidak ada data yang bisa diupdate!\n\n\n\n\n";

            thread_finished=true;
            break;
    }}}).start();

        try {
            for( int i=0; i<6000; i++ ) {
                  Thread.sleep(100);
                  if( thread_finished ) break;
            }
        } catch (InterruptedException e1) {}
    }

    protected void close( Boolean rs_close ) {
        try {
            //eh, jgn dengg:p >>> err_msg  = "";  //not thoroughly check yet:)
            last_sql = "";
            //last_sql_read = "";    //masih gue pakai saat refresh
            //err_msg += "debug: " + this.rs.size(); //while (rs.next()) { err_msg += "\nData : " + rs.getString(1); }
            if( rs_close ) if( rs != null ) rs.close();
            if( cmd != null) cmd.close();
            if( cmdp != null) cmdp.close();
            if( con != null) con.close();        //if( !con.isClosed() ) ;
        } catch (Exception e) {
            err_msg += "\nMaaf, Koneksi ke Database Gagal Ditutup!\n(" + e.getMessage() + ")";
        }
    }
    protected void close() {    //overload method, ga perlu rs_close
        close(false);
    }



  //encrypt doang take my whole day:( ... tau gitu mending diencode manual ajah :p
/* well, alternatively, java 8 !!! or .... 
import org.apache.commons.codec.binary.Base64;
    byte[] encodedBytes = Base64.encodeBase64("JavaTips.net".getBytes());
    System.out.println("encodedBytes " + new String(encodedBytes));
    byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
    System.out.println("decodedBytes " + new String(decodedBytes));
*/

  //static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
  String encrypt(char[] plainText, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    //Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    String newText = new String(plainText).trim();
    //for (int i = plainText.length; i < 16; i++) newText += " "; //padding:p

    //Class c = Class.forName( "sun.misc.BASE64Encoder" );
    //sun.misc.BASE64Encoder encoder = (sun.misc.BASE64Encoder) c.newInstance();
    return android.util.Base64.encodeToString( cipher.doFinal(newText.getBytes("UTF-8")), android.util.Base64.DEFAULT );    //is_android.. new sun.misc.BASE64Encoder().encodeBuffer( cipher.doFinal(newText.getBytes("UTF-8")) );
    //return URLEncoder.encode( new String( cipher.doFinal(newText.getBytes("UTF-8")), "UTF-8" ), "UTF-8" );
    //return new String( cipher.doFinal(newText.getBytes("UTF-8")), "UTF-8");

    //return Base64.encodeBase64String( cipher.doFinal(newText.getBytes("UTF-8")) );
    //return new String( cipher.doFinal(newText.getBytes("UTF-8")) );
    //return cipher.doFinal(newText.getBytes("UTF-8"));
    //return cipher.doFinal(plainText.getBytes("UTF-8"));
  }
 
  String decrypt(String cipherText, String encryptionKey) throws Exception{
  //private static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    //Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");  //AES/CBC/NoPadding
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    //byte[] newText = new byte[cipherText.length()];
    //for (int i = 0; i < cipherText.length(); i++) newText[i] = (byte)(cipherText.charAt(i));   //new byte(cipherText[i]);
                
    //Class c = Class.forName( "sun.misc.BASE64Decoder" );
    //sun.misc.BASE64Decoder decoder = (sun.misc.BASE64Decoder) c.newInstance();

    return new String(cipher.doFinal( android.util.Base64.decode( cipherText, android.util.Base64.DEFAULT ))) ;    //is_android.. new String(cipher.doFinal( new sun.misc.BASE64Decoder().decodeBuffer(cipherText)));

    //return new String( cipher.doFinal( URLDecoder.decode(cipherText, "UTF-8").getBytes("UTF-8")), "UTF-8");
    //return new String( cipher.doFinal( cipherText.getBytes("UTF-8") ), "UTF-8").trim();

//    return new String(cipher.doFinal(newText),"UTF-8").trim();

    //return new String(cipher.doFinal(Base64.decodeBase64(cipherText))).trim();
    //return new String(cipher.doFinal(cipherText.getBytes("UTF-8")),"UTF-8").trim();
    //return new String(cipher.doFinal(cipherText),"UTF-8");

  }




    //  Implementation of the TableModel Interface

    // MetaData
    public String getColumnName(int column) {
        String col_name = columnNames[column];
        if( col_name==null )            col_name = "";
        else                            col_name = retail.toTitleCase(col_name).replace(" Id", " ").replace("Code", "Kode").replace("Name", "Nama");  //.replace("_", " "); //.toUpperCase()
        if( col_name.indexOf("  ")>=0 ) col_name = "<html><center>" + col_name.replace("  ","<br>");    //di col_label harus ada "__" utk mewakili newline.
        return col_name;
    }

    public Class getColumnClass(int column) {    //ga bisa:  throws SQLException
        if( col_editor[column] != null ) if( col_editor[column] instanceof JCdb ) return String.class;   //supaya tidak right align :p

        switch( col_type[column] ) {
//? is_android ...
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return String.class;

            case Types.BIT:
                return Boolean.class;

            case Types.INTEGER:
               if( !isSigned[column] )  return Long.class;    //anyway bakal diset otomatis oleh javanya jg deh ....
            case Types.TINYINT:
            case Types.SMALLINT:
            //none>> case Types.MEDIUMINT:
                return Integer.class;

            case Types.BIGINT:
                return Long.class;

            case Types.FLOAT:
            case Types.DOUBLE:
                return Double.class;

            case Types.DATE:
                return java.sql.Date.class;

            case Types.TIME:
                return java.sql.Time.class;

            default:
                return Object.class;
        }
    }

    public boolean isCellEditable(int row, int column) {
        try {
            //JOptionPane.showMessageDialog( retail.f, "in celleditable", "Kesalahan", JOptionPane.ERROR_MESSAGE);// Some of the drivers seem buggy, table should not be null.
            return metaData.isWritable(column+1) && col_width[column]>0 ;  //if( table.getModel().getColumnName(column).toLowerCase().equals("id") 
        }
        catch (SQLException e) {
            return false;
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    // Data methods
    public int getRowCount() {
        return rows.size();
    }

/*
    public String getValue(int col_idx, Object val) {
        //if( columnNames[col_idx].toLowerCase().equals("diskon") && value.toString().trim().equals("%") ) value="";
        if( val==null) val="";
        String value = val.toString().trim();
        }
    }
*/

    public Object getValueAt(int row_idx, int col_idx) {
        // method getValueAt ini ternyata dipanggil oleh sorter .... dan error saat firetableinsertedrow selalu gagal krn ga bs casting to integer error gitu dehhh .... hm, ngabisin waktu gue ni seharian ... sekarang da jam 12.24 :(
        //return getValue(col_idx, ((Vector)rows.elementAt(row_idx)).elementAt(col_idx) );
        Object val = ((ArrayList<Object>)rows.get(row_idx)).get(col_idx);    //((Vector)rows.elementAt(row_idx)).elementAt(col_idx);
        if( val==null ) val="";
        return val;
    }
    public String to_str(int col_idx, Object val) {
        String value = val.toString().trim();
        if( col_editor[col_idx] instanceof JCdb )   return value;
        //di pattern_Filter aja deh :p  >> if( getColumnName(col_idx).equals("Kode") ) value = retail.ean13(value);
        switch( col_type[col_idx] ) {
            case Types.TINYINT : return value.equals("")?"0":Integer.valueOf(value).toString();
            case Types.INTEGER : return value.equals("")?"0":Integer.valueOf(value).toString();  //((Integer)value).toString();    //(new Integer(value.toString())).toString();    //Integer.valueOf( (Int)value );
            case Types.BIGINT  : return value.equals("")?"0":Integer.valueOf(value).toString();  //((Integer)value).toString();    //(new Integer(value.toString())).toString();    //Integer.valueOf( (Int)value );
            case Types.SMALLINT: return value.equals("")?"0":Integer.valueOf(value).toString();  //((Integer)value).toString();    //(new Integer(value.toString())).toString();    //Integer.valueOf( (Int)value );
            case Types.DOUBLE  : return Double.valueOf(value).toString();  //((Double)value).toString();
            case Types.FLOAT   : return Float.valueOf(value).toString();  //((Float)value).toString();
            case Types.BIT     : return Boolean.valueOf(value).toString();  //((Boolean)value).booleanValue() ? "1" : "0";
            case Types.DATE    : return value; // This will need some conversion.
            default            : return value;
        }
    }
    public String to_db(int col_idx, Object val) {
        //nop, i've used paramaterized val ... if (val == null) return "null";
        if( col_editor[col_idx] instanceof JCdb )    return ((Object)((JCdb)col_editor[col_idx]).my_key_of(val.toString())).toString();    //val = 1 + ((JCdb)col_editor[col_idx]).my_index_of(val.toString());
        else                                         return to_str(col_idx, val);
    }
    public int getIntAt(int row_idx, int col_idx) {
        return Integer.valueOf( getStringAt(row_idx, col_idx) );     //return Integer.valueOf( getValueAt(row_idx, col_idx).toString().replace( retail.digit_separator, "" ).replace("%","") );
    }
    public String getStringAt(int row_idx, int col_idx) {
        return getValueAt(row_idx, col_idx).toString();
    }

    private String where_clause = "";
    private String[] params;
    private String table;
    public void construct_sql_clause( String new_val, int row_idx, int skip_col_idx ) { //construct where_clause and params
        try {
            if( table_name==null ) retail.show_error( "\nNama Table \"null\"!", "Kesalahan" );// Some of the drivers seem buggy, table should not be null.
            int set_count = new_val==null?0:1 ;
            String id = "";
            String[] params_all = new String[ getColumnCount() + set_count - ( skip_col_idx>=0 ? 1 : 0 ) ];
            params_all[0] = new_val;  //for the SET sql parameter
            where_clause = "";
            for( int c=0; c<getColumnCount(); c++ ) {
                if( c==skip_col_idx ) {
                    set_count -- ;  //agar index params_all tidak lompat...
                    continue;
                }
                if( (columnNames[c]).trim().equals("") ) continue;
                if( !where_clause.equals("") ) where_clause += " AND ";
                params_all[c+set_count] = to_db(c, getValueAt(row_idx, c));
                if( (columnNames[c]).toLowerCase().equals("id") && !params_all[c+set_count].equals("0") ) {  //we found the primary key which was not 0 (was inserted)!!!
                    id = params_all[c+set_count];
                    where_clause = " id = ? ";
                    break;
                }
                where_clause += " " + columnNames[c] + " = ?";    //ada spasi di depan agar replace di if(need_insert) di bawah bekerja dgn tepat .... //if( !id.equals("") ) break;
            }
            //JOptionPane.showMessageDialog( retail.f, "  where_clause= " + where_clause + "\n params_all.length=" + params_all.length + "     params_all[0]=" + params_all[0] +  "   set='" + new_val +  "'   WHERE id=" + id +  "params_all[1]=" + params_all[1], "Kesalahan", JOptionPane.ERROR_MESSAGE);
            params = null;
                 if( id.equals("") ) params = params_all;
            else if( new_val==null ) params = new String[] { id };
            else                     params = new String[] { new_val, id };
        } catch (Exception e) {
            retail.show_error( "\nPengolahan Database Gagal\nJenis Kesalahan: " + e.getMessage(), "Kesalahan" );
        }
    }


    public void setValueAt(Object val, int row_idx, int col_idx) {
        setValueAt( true, val, row_idx, col_idx );
    }
    public void setValueAt(final Boolean async, final Object val, final int row_idx, final int col_idx) {
        if( !table_name.equals("no_db") )    //goblok gue:p >> if( async )
            new android.os.AsyncTask<Void, Void, Integer> () {
                @Override protected Integer doInBackground( Void... v ) {
                    return new Integer( setValueAt_sync( true, val, row_idx, col_idx ) );
                }
                @Override protected void onPostExecute( Integer notify ) {
android.util.Log.e("setvalue: ", " notify=" + notify );
                    if( notify==2 ) {
                        adapter.notifyItemChanged( row_idx );
                        new android.os.Handler().post(new Runnable() { @Override public void run() {
                            addRow(/*false,*/getRowCount(),true) ;    //if( async ) addRow(); else addRow(false,getRowCount(),true) ;    //is_android ...   untested: ini agar selalu ada satu baris terakhir yg disiapkan utk user nambah data
                        }});
                    } else if( notify==1 )
                        if( jtable.findViewHolderForLayoutPosition(row_idx)==null ) adapter.notifyItemChanged(row_idx);    //payload tak dijamin diload:p >> 
                        else                                                        adapter.notifyItemChanged(row_idx, new Integer(col_idx));    //is_android ...    //payload kadang2 tidak kerja :p!!!    fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)
                }
            }.execute();
        else {
            final int notify = setValueAt_sync( false, val, row_idx, col_idx );
            if( notify==0 ) return;
            new android.os.Handler().post(new Runnable() { @Override public void run() {
                //if( async )                   adapter.notifyItemChanged( row_idx, new Integer(col_idx) );    //is_android ...    //payload kadang2 tidak kerja :p!!!    fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)
android.util.Log.e("setvalue: ", " notify=" + notify );
                    if( notify==2 ) {
                        adapter.notifyItemChanged( row_idx );
                        new android.os.Handler().post(new Runnable() { @Override public void run() {
                            addRow(/*false,*/getRowCount(),true) ;    //if( async ) addRow(); else addRow(false,getRowCount(),true) ;    //is_android ...   untested: ini agar selalu ada satu baris terakhir yg disiapkan utk user nambah data
                        }});
                    } else if( notify==1 )
                        //if( jtable.findViewHolderForLayoutPosition(row_idx)==null )
 adapter.notifyItemChanged(row_idx);    //payload tak dijamin diload:p >> 
                        //else                                                        adapter.notifyItemChanged(row_idx, new Integer(col_idx));    //is_android ...    //payload kadang2 tidak kerja :p!!!    fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)
            }});
        }
    }

    public int setValueAt_sync(final Boolean async, Object val, final int row_idx, final int col_idx) {
        String value = to_str( col_idx, val );    //JOptionPane.showMessageDialog( retail.f, "in setval value=" + to_str(col_idx,value) + " getvalue= " + getValueAt(row_idx, col_idx), "Kesalahan", JOptionPane.ERROR_MESSAGE);
                                                  //agak lucu kan pake ngecek col_editor[] dulu:D >> hm di edit hak akses modul jadi ga bisa nyimpan krn setvalue mendetect perubahan value ....
        Boolean val_tetap = to_str( col_idx, getValueAt(row_idx, col_idx) ).equals(value);
        //pucing:0 >> Boolean all_are_foreign = col_editor[0] instanceof JComboBox && col_editor[1] instanceof JComboBox;
        if( val_tetap ) return 0;    //pucing:0 >> if( val_tetap && !all_are_foreign ) return 0;    //berat saat loop JCdb:p >> if( to_db( col_idx, getValueAt(row_idx, col_idx).toString().trim() ).equals( to_db(col_idx,value) ) ) return 0;
        String id = getValueAt(row_idx, 0).toString();
        Boolean need_insert = id.equals("0") || id.equals("");   //id value sounds like need to insert :)  //tp sebetulnya ini masi bermasalah klo ada unique key2 yg dibiarkan default oleh user .... akibatnya ga akan bisa insert lagi:D
        if( !table_name.equals("no_db") ) {    //otherwise ... do ur own db executing:)    //JOptionPane.showMessageDialog( retail.f, "INSIDE setcalue to_db( col_idx, getValueAt(row_idx, col_idx)= " + to_db( col_idx, getValueAt(row_idx, col_idx).toString().trim() ) + "   to_db(col_idx, value) = " + to_db(col_idx, value), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            String sql="";
            //pucing:0 >> if( all_are_foreign ) need_insert = val_tetap && col_editor[0].getSelectedIndex()==0 && col_editor[1].getSelectedIndex()==0;    //jika val_tetap masuk sini, berati dia all_are_foreign
            if( need_insert ) {   //pucing:0 >> //edit hak akses modul perlu lakukan insert jika user milih isian default
                construct_sql_clause( to_db(col_idx, val), row_idx, col_idx );
                sql = " INSERT INTO " + table_name
                            + " SET " + columnNames[col_idx] + " = ?,"
                            + where_clause.replace(" AND ", ", ").replace(" id = ", " id = (SELECT id FROM (SELECT IFNULL( MAX(id), 0) + 1 AS id FROM " +table_name+ ") p ) + '" +params[1]+ "' - " );
                /* executedbatch yg gue bikin harus tanpa param:)
                if( col_editor[0] instanceof JComboBox && col_editor[1] instanceof JComboBox ) {
                    String[] params_insert = params;
                    params = new String[ params_update.length + params_insert.length ];
                    System.arraycopy( params_update, 0, params, 0, params_update.length );
                    System.arraycopy( params_insert, 0, params, params_update.length, params_insert.length );
                    sql = sql.replace("INSERT INTO", " ;\n INSERT IGNORE INTO").replace(" UPDATE ", " UPDATE IGNORE ");    //|| ( col_editor[0] instanceof JComboBox && col_editor[1] instanceof JComboBox && col_editor[0].getSelectedIndex()==0 && col_editor[1].getSelectedIndex()==0 
                }
                */
            } else {
                construct_sql_clause( to_db(col_idx, val), row_idx, -1 );
                sql = " UPDATE " + table_name
                           + " SET " + columnNames[col_idx] + " = ? "
                           + " WHERE " + where_clause
                           + " LIMIT 1";
            }
                //JOptionPane.showMessageDialog( retail.f, sql+ "\nparams[0]=" + params[0]+ "\nparams[1]=" + params[1], "Kesalahan", JOptionPane.ERROR_MESSAGE);
            exec(sql, params, true);
            try { while( in_progress>0 ) Thread.sleep(100); } catch (InterruptedException e1) {}
            if( err_msg != "" ) {
// is_android ...
                if( err_msg.indexOf("duplikasi data") >= 0 && col_editor[0] instanceof android.widget.Spinner && col_editor[1] instanceof android.widget.Spinner ) {    //tangani manual, hahahaha
                    String keys = ( col_idx==0 ? value + " - " + to_str( 1, getValueAt(row_idx, 1) ) : to_str( 0, getValueAt(row_idx, 0) ) + " - " + value ) ;
                    err_msg = err_msg.substring( 0, err_msg.indexOf("'")+1 ) + keys + err_msg.substring( err_msg.lastIndexOf("'"), err_msg.length() ) ;
                }
                retail.show_error( err_msg, "Kesalahan" );
                return 0;
            }
//is_android ...
            if( need_insert && !(col_editor[0] instanceof android.widget.Spinner) ) ((ArrayList<Object>)rows.get(row_idx)).set(0,last_inserted_id);   //id value sounds like was just inserted :)
        }    //from  if( !table_name.equals("no_db") ) {    //otherwise ... do ur own db executing:)

// is_android ...
        if( enable_filter ) {
            int row_idx_backup = (int)((ArrayList<Object>)rows.get(row_idx)).get( col_width.length );
            ((ArrayList<Object>)rows_backup.get(row_idx_backup)).set(col_idx,value);    //then rows can be filtered :p and the last_column will be the mapped rows_backup.row_idx to modify it in setValueAt() and remove()
        }
        ((ArrayList<Object>)rows.get(row_idx)).set(col_idx,value);
        //adapter.notifyItemChanged( row_idx, Arrays.asList(col_idx) );    //is_android ...        fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)
        //adapter.notifyItemChanged( row_idx, (List<Object>)Arrays.asList( new Integer(col_idx) ) );    //is_android ...        fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)


android.util.Log.e("fireupdate: ", need_insert + " <<need_insert ,  value=" + value + " row_idx=" + row_idx + "  col_idx" + col_idx );

/* solusi: bind method saya masukkan ke dalam handler (thread) baru << aneh, mungkin karena dipanggil diasyncthread, notify bikin bind jalan tapi bisa diinterrupt oleh log.e,  lihat aja:
        if( async )            adapter.notifyItemChanged( row_idx, new Integer(col_idx) );    //is_android ...    //payload kadang2 tidak kerja :p!!!    fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)
        else if( need_insert ) adapter.notifyItemChanged( row_idx );

E/bind first:(28088): 0
E/bind:   (28088): row
E/bind:   (28088): col_idx=0
E/fireupdate: (28088): true <<need_insert ,  value=8992388121038 row_idx=0  col_
idx0
E/bind:   (28088): row last
E/bind lasttt:(28088): 0

*/
        final Boolean backup_need_insert = need_insert;

        if( need_insert ) {    //perlu dicek lagi apa id sudah terset (kasus di penjualan, mungkin aja user hanya ngeset kolom banyak doang)
            id = getValueAt(row_idx, 0).toString();
            need_insert = /*untested col_idx==0 ||*/ !id.equals("0") && !id.equals("");
        }

if( need_insert ) android.util.Log.e("needinsert: ", "  addrow from setvalue ");

        final Boolean backup_need_insert_last = need_insert;


//        new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {
//        if( backup_need_insert_last ) addRow(/*false,*/getRowCount(),true) ;    //if( async ) addRow(); else addRow(false,getRowCount(),true) ;    //is_android ...   untested: ini agar selalu ada satu baris terakhir yg disiapkan utk user nambah data

        return ( backup_need_insert_last ? 2 : 1 );
/*
        new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {

        //if( async )                   adapter.notifyItemChanged( row_idx, new Integer(col_idx) );    //is_android ...    //payload kadang2 tidak kerja :p!!!    fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)
        //else if( backup_need_insert ) adapter.notifyItemChanged( row_idx );
        //if( backup_need_insert ) adapter.notifyItemChanged( row_idx );
        //else                     adapter.notifyItemChanged( row_idx, new Integer(col_idx) );    //is_android ...    //payload kadang2 tidak kerja :p!!!    fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)
        adapter.notifyItemChanged( row_idx );    //is_android ...    //payload kadang2 tidak kerja :p!!!    fireTableCellUpdated(row_idx, col_idx);   //memang diperlukan :)

        }},200);

*/

//        }},200);


    }



    public void removeRow(int row_idx) {
        removeRow( true, row_idx );
    }
    public void removeRow(final Boolean async, final int row_idx) {
        if( async )
            new android.os.AsyncTask<Void, Void, Void> () {
                @Override protected Void doInBackground( Void... v ) {
                    removeRow_sync(row_idx);
                    return null;
                }
                @Override protected void onPostExecute(Void v) {
                    if( err_msg != "" ) retail.show_error( "Gagal menghapus baris.\n" + err_msg, "Kesalahan" );
                    adapter.notifyItemRemoved(row_idx);
android.util.Log.e("removeRow", "last after notify in async");
                }
            }.execute();
        else {
            removeRow_sync(row_idx);
            if( err_msg != "" ) retail.show_error( "Gagal menghapus baris.\n" + err_msg, "Kesalahan" );
            new android.os.Handler().post(new Runnable() { @Override public void run() {
                adapter.notifyItemRemoved(row_idx);
android.util.Log.e("removeRow", "last after notify in !async");
            }});
        }

    }
    public void removeRow_sync(final int row_idx) {
        String id = getValueAt(row_idx, 0).toString();
android.util.Log.e("removeRow_sync: ", "1 id=" + id);
        if( !id.equals("0") && !id.equals("") && !table_name.equals("no_db") ) { //escape if only need to abort last inserted row or if this table is no_db // || row_idx!=rows.count()-1 
            construct_sql_clause(null, row_idx, -1);
            String sql = " DELETE FROM " +table_name
                       + " WHERE " + where_clause
                       + " LIMIT 1";
            exec(sql, params, true);
            try { while( in_progress>0 ) Thread.sleep(100); } catch (InterruptedException e1) {}
            if( err_msg != "" ) {
                //retail.show_error( err_msg, "Kesalahan" );
                return;
            }
        }
// is_android ...
android.util.Log.e("removeRow_sync: ", "2");
        if( enable_filter ) {
            int row_idx_backup = (int)((ArrayList<Object>)rows.get(row_idx)).get( col_width.length );
            ((ArrayList<Object>)rows_backup.get(row_idx_backup)).clear();    //ini ngerusak mapping indexnya :p >> rows_backup.remove(row_idx_backup);    //then rows can be filtered :p and the last_column will be the mapped rows_backup.row_idx to modify it in setValueAt() and remove()
        }
android.util.Log.e("removeRow_sync: ", "3");
        rows.remove(row_idx);
android.util.Log.e("removeRow_sync: ", "4");

        //                android.os.Looper.prepare();
        ///new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {
        //adapter.notifyItemRemoved(row_idx);    //fireTableRowsDeleted(row_idx, row_idx);    //belum refresh :p
        //}},200);
        //                android.os.Looper.loop();

        //fireTableDataChanged();  //ini masi ada sisa row di akhir jika sedang difilter...
        //fireTableChanged(new TableModelEvent(this));

/*
           for(int rowIndex = data.size() - 1; rowIndex >= 0; rowIndex--) {
        if(data.get(rowIndex).isSelect()) {
          data.remove(rowIndex); break;
         }
*/
    }


// is_android ...
    Table_adapter adapter;    android.support.v7.widget.RecyclerView jtable;

    public void set_table_adapter( Table_adapter adapter ) {    //supaya notify2update bisa dikirim balik ke sini:p
        this.adapter = adapter;
    }



    public void addRow(int row_idx, Object[] rs_row, int[] type, Boolean fireInserted ) {
        addRow( true, row_idx, rs_row, type, fireInserted );
    }
    public void addRow(final Boolean async, final int row_idx, final Object[] rs_row, final int[] type, final Boolean fireInserted ) {
        if( async )
            new android.os.AsyncTask<Void, Void, Void> () {
                @Override protected Void doInBackground( Void... v ) {
                    addRow_sync( row_idx, rs_row, type, fireInserted );
                    return null;
                }
                @Override protected void onPostExecute(Void v) {
                    if( fireInserted ) adapter.notifyItemInserted(row_idx);    //fireTableRowsInserted(row_idx, row_idx);    //jika ini error, cek table.getSelectionModel().addListSelectionListener    //fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
                }
            }.execute();
        else {
                addRow_sync( row_idx, rs_row, type, fireInserted );
//                if( fireInserted )
//                    new android.os.Handler().post(new Runnable() { @Override public void run() {
//                        adapter.notifyItemInserted(row_idx);    //fireTableRowsInserted(row_idx, row_idx);    //jika ini error, cek table.getSelectionModel().addListSelectionListener    //fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
//                    }});
        }
    }


// is_android end ...

    public void addRow_sync( final int row_idx, Object[] rs_row, int[] type, Boolean fireInserted ) {   //Boolean fireInserted mencegah fireTableRowsInserted saat load pertamakali krn sudah akan difireTableChanged
//ternyata debug ini dan (sepertinya) android.util.Log.e() bikin lambat secara significant pada record>20.000 >> String debug="";
//try{
//debug+="1";

// android.util.Log.e("addrow: ", "1");
        //if table.getSelectedRow() < table.getRowCount() -1  return;
        //untested: to save memory >> ArrayList<Object> newRow = new ArrayList<Object>();
        ArrayList newRow = new ArrayList(getColumnCount()+1);
        for( int i=0; i<getColumnCount(); i++ ) {  //ini jg bisa: metaData.getColumnCount()
//debug+="2";
	    Object obj = rs_row[i];
//debug+="3";
	    //int type_  = type[i];
            //what u will see not what u get:)
// is_android ... col_editor[i] saya set di JTable with if( db.col_editor[i]==null ) db.col_editor[i] = new EditText(my_activity);
            if( col_editor[i] != null && col_editor[i] instanceof JCdb    /*!(col_editor[i] instanceof android.widget.EditText) */  ) {
                if( col_editor[i] instanceof JCdb/*android.widget.Spinner*/ ) {
//debug+="4";

                    if( obj.toString().equals("") ) {
//debug+="4a";
                        obj = col_editor[0] != null && col_editor[0] instanceof JCdb ? 0 : 1;    //agak lucu kan pake ngecek col_editor[0] dulu:D >> //tapi tetap di hak akses modul itu bisa diisi kosong oleh user << lah, otherwise ... malah ngelock by duplikat error :p >> paksa foreignkey selalu terisi .. unfully checked :) .. hm di edit hak akses modul jadi ga bisa nyimpan krn setvalue mendetect perubahan value ....
//debug+="4b ";
//debug+="4b (Integer)obj=" + (Integer)obj + "   ((JCdb)col_editor[i]).getCount()=" + ((JCdb)col_editor[i]).getCount();
                        obj = ((JCdb)col_editor[i]).getItemAt( (Integer)obj - 1 );
                    } else
                        obj = ((JCdb)col_editor[i]).my_jcdb_item_of_key( (Integer)obj );    //kayaknya lambat deh >> obj = ((JCdb)col_editor[i]).my_item_of_key( Integer.valueOf(obj.toString()) );
//debug+="4c";

                    //if( obj.toString().equals("") ) obj = col_editor[0] instanceof JComboBox ? 0 : 1;    //agak lucu kan pake ngecek col_editor[0] dulu:D >> //tapi tetap di hak akses modul itu bisa diisi kosong oleh user << lah, otherwise ... malah ngelock by duplikat error :p >> paksa foreignkey selalu terisi .. unfully checked :) .. hm di edit hak akses modul jadi ga bisa nyimpan krn setvalue mendetect perubahan value ....
                    //obj = ((JCdb)col_editor[i]).getItemAt( (Integer)obj - 1 );

                    //unfully tested >> obj = ((JComboBox)col_editor[i]).getItemAt( (Integer)obj - 1 );
//debug+="4d";
                    if( obj==null ) obj="";    //supaya tetap string .. unfully checked :)
//debug+="4e";
                    if( !table_name.equals("no_db") ) obj = obj.toString();    //tanpa toString(), sortnya error:) tapi utk "no_db" malah toString() bikin error saat open pertamakali:) ... ntah kenapa ... males ngeceknya :p
                }
            } else 
            if( 1==1 ) {    //init value then    //gak jadi .... format to view in table
//debug+="5";

                    if( obj==null ) obj="";    //supaya tetap string .. unfully checked :)
                //String val = obj.toString();    //obj.toString() perlu!!! utk sorter ....

                //THIS MAGIC CASTING (Object) 0 ... TAKE MY THREE DAYS (INCLUDING INFLUENZA) :P .... and this is at 2.00 AM
                Object zero = table_name.equals("no_db") ? "0" : ( ( type[i]==Types.INTEGER&&!isSigned[i] ) || type[i]==Types.BIGINT ? (Object) 0L : (Object) 0 ) ;
                //Object zero = row_idx==0 ? 0 : getValueAt(0, i);

                switch( type[i] ) {
                    case Types.TINYINT : obj = obj.toString().equals("")?zero:obj;  break;
                    case Types.SMALLINT: obj = obj.toString().equals("")?zero:obj;  break;
                    case Types.INTEGER : obj = obj.toString().equals("")?zero:obj;  break;
                    case Types.BIGINT  : obj = obj.toString().equals("")?zero:obj;  break;
                    case Types.DOUBLE  : obj = obj.toString().equals("")?zero:obj;  break;
                    case Types.FLOAT   : obj = obj.toString().equals("")?zero:obj;  break;
                    case Types.BIT     :   break;
                    case Types.DATE    : obj = obj.toString().equals("")?(Object)0 : obj;  break;    //((java.sql.Date)obj).toString()    //"1900-01-01"
                    //case Types.DATE    : obj = obj.toString().equals("")?(Object)0 : new java.text.SimpleDateFormat("yyyy-MM-dd").format((java.sql.Date)obj);  break;    //"1900-01-01"
                    case Types.TIMESTAMP: obj = obj.toString().equals("")?(Object)0 : new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Timestamp)obj);  break;    //case Types.TIMESTAMP: obj = obj.toString().equals("")?(Object)0 : new java.util.Date(((Timestamp)obj).getTime()).toString();  break;    //ketambahan dua digit di belakang:p
                    case Types.TIME: obj = obj.toString().equals("")?(Object)0 : obj;  break;    //obj = obj.toString().equals("")?(Object)0 : new java.text.SimpleDateFormat("HH:mm").format((Time)obj);  break;
                    //default            :   
                }
                //if( columnNames[i].indexOf("diskon")>=0 ) val += "%";
                //obj = val;
            }
	    newRow.add(obj);
        }
// is_android ...
//android.util.Log.e("addrow: ", "2");
	newRow.add( rows.size() );
//android.util.Log.e("addrow: ", "3 enable_filter=" + enable_filter );
        if( enable_filter ) rows_backup.add(rows.size(),newRow);    //then rows can be filtered :p and the last_column will be the mapped rows_backup.row_idx to modify it in setValueAt() and remove()
//android.util.Log.e("addrow: ", "4");
        rows.add(row_idx,newRow);    //rows.addElement(newRow);
//android.util.Log.e("addrow: ", "5");
            //JOptionPane.showMessageDialog( retail.f, "before fire ... row_idx="+row_idx , "Kesalahan", JOptionPane.ERROR_MESSAGE);
         //((DefaultTableModel)table.getModel()).addRow(new java.util.Vector<String>(java.util.Arrays.asList(new String[]{String.valueOf(x++),"",""})));  
        //if( rs_row.length>0 )
                //try{
// is_android ...
//        if( fireInserted ) {
                        //android.os.Looper.prepare();
// new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {
//                                adapter.notifyItemInserted(row_idx);    //fireTableRowsInserted(row_idx, row_idx);    //jika ini error, cek table.getSelectionModel().addListSelectionListener    //fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
 //                           }},200);
 //                       android.os.Looper.loop();
//                           }

//                    } catch (Exception ex) {    retail.show_error( debug+"from fire \n. Pesan Kesalahan: " + ex + "  getColumnName=" + getColumnName(5)+ getColumnClass(5).getName() + (type[5]==Types.TINYINT), "Gagal baca Variable" ); }
        //fireTableDataChanged();
    }


    public void addRow( Object[] rs_row, int[] type ) {   //overload method, ga perlu fireInserted krn dipanggil oleh show_table
        addRow( getRowCount(), rs_row, type, false );
    }
    public void addRow( Boolean async, Object[] rs_row, int[] type ) {
        addRow( async, getRowCount(), rs_row, type, false );
    }

    public void addRow( int row_idx, Boolean fireInserted ) {   //overload method
        Object[] rs_row = new Object[getColumnCount()];
        for( int i=0; i<rs_row.length; i++ ) rs_row[i] = "";
        addRow( row_idx, rs_row, col_type, fireInserted );
    }
    public void addRow( Boolean async, int row_idx, Boolean fireInserted ) {   //overload method
        Object[] rs_row = new Object[getColumnCount()];
        for( int i=0; i<rs_row.length; i++ ) rs_row[i] = "";
        addRow( async, row_idx, rs_row, col_type, fireInserted );
    }

    public void addRow(int row_idx) {   //overload method
        addRow( row_idx, true );
    }    
    public void addRow(Boolean async, int row_idx) {   //overload method
        addRow( async, row_idx, true );
    }    

    public void addRow() {   //overload method, ga perlu fireInserted krn dipanggil oleh show_table
        if( getRowCount()>0 ) {
            String id = getStringAt(getRowCount()-1, 0);
            if( id.equals("0") || id.equals("") ) return;   //utk sementara, cek dulu apa row terakhir ini masih kosong :p
        }
        addRow( getRowCount(), false );
    }
    public void addRow(Boolean async) {
        if( getRowCount()>0 ) {
            String id = getStringAt(getRowCount()-1, 0);
            if( id.equals("0") || id.equals("") ) return;   //utk sementara, cek dulu apa row terakhir ini masih kosong :p
        }
        addRow( async, getRowCount(), false );
    }

    public String get_column_pattern(int column) {
        int len = 16;
        try {
            len  = metaData.getPrecision(column+1);    //getColumnDisplaySize
            if( isSigned[column] ) len--;    //Indicates whether values in the designated column are signed numbers.
        } catch (SQLException e) {
            return ".{0," +len+ "}";
        }

        switch( col_type[column] ) {
            //case Types.BIT: return Boolean.class;

        case Types.TINYINT  : //return "\\d{0,2}";   //"\\d{1," >> dia lakukan validasi klo disetText("") atau setText(null) dari code     //(otherwise, overload patternfilter dan cek apa lebih besar dari maksimum number :p) cari aman aja dulu batasin maxnya :p
        case Types.SMALLINT : //return "\\d{0,4}";
        case Types.INTEGER  : //return "\\d{0,9}";
             return   ( columnNames[column].indexOf("stok")==0 || columnNames[column].indexOf("amount")==0 ? "^-?" : ( columnNames[column].indexOf("_id")==columnNames[column].length()-3 ? "no-digit-separator" : "" ) )
                    //ga jadi, harusnya di on posting setvalue >> krn klo user ngubah ngubah 30 ke 20 tapi dengan cara tekan delete jadi ga bs:p + ( columnNames[column].indexOf("banyak")==0 ? "^[1-9]" : "" )
                    + "\\s*[(]*\\d{0,"+(len-1)+ "}(\\s*[\\-\\+\\/\\*%]{0,1}\\s*[(]*\\s*\\d{0,"+(len-1)+ "}\\s*[)]*){0,3}";   ////-?\\d+(?:\\.\\d+(?:E\\d+)?)?(\\s*[-+/\\*]\\s+-?\\d+(?:\\.\\d+(?:E\\d+)?)?)+    //by help of http://stackoverflow.com/users/106463/mihai-toader    //"\\d{0,"+(len-1)+ "}"    //stok need to allow negative too:p >> //"\\d{1," >> dia lakukan validasi klo disetText("") atau setText(null) dari code     //(otherwise, overload patternfilter dan cek apa lebih besar dari maksimum number :p) cari aman aja dulu batasin maxnya :         //(?:\\d+\\.)?\\d*(?:e[+\\-]?\\d+)?|[\\s\\-\\/()+*%=]
        case Types.BIGINT   : return "^-?\\d{1,19}$";    //utk masang negative sign, ini ada contoh yg agak mirip seh>> Pattern.compile("T(-{0,1}(?!0)\\d+)");  Please note the usage of negative look-ahead (?!0) to exclude -0 number and numbers that start with 0.
        case Types.TIME   : return "(([01]?[0-9]|2[0-3])?[:]?([0-5][0-9])?(:[0-5][0-9])?){0,8}";    //"((?:[01][0-9]|2[0-3]):(?:[0-5][0-9])(?::[0-5][0-9])){0,8}";    //".{0,8}([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?";    //>> tanpa detik dia hang:p >> "([01]?[0-9]|2[0-3]):[0-5][0-9]";    //ato >> (?:[01]\\d|2[0-3]):(?:[0-5]\\d))




        //case Types.FLOAT:
        //case Types.DOUBLE:
        //case Types.DATE: 

        default: return ( columnNames[column].equals("code") ? "ean13" : "" )    + ".{0," +len+ "}";
        }
    }

}
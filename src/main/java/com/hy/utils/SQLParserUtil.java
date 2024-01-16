package com.hy.utils;

import cn.hutool.json.JSONArray;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

//import co.elastic.logstash.api.Event;

public class SQLParserUtil {
    public static void main(String[] args) throws Exception {

//        System.out.println(formatDate2("2022-03-22T00:19:07.194Z",true));//
        String query="SELECT*FROM `SYNC_INFO` WHERE(UID=? AND PTS>=?)ORDER BY PTS ASC LIMIT 1000/*Y*/";

        Map<String, Object> msql = getSqlOut(query, "mysql");
        System.out.println("======================="+msql);
    }



    public static SQLStatement parser(String sql, DbType dbType) throws Exception{
        try {
            String  str= unicodeToCn(sql);
            List<SQLStatement> list = SQLUtils.parseStatements(str, dbType);
            if (list.size() > 1) {
                //throw new SQLSyntaxErrorException("MultiQueries is not supported,use single query instead ");
                System.out.println("MultiQueries is not supported,use single query instead:"+sql+"-end");
            }
            if(list.size()==1){
                return list.get(0);
            }
        }catch (Exception e){
            System.out.println("SQLParserUtil parser sql："+sql);
            e.printStackTrace();
        }
        return null;
    }

    public static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        if(StringUtils.isBlank(returnStr)){
            returnStr=unicode;
        }
        return returnStr;
    }

    public static Map<String,Object> getSqlOut(String sql,String dbTypeStr) throws Exception {
        Map<String, Object> sqlOut = new HashMap<>();
        DbType dbType = DbType.of(dbTypeStr);
        SQLStatement sqlStatement= parser(sql, dbType);
        if(sqlStatement!=null){
//            System.out.println(sqlStatement.getClass().getName());
            switch(sqlStatement.getClass().getName()){
                case "com.alibaba.druid.sql.ast.statement.SQLSelectStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLShowCreateViewStatement":
                    break;
                case "com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExplainStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDropSchemaStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGCreateSchemaStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLGrantStatement":
                    sqlOut = toJson(sqlStatement,"dcl",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLRevokeStatement":
                    sqlOut = toJson(sqlStatement,"dcl",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGAlterSchemaStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLAnalyzeTableStatement":
                    sqlOut = toJson(sqlStatement,"hql",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLDropTableStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLShowVariantsStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.ast.statement.SQLSetStatement":
                    sqlOut = toJson(sqlStatement,"ddl",dbType);
                    break;
                case "com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement":
                    sqlOut = toJson(sqlStatement,"dml",dbType);
                    break;
                default :
            }
        }
        return sqlOut;
    }

    public static Map<String,Object> toJson(SQLStatement statement, String dlanguage, DbType dbTypeField){
        HashMap<String, Object> sqlOut = new HashMap<>();
        SchemaStatVisitor visitor=new MySqlSchemaStatVisitor();
        if(dbTypeField.name().equals("mysql")){
            visitor = new MySqlSchemaStatVisitor();
        }else if(dbTypeField.name().equals("oracle")){
            visitor=new OracleSchemaStatVisitor();
        }else if(dbTypeField.name().equals("hive")){
            visitor=new HiveSchemaStatVisitor();
        }
        statement.accept(visitor);

        JSONArray arrayColumn=new JSONArray();
        JSONArray arrayTable=new JSONArray();
        for(TableStat.Column co:visitor.getColumns()){
            arrayColumn.add( co.getFullName());
        }
        Map<TableStat.Name, TableStat> map= visitor.getTables();
        Iterator<Map.Entry<TableStat.Name, TableStat>> entries = map.entrySet().iterator();
        ArrayList<Object> dbs = new ArrayList<>();
        while (entries.hasNext()) {
            JSONObject tableObject=new JSONObject();
            Map.Entry<TableStat.Name, TableStat> entry = entries.next();
            String[] split = entry.getKey().getName().split("\\.");
            if (split.length > 1){
                dbs.add(split[0]);
            }
            tableObject.put("name",entry.getKey().getName());
            tableObject.put("stat",entry.getValue().toString());
            arrayTable.add(tableObject);
        }
        if(arrayTable.size()>0){
            sqlOut.put("tables",arrayTable);
            System.out.println("[sql_out][tables]："+arrayTable);
        }
        if(arrayColumn.size()>0){
            sqlOut.put("columns",arrayColumn);
            System.out.println("[sql_out][columns]:"+arrayColumn);
        }
        if (dbs.size()>0){
            sqlOut.put("dbName",dbs.get(0));
            System.out.println("[sql_out][dbName]:"+dbs.get(0));
        }
        sqlOut.put("dlanguage",dlanguage);
        System.out.println("[sql_out][dlanguage]:"+dlanguage);
        return sqlOut;
    }

}

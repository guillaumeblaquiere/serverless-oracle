import os

import cx_Oracle


def oracle_connection(request):
    # Create a table in Oracle database
    try:

        db_ip = os.getenv("ORACLE_IP")
        db_schema = os.getenv("ORACLE_SCHEMA")
        db_user = os.getenv("ORACLE_USER")
        db_password = os.getenv("ORACLE_PASSWORD")

        # con = cx_Oracle.connect(f'{db_user}/{db_password}@{db_ip}:1521/{db_schema}')
        con = cx_Oracle.connect('system/root@35.222.1.145:1521/xe')

        # Now execute the sqlquery
        cursor = con.cursor()

        cursor.execute("select 'Great!' from dual")
        for row in cursor:
            return row[0], 200

    except cx_Oracle.DatabaseError as e:
        print("There is a problem with Oracle", e)
        return e, 500

    # by writing finally if any error occurs
    # then also we can close the all database operation
    finally:
        if cursor:
            cursor.close()
        if con:
            con.close()

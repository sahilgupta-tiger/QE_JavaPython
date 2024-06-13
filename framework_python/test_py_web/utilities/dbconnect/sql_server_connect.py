import sqlite3
import allure
import json
from utilities.CustomLogging import getLogger
import pyodbc
from utilities.encryption import decode
from tests.settings import *

log = getLogger()


def replace_sql_filters(sqlfile, tc_params):
    year_params = []
    commodity_group_params = []
    commodity_params = []
    sector_params = []
    country_params = []
    coun_region_params = []
    year_sql = ""
    comm_grp_sql = ""
    comm_sql = ""
    sector_sql = ""
    country_sql = ""
    region_sql = ""
    # Splitting the multi selection parameters
    for i in range(0, len(tc_params)):
        if tc_params[i].find("|") != -1:
            if i == 0:
                year_params = tc_params[i].split("|")
            elif i == 1:
                commodity_group_params = tc_params[i].split("|")
            elif i == 2:
                commodity_params = tc_params[i].split("|")
            elif i == 3:
                sector_params = tc_params[i].split("|")
            elif i == 4:
                country_params = tc_params[i].split("|")
            elif i == 5:
                coun_region_params = tc_params[i].split("|")

    # Replacing the Filter values in SQL statement
    if tc_params[0] != "All":
        if db_type == "sqlserver":
            if len(year_params) > 1:
                sql_params = ", ".join(["'{}'".format(each) for each in year_params])
                year_sql = f" AND [Year] in ({sql_params})"
            else:
                year_sql = f" AND [Year] in ('{str(tc_params[0])}')"
        elif db_type == "sqlite3":
            if len(year_params) > 1:
                sql_params = " OR ".join(["[DATE] LIKE '%{}'".format(each) for each in year_params])
                year_sql = " AND (" + str(sql_params) + ")"
            else:
                year_sql = f" AND [DATE] LIKE '%{str(tc_params[0])}'"
    if tc_params[1] != "All":
        if len(commodity_group_params) > 1:
            sql_params = ", ".join(["'{}'".format(each) for each in commodity_group_params])
            comm_grp_sql = f" AND [Commodity] in ({sql_params})"
        else:
            comm_grp_sql = f" AND [Commodity] in ('{str(tc_params[1])}')"
    if tc_params[2] != "All":
        if db_type == "sqlite3":
            column_2 = "[Sub0Commodity]"
        else:
            column_2 = "[Sub_Commodity]"
        if len(commodity_params) > 1:
            sql_params = ", ".join(["'{}'".format(each) for each in commodity_params])
            comm_sql = f" AND {column_2} in ({sql_params})"
        else:
            comm_sql = f" AND {column_2} in ('{str(tc_params[2])}')"
    if tc_params[3] != "All":
        if len(sector_params) > 1:
            sql_params = ", ".join(["'{}'".format(each) for each in sector_params])
            sector_sql = f" AND [Sector] in ({sql_params})"
        else:
            sector_sql = f" AND [Sector] in ('{str(tc_params[3])}')"
    if tc_params[4] != "All":
        if db_type == "sqlite3":
            pass
        else:
            if len(country_params) > 1:
                sql_params = ", ".join(["'{}'".format(each) for each in country_params])
                country_sql = f" AND [Country] in ({sql_params})"
            else:
                country_sql = f" AND [Country] in ('{str(tc_params[4])}')"
    if tc_params[5] != "All":
        if db_type == "sqlite3":
            column_5 = "[Country/Region]"
        else:
            column_5 = "[Country_Region]"
        if len(coun_region_params) > 1:
            sql_params = ", ".join(["'{}'".format(each) for each in coun_region_params])
            region_sql = f" AND {column_5} in ({sql_params})"
        else:
            region_sql = f" AND {column_5} in ('{str(tc_params[5])}')"

    final_filter = year_sql + comm_grp_sql + comm_sql + sector_sql + country_sql + region_sql

    if sqlfile.find("--$conditions$") != -1:
        sqlfile = sqlfile.replace("--$conditions$", final_filter)
        print("The Whole SQL query is:\n"+sqlfile)

    return sqlfile


def format_sensitivity_data(inputlist):
    formattedlist = []
    if len(inputlist) == 1 and len(inputlist[0]) == 1:
        formattedlist.append(convert_sensitivity_data(int(inputlist[0][0])))
    else:
        for a in inputlist:
            if len(a) > 1:
                formattedlist.append(convert_sensitivity_data(int(a[1])))
            else:
                formattedlist.append(convert_sensitivity_data(int(a[0])))

    return formattedlist


def format_db_data(inputlist):
    formattedlist = []
    if len(inputlist) == 1 and len(inputlist[0]) == 1:
        formattedlist.append(convert_numeric_data(int(inputlist[0][0])))
    else:
        for b in inputlist:
            if len(b) > 1:
                formattedlist.append(convert_numeric_data(int(b[1])))
            else:
                formattedlist.append(convert_numeric_data(int(b[0])))
    return formattedlist


def convert_numeric_data(num):
    ret_str = ""
    if num >= 0:  # for positive numbers
        if num >= 1000000:
            if not num % 1000000:
                ret_str = f'${num // 1000000}M'
            else:
                ret_str = f'${round(num / 1000000)}M'
        elif num >= 1000:
            if not num % 1000:
                ret_str = f'${num // 1000000}M'
            else:
                ret_str = f'${round(num / 1000000)}M'
        elif num < 1000:
            ret_str = f'${round(num / 1000000)}M'
    elif num < 0:       # for negative numbers
        num = abs(num)
        if num >= 1000000:
            if not num % 1000000:
                ret_str = f'(${num // 1000000}M)'
            else:
                ret_str = f'(${round(num / 1000000)}M)'
        elif num >= 1000:
            if not num % 1000:
                ret_str = f'(${num // 1000000}M)'
            else:
                ret_str = f'(${round(num / 1000000)}M)'
        elif num < 1000:
            ret_str = f'(${round(num / 1000000)}M)'

    return ret_str


def convert_sensitivity_data(num):
    ret_str = ""
    if num == 0:
        ret_str = "$0.00M"
    elif num > 0:  # for positive numbers
        if num >= 1000000:
            if not num % 1000000:
                ret_str = f'${num // 1000000}M'
            else:
                ret_str = f'${"{:.2f}".format(num / 1000000, 2)}M'
        elif num >= 1000:
            if not num % 1000:
                ret_str = f'${num // 1000000}M'
            else:
                ret_str = f'${"{:.2f}".format(num / 1000000, 2)}M'
        elif num < 1000:
            ret_str = f'${"{:.2f}".format(num, 2)}'
    elif num < 0:       # for negative numbers
        num = abs(num)
        if num >= 1000000:
            if not num % 1000000:
                ret_str = f'(${num // 1000000}M)'
            else:
                ret_str = f'(${"{:.2f}".format(num / 1000000, 2)}M)'
        elif num >= 1000:
            if not num % 1000:
                ret_str = f'(${num // 1000000}M)'
            else:
                ret_str = f'(${"{:.2f}".format(num / 1000000, 2)}M)'
        elif num < 1000:
            ret_str = f'(${"{:.2f}".format(num, 2)})'

    return ret_str


def convert_value_MK(num):
    ret_str = ""
    if num >= 0:  # for positive numbers
        if num >= 1000000:
            if not num % 1000000:
                ret_str = f'${num // 1000000}M'
            else:
                ret_str = f'${"{:.2f}".format(num / 1000000)}M'
        elif num >= 1000:
            if not num % 1000:
                ret_str = f'${num // 1000}K'
            else:
                ret_str = f'${"{:.2f}".format(num / 1000)}K'
        elif num < 1000:
            ret_str = f'${"{:.2f}".format(num, 2)}'
    elif num < 0:       # for negative numbers
        num = abs(num)
        if num >= 1000000:
            if not num % 1000000:
                ret_str = f'(${num // 1000000}M)'
            else:
                ret_str = f'(${"{:.2f}".format(num / 1000000)}M)'
        elif num >= 1000:
            if not num % 1000:
                ret_str = f'(${num // 1000}K)'
            else:
                ret_str = f'(${"{:.2f}".format(num / 1000)}K)'
        elif num < 1000:
            ret_str = f'(${"{:.2f}".format(num, 2)})'

    return ret_str


def connect_to_database(sql):

    if db_type == "sqlserver":
        conn = pyodbc.connect('Driver={SQL Server};'
                              'Server=qe-vm1;'
                              f'Database={dbname};'
                              'Trusted_Connection=yes;'
                              f'User ID={dbuserid};Password={decode(dbpassword)}')
    elif db_type == "sqlite3":
        conn = sqlite3.connect(sqlite_loc)

    cursor = conn.cursor()
    try:
        cursor.execute(sql)
        dbval = cursor.fetchall()
    finally:
        cursor.close()
    conn.close()
    return dbval


def read_from_sql_file(sfpath, tc_params, tc_name):
    log.info("Read SQL file and Execute SQL on DB")
    with allure.step("Read SQL file and Execute SQL on DB"):
        if not use_query_json:
            sql_path = sql_folder + sfpath
            fd = open(sql_path, "r+")
            readsqlfile = fd.read()
        else:
            f = open(sql_qry_file, "r+")
            data = json.load(f)
            readsqlfile = data[sfpath]

        wholesql = replace_sql_filters(readsqlfile, tc_params)
        queryrunval = connect_to_database(wholesql)
        if tc_name == "tc5":
            returnlist = format_sensitivity_data(queryrunval)
        else:
            returnlist = format_db_data(queryrunval)
        returnlist.sort()
        log.info(returnlist)
        if len(returnlist) == 1:
            return str(returnlist[0])
        else:
            return returnlist


def direct_read_from_sql(sfpath, tc_params):
    log.info("Read SQL file and Execute SQL on DB")
    with allure.step("Read SQL file and Execute SQL on DB"):
        if not use_query_json:
            sql_path = sql_folder + sfpath
            fd = open(sql_path, "r+")
            readsqlfile = fd.read()
        else:
            f = open(sql_qry_file, "r+")
            data = json.load(f)
            readsqlfile = data[sfpath]

        wholesql = replace_sql_filters(readsqlfile, tc_params)
        returnlist = connect_to_database(wholesql)
        returnlist.sort()
        log.info(returnlist)
        if len(returnlist) == 1:
            return returnlist[0]
        else:
            return returnlist


import sqlite3
import pandas
from tests.settings import *

conn = sqlite3.connect(f'{dbname}.db')
c = conn.cursor()
table_name_list = ['bi_demo_filters', 'executive_summary', 'drilldown', 'sample_table', 'spend_data']


def import_csv_to_db(csv_file, tbl_name):
    df = pandas.read_csv(csv_file)
    df.to_sql(tbl_name, conn, if_exists='replace', index=False)


def table_exists(table):
    c.execute('''SELECT count(name) FROM sqlite_master 
        WHERE TYPE = 'table' AND name = '{}' '''.format(table))
    if c.fetchone()[0] == 1:
        return True  # Change this to False if data is updated in CSV files after tables are created.
    return False


# Create all tables in SQLITE3 for comparison with Power BI
for tblname in table_name_list:
    if not table_exists(tblname):
        import_csv_to_db(f"{tblname}.csv", tblname)


conn.commit()
conn.close()


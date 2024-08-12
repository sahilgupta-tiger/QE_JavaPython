import os

url = "https://www.saucedemo.com"
browser = "chrome"  # options: chrome, edge, headless, firefox, healenium
db_type = "sqlite3"  # options: sqlserver, sqlite3
chrome_loc = r"D:\Project\QE_JavaPython\commonutils\drivers\chromedriver\chromedriver.exe"
emailID = "sahil.gupta@tigeranalytics.com"
encryptedPasscode = "UGFydHkjMjk5"
dbname = 'SamplePowerBI'
dbuserid = "sa"
dbpassword = "Um9vdEAxMjM="
sqlite_loc = fr"database\{dbname}.db"
use_query_json = True  # options: True for not using sql query files else False
sql_qry_file = "tests/test_data/sql_queries.json"
sql_folder = "tests/test_data/sql/"

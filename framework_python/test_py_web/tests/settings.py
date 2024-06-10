import os

url = "https://app.powerbi.com/groups/me/reports/d0e812bb-85c0-4f8c-895f-019035e3c481/ReportSection59b352136fd9452af512?ctid=e714ef31-faab-41d2-9f1e-e6df4af16ab8&experience=power-bi&bookmarkGuid=6107fe52-1509-495f-b1f8-0c1dad32dc34"
browser = "chrome"  # options: chrome, edge, headless, firefox, healenium
db_type = "sqlite3"  # options: sqlserver, sqlite3
chrome_loc = r"utilities\drivers\chromedriver\chromedriver.exe"
emailID = "sahil.gupta@tigeranalytics.com"
encryptedPasscode = "UGFydHkjMjk5"
dbname = 'SamplePowerBI'
dbuserid = "sa"
dbpassword = "Um9vdEAxMjM="
sqlite_loc = fr"database\{dbname}.db"
use_query_json = True  # options: True for not using sql query files else False
sql_qry_file = "tests/test_data/sql_queries.json"
sql_folder = "tests/test_data/sql/"

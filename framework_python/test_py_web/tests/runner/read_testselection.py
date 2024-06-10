import csv
import pandas as pd

csv_filepath = "tests/runner/dashboard_testselection.csv"

def get_marker_read_csv():
    currentrun = ""
    with open(csv_filepath, 'r') as file:
        csvreader = csv.reader(file)
        markerlist = []
        for row in csvreader:
            if row[1].upper() == 'Y':
                markerlist.append(row[0])
        if len(markerlist) != 0 and len(markerlist) > 1:
            currentrun = "-m " + " or ".join(markerlist)
        elif len(markerlist) == 1:
            currentrun = "-m " + markerlist[0]

    return currentrun


def get_execute_tc_args(var):
    tagfilter = ""
    scnlist = []
    with open(csv_filepath, 'r') as file:
        csvreader = csv.reader(file)
        if var.upper() == "ALL":
            for row in csvreader:
                scnlist.append(row[0])
        elif var.upper() == "SMOKE":
            for row in csvreader:
                if row[2].find("smoke") != -1:
                    scnlist.append(row[0])
        elif var.upper() == "REGRESSION":
            for row in csvreader:
                if row[2].find("regression") != -1:
                    scnlist.append(row[0])
        elif "," not in var:
            tagfilter = f"-k {var}"
        else:
            scnlist = var.split(",")

    if len(scnlist) != 0 and len(scnlist) > 1:
        tagfilter = "-k " + " or ".join(scnlist)
    elif len(scnlist) == 1:
        tagfilter = "-k " + scnlist[0]

    return tagfilter


def get_scenario_read_csv():
    tagfilter = ""
    scnlist = []
    with open(csv_filepath, 'r') as file:
        csvreader = csv.reader(file)
        for row in csvreader:
            if row[1].upper() == 'Y':
                scnlist.append(row[0])
    if len(scnlist) != 0 and len(scnlist) > 1:
        tagfilter = "-k " + " or ".join(scnlist)
    elif len(scnlist) == 1:
        tagfilter = "-k " + scnlist[0]

    return tagfilter


def get_tc_parameters(tc_name):
    tc_with_parameters = []
    df = pd.read_csv(csv_filepath)
    df.fillna("All", inplace=True)
    df.set_index("Marker", inplace=True)
    results = df.loc[tc_name]
    if type(results['Year']) == float:
        results['Year'] = str(results['Year']).replace(".0", "")
    tc_with_parameters.append(results['Year'])
    tc_with_parameters.append(results['Commodity Group'])
    tc_with_parameters.append(results['Commodity'])
    tc_with_parameters.append(results['Sector'])
    tc_with_parameters.append(results['Country'])
    tc_with_parameters.append(results['Country/Region'])
    return tc_with_parameters


def get_parallel_executors_csv(scenarioexecute):
    number = 1
    executorlist = []
    if scenarioexecute.find("or") != -1:
        executorlist = scenarioexecute.split(" or ")
    else:
        executorlist.append(scenarioexecute)
    length = len(executorlist)
    if length != 0 and length > 1:
        if 4 < length < 9:
            number = 2
        elif length >= 9:
            number = 3

    return number


import inspect
import logging
import os

test_res = os.getcwd()

def getLogger():

    # Gets the name of the class / method from where this method is called
    loggerName = inspect.stack()[1][3]
    logger = logging.getLogger(loggerName)
    # By default, log all messages
    #if not os.path.exists(".//tests_results//PytestLogs//Pytest.log"):
     #   os.makedirs(".//tests_results//PytestLogs//Pytest.log")
    #os.chmod(".//tests_results//PytestLogs//Pytest.log", 0o777)  # for Python3

    fileHandler = logging.FileHandler("tests/tests_results/PytestLogs/Pytest.log", mode='a')

    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s: %(message)s',
                                  datefmt='%m/%d/%Y %I:%M:%S %p')

    fileHandler.setFormatter(formatter)
    logger.addHandler(fileHandler)
    logger.setLevel(logging.DEBUG)
    return logger




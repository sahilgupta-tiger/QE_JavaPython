@echo off
SETLOCAL ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION
REM Set PYTHONPATH so Python can locate root project modules
SET PYTHONPATH=%CD%

SET VENV_DIR=.jupyvenv
SET REQUIREMENTS=requirements.txt

REM Check if virtual environment exists
IF NOT EXIST %VENV_DIR% (
    echo Creating virtual environment...
    python -m venv %VENV_DIR%
)

REM Activate the virtual environment
CALL %VENV_DIR%\Scripts\activate

REM Install dependencies
IF EXIST %REQUIREMENTS% (
    python.exe -m pip install --upgrade pip
    echo Installing all dependencies...
    pip install --upgrade browser-use playwright --break-system-packages
    pip install -r %REQUIREMENTS%
) ELSE (
    echo No requirements.txt found, skipping installation.
)

REM Install Playwright browsers
CALL playwright install

REM Run the jupyter server
where jupyter >nul 2>nul
IF ERRORLEVEL 1 (
    echo Jupyter executable not found in the virtual environment.
    exit /b 1
)

echo Starting Jupyter server...
jupyter notebook

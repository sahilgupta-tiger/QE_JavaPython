@echo off
title Empty Out Allure Results
echo ::Deleting all files from Allure Results folder and its sub-folders::
powershell.exe "Get-ChildItem -Path 'allure_results' -File -Recurse -Exclude environment.properties -Force | Remove-Item -Force"
timeout 5
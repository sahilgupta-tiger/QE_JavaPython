pipeline {
    agent any
    parameters {
        separator(name: 'Functional_Testing', sectionHeader: 'Functional Testing')
        booleanParam(name: 'skip_stg_java', defaultValue: false,
                    description: 'Click above to skip Java execution stage')
        booleanParam(name: 'skip_stg_python', defaultValue: false,
                            description: 'Click above to skip Python execution stage')
        string (name: 'testcases', defaultValue: 'tc1',
                description: 'Enter list of test case markers to execute, separated by comma & no-spaces.')
    }
    options {
        timeout(time: 45, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '25'))
    }
    environment {
        datfpath = 'D:/My_Workspaces/GitHub/QE_ATF/datf_core/'
        currwrkdir = pwd()
    }
    stages {
        stage('Infra Setup for Auto Execution'){
            steps {
                script {
                    catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                        if (!params.skip_stg_python) {
                            echo '### Install Python libraries for App'
                            withPythonEnv('python3') {
                                bat """
                                python.exe -m pip install --upgrade pip
                                pip install --no-cache-dir -r requirements.txt
                                """
                            }
                        }
                        if (!params.skip_stg_java) {
                            def owaspName = "${env.containerZAP}"
                            echo "### Starting OWASP ZAP container"
                            bat """
                            docker run -dt --name $owaspName \
                            owasp/zap2docker-stable /bin/bash
                            """
                        }
                    }
                }
            }
        }
        stage('Execute Python based Functional Automation') {
            when { expression { params.skip_stg_python != true } }
            steps {
                echo '### Execute App Test Suite ###'
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    withPythonEnv('python3') {
                        bat 'python run.py'
                    }
                }
            }
        }
        stage('Execute Java based Functional Automation') {
            when { expression { params.skip_stg_java != true } }
            steps {
                echo '### Execute App Test Suite ###'
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    withPythonEnv('python3') {
                        bat 'python run.py'
                    }
                }
            }
        }
        stage('Post Build Reporting') {
            steps {
                script {
                    junit 'tests/tests_results/integration.xml'
                    allure([
                            includeProperties: false,
                            jdk: '',
                            properties: [],
                            reportBuildPolicy: 'ALWAYS',
                            results: [[path: 'tests/tests_results/reports/allure_results']]
                    ])
                }
            }
        }
    }
}
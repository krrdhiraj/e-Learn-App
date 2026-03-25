pipeline {
    agent any          // which machine/container to run on

    environment {      // environment variables
        APP_NAME = 'e-LearningApp'
    }

    stages {           // the actual steps, run in order
        stage('Build') {
            steps {
                sh 'npm install'
            }
        }
        stage('Test') {
            steps {
                sh 'npm test'
            }
        }
    }

    post {             // what to do after pipeline finishes
        success { echo 'Pipeline passed!' }
        failure { echo 'Something broke.' }
    }
}
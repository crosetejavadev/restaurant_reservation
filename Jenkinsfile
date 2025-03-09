pipeline {
    agent any

    environment {
        APP_NAME = 'restaurant-reservation'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/crosetejavadev/restaurant_reservation.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying ${APP_NAME}..."
                // Add deployment commands here
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed! Check logs for details.'
        }
    }
}
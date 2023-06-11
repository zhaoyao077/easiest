pipeline {
    agent any
    tools {
        maven 'MyMAVEN'
        jdk 'MyJDK'
    }
    stages {
        stage ('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'ce496662-53b4-4d33-807a-2bb10cf28005', url: 'https://git.nju.edu.cn/40/sentistrength.git'
            }
        }
        stage ('Build') {
            steps {
                sh 'mvn clean compile package'
            }
        }
        stage ('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/sentistrength-*.jar', followSymlinks: false, onlyIfSuccessful: true
//                 updateGitlabCommitStatus name: 'build', state: 'success'
                sh 'pwd'
                sh 'rm /home/jenkins/archive/sentistrength*.jar'
                sh 'cp target/sentistrength*.jar "/home/jenkins/archive/sentistrength.jar"'
                sh 'bash deploy.sh'
            }
        }
    }
}
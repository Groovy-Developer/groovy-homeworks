pipeline {
  agent any
  stages {
    stage('stage1') {
      parallel {
        stage('stage1') {
          steps {
            sh '''echo "hello world"
'''
            sh 'echo "hello2"'
          }
        }

        stage('stage2') {
          steps {
            echo 'hello3'
          }
        }

      }
    }

  }
}
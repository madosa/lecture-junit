### II. 개발 환경 구축

####   1. OpenJDK, IntelliJ IDEA, Docker, MySQL 설치

- OpenJDK 설치

  - Mac

    ```
    brew tap AdoptOpenJDK/openjdk
    brew cask install adoptopenjdk8
    ```

  - Window : https://adoptopenjdk.net/

- IntelliJ IDEA : https://jwprogramming.tistory.com/99

- Docker 설치

  - Mac : https://madosa.kr/58

    ```
    $ mkdir /Users/majoonchae/mysql
    $ cd /Users/majoonchae/mysql
    $ mkdir /Users/majoonchae/mysql/lecture
    $ cd /Users/majoonchae/mysql/lecture
    $ mkdir conf data
    $ cd conf
    $ vi my.cnf
    [client] 
    default-character-set=utf8mb4 
    
    [mysql] 
    default-character-set=utf8mb4 
    
    [mysqld] 
    collation-server = utf8mb4_unicode_ci 
    init-connect='SET NAMES utf8mb4' 
    character-set-server = utf8mb4
    
    $ sudo docker create --name lecture-mysql -p 43306:3306 -v /Users/majoonchae/mysql/lecture/conf:/etc/mysql/conf.d -v /Users/majoonchae/mysql/lecture/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=admin12# -e MYSQL_DATABASE=lecture -e MYSQL_USER=lecture -e MYSQL_PASSWORD=lecture12# mysql:5.7.19
    
    $ sudo docker start lecture-mysql
    ```

  - Window : https://steemit.com/kr/@mystarlight/docker

  - PowerShell : https://www.tabmode.com/windows10/powershell-to-command-prompt.html
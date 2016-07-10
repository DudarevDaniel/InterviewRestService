# InterviewRestService
[![Build Status](https://travis-ci.org/DudarevDaniel/InterviewRestService.svg?branch=master)](https://travis-ci.org/DudarevDaniel/InterviewRestService)

<h3>Rest service which returns contacts by name filter</h3>
<br>
<p>Пояснения решения:</p>
<p>Относительно пожелания №1 о том, что фильтр нужно применять в Java, а не в SQL - я не увидел в этом смысла, 
так-как в базе данных хранятся миллионы записей, и для каждого запроса нужно будет выгружать их все, чтобы отфильтровать 
 в Java. Поэтому я использовал возможности SQL. Может я не корректно понял требование?</p>
 
<p>Для реализации второго требования, понимая, что результат запроса может быть таким большим, что приведет к OutOfMemoryError, 
я использовал ScrollableResults (Hibernate), который является по-сути абстракцией над курсором в базе данных. 
Таким образом, мы не выгружаем в память сразу все результаты, а итерируемся по ним. Однако, все еще остается риск того, 
что результирующий json будет слишком большим. Также, из-за решения применить ScrollableResults, я не использую 
автоматическую сериализацию Jackson, а формирую json вручную.</p>

<p>Многопоточность сервиса обеспечивается Spring</p>
<p>Я использовал MySQL, т.к. на данном примере не принципиально какая БД, а Postgre у меня не было установлено :)</p>

<h3>Как развернуть БД, запустить приложение:</h3>
<p>Необходимо запустить службу MySQL, в консоли (create database demoRestDB;) или в Workbench создать базу 
данных "demoRestDB". Порт по-умолчанию 3306, username: root, password: root. Настройки подключения указаны в 
application.properties. Приложение запускается либо командой "mvn spring-boot:run", либо в IDE запустить 
main-class "com.example.helloworld.InterviewApplication". Проект пакуется в jar.</p>

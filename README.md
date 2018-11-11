Ecom Backend System
=========
```
@author     Jiupeng Zhang <jiupeng.zhang@gmail.com>
@date       11/17/2017
@since      1.0.0
```



Demo
====

![](https://github.com/jiup/ecom/raw/master/.github/DEMO_IMAGES/d1.png)
![](https://github.com/jiup/ecom/raw/master/.github/DEMO_IMAGES/d2.png)
![](https://github.com/jiup/ecom/raw/master/.github/DEMO_IMAGES/d3.png)
![](https://github.com/jiup/ecom/raw/master/.github/DEMO_IMAGES/d4.png)
![](https://github.com/jiup/ecom/raw/master/.github/DEMO_IMAGES/d5.png)
![](https://github.com/jiup/ecom/raw/master/.github/DEMO_IMAGES/d6.png)
![](https://github.com/jiup/ecom/raw/master/.github/DEMO_IMAGES/d7.png)


Open-Source
====

This project was open-sourced by *Jiupeng Zhang* since *Nov 2018*.

Under [MIT License](LICENSE)




Table of Contents
=================

* [Name](#Name)
* [Deploy Address](#deploy-address)
* [Dependency and Environment](#dependency-and-environment)
* [DDL](#ddl)
* [Develop Process](#develop-process)




Deploy Address
==============
- dev
    - localhost/ (jetty:run)
    - localhost/ecom/ (tomcat7:run)
- prod



Dependency and Environment
==========================

- version
    - 1.0.0

- domain


- port
    - 443

- dev language
    - Java 1.8 (Oracle)

- runtime user:
    - nginx:www
    - jvm:root

- runtime dependency:
    - nginx 1.10.1
    - tomcat 8.5.23
    - mysql 5.7
    - jvm 1.8

- doc
    - landing



DDL
===

- see development docs (database part)



Develop Process
===============

- Project Init
    - [x] maven ready
    - [x] spring ready
    - [x] datasource ready
    - [x] spring mvc ready
    - [x] thymeleaf ready
    - [x] mybatis ready
    - [x] i18n ready
    - [x] captcha schema ready
    - [x] json resolver ready
    - [x] global error_page ready
    - [x] rest api design ready
    - [x] mybatis-generator ready
    - [x] page_helper ready
    - [x] swagger ready

- Coding
    - [x] token validation
    - [x] login/out part
    - [x] api contact form
    - [x] api quote request form
    - [x] customized auto-mail
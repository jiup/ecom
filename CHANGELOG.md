CHANGELOG.md
============
```
@author     Jiupeng Zhang <jiupeng.zhang@gmail.com>
@date       11/17/2017
@since      1.0.0
```

1.0.0
=====

* Initial release

1.0.1
=====

* Supports SSO
* Add token self-validation
* Alter package name and decoupling fore/back parts


1.0.2
=====

* Add two mail services - JavaMail, Mailgun
* Add mail plugin
* Add simple rate limiter
* Separated routing and added form validation


1.0.3
=====

* Add annotations for form validation
* Update token hash to hmac-sha1
* Coexists Thymeleaf and JSP for BotDetect
* Customize BotDetect for better use


1.0.4
=====

* Update BotDetectUtils, supports captcha validation
* Gson message converter added
* Designed basic restful api with response builder
* Add RestError
* Diff error view for /api and /admin
* Prep for contact service


1.0.5
=====

* Embrace restful api design
* Add RestError
* Diff error view for /api and /admin
* Prep for contact service
* Restful error-handling schema added
* Mybatis generator & page helper introduced
* Prepared for Swagger


1.0.6
=====

* Update BotDetectUtils, remove inline scripts
* Update document path to swagger json docs
* Contact auto-email done
* Contact form service completed (customize captcha image size)


1.0.7
=====

* Update settings to combine with Jenkins
* Hidden cookie api param
* Bugfix: MemberLoginForm not found
* Offered AdminLoginForm instead for AdministratorController
* Add include.js for captcha demo page


1.0.8
=====

* Start file logging (highlight error logs)
* Bugfix: use absolute path (tomcat:catalina.base) for file logging
* Get a quote demo page offered HandleAuthenticationException in api context
* Update project progress description
* Add admin mail contact page
* Add admin index page
* Update contact demo page to latest version


1.0.9
=====

* Add global error page
* Update database structure (table quote_request.if_delete)
* Add Login forward path support (CSRF)


1.0.10
=====

* Errorpage logic divided by sub-domain "context + /api" (api.xxx.xx)
* Add ip headers detect for reverse-proxies
* Update tokenHoldExceptionHandler responses status 403
* Add assets files (ico and main-logo)
* Set default view for <context>/admin/
* Cookie path set to / for cross site service
* Bugfix: disable form error message source translation
* Bugfix: login state transfer error


1.0.11
=====

* Add mail template module
* Update pages link for mail template
* Rename mail service base
* Bugfix: user login expired forward path with context
* Build cors whitelist
* Allow cross origin botdetect captcha request
* Rename captcha servlet
* Re-add cors support for captcha servlet
* Try supports preflight access control check (method=OPTION)
* Add option Access-Control-Allow-Headers for captcha servlet option request
* Move Access-Control-Allow-Origin control to nginx.conf
* Bugfix: CAPTCHA_REQUEST_LIMITER point to MAIL_REQUEST_LIMIT_RATE


1.0.12
=====

* Implemented automail module
* Add Mail RateLimiter
* Bugfix: Pictshare returned non-json data
* Add Automail pages
* Supports multi notify email addresses
* Update mail status display
* Add page item delete confirm
* Submit dependency js files
* Update user name to 'admin'
* Add delete confirm on customer-ext page
* Update 'switch identity' link on admin login page
* Bugfix: status not updated if automail sent failure
* Bugfix: wrong link for twitter in automail template
* Supports ext-customer insert/update
* Fine-tune on customer-ext query_all sql
* Deleted unused js code in customer-ext admin page


1.0.13
=====

* Add pagination to mail-sender page
* Update automail template
* Update admin header logo
* Update automail template (compatible for gmail)
* Add product API for ecom-fe
* Update config file for integrate test
* Update Quote Form boolean -> Boolean
* Remove test codes
* Bugfix: phone_number required in quote form
* Supports admin_login auto forward
* Update login failure warnings
* Bugfix: logout removes user_token in database but not remove client's causes NPE
* Update mismatched product images
* Extends customer page and pagehelper select methods
* Ready for prod_environment


1.1.0
====

* initial release to prod-env
* Supported backend product management
* Updated product APIs
* Supported online/offline toggle for products
* Bugfix: internal error when visit /admin/product/{not_exist_id}


1.1.1
=====

* Supported inventory management 
* Supported sku-specs management
* Supported rich text for product detail
* Rearranged menu buttons
* Bugfix: summernote not loaded


1.1.2
=====

* Supported category structure management
* Updated product and specification functions for category
* Updated menu item for categories -> structure
* Supported spec-filter
* Bugfix: ProductInitForm didn't pass categoryId


1.1.3
=====

* Updated service permissions
* Supports specs filter management
* Extract page common elements to thymeleaf fragments
* Supports online user query and display
* Supports personal account settings
* Supports password update
* Supports manageable accounts add/reset


1.1.4
=====

* Open-sourced to GitHub
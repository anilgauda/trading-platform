Trading Platform
==

A platform for buying/selling stocks with advanced reporting and goal management.

## Database

We are using Postgres database with Spring JPA (with Hibernate). 
Please make sure to use `@Table` annotation to give plural names for your tables
when creating an entity in model package  
like this:  
`@Table(name = "users")`

This is because many of the words like "user" are keywords in Postgres database.  


## Authentication

The authentication is built with Spring security using JWT for stateless identification of the user.

How it works:

1. When user sends an authenticate request to the server with username and password a **token** 
is generated using JWT. This token stores the username in it with an expiry.
2. If the user sends the same token to the server, then the server knows its a valid user.


#### Testing out the current implementation

Open http://localhost:8080/dash  
Run following in browser console in developer tools (f12 in chrome):

1. XHR.createUser("username", "password")
2. XHR.authenticate("username", "password")
3. XHR.get("user/profile", {}, function(data) {console.log(data)})


In above code, XHR is just a simple wrapper over jQuery AJAX. It does nothing more than
add a **Authentication** header in every request with a token. The token is generated
and stored in **sessionStorage** when you call XHR.authenticate() so that the token persists
even after a page refresh.

Please do not use jQuery.get() or jQuery.post() for any calls to the server, instead use
the wrapper methods XHR.get() and XHR.post()

## Front-end

The front end is built on SB Admin 2 Bootstrap theme using Pebble template framework.
We **will not** use pebble template engine at all since we will be writing the complete
frontend using simple html and jquery. Pebble templating engine was only used to create
the skeleton base template because we do not want unnecessary code duplication of the site menu
and any other frequently changed things.

So, if you want to add a new page, just copy `dash.html` in src/main/resources/templates

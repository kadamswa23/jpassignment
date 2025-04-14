Spring boot application running on default port - 8080


API exposed with
 http://localhost:8080/api/v1.0/users

Post API
 POST request (http://localhost:8080/api/v1.0/users)
with below json

{
   "firstName":"Jean",
   "lastName":"Mark",
   "dob":"2007-07-25",
   "addresses":[
      {
         "street":"123a Main Road, Glasgow, G1 1AA"
      },
      {
         "street":"123a Main Road, Glasgow, G1 1AA"
      }
   ],
   "contact":{
      "homePhone":"0141123451234",
      "mobilePhone":"839432487324",
      "email": [
            "test@gmail.com",
            "janedoe@email.co.uk"
        ]
   }
}

GET ALL
 http://localhost:8080/api/v1.0/users?offset=0&pageSize=10&sortBy=firstName
GET by Email
 http://localhost:8080/api/v1.0/user?emailId=manojp@gmail.com
GET by ID
 http://localhost:8080/api/v1.0/users/{id}
DELETE by ID -- DELETE Request
 http://localhost:8080/api/v1.0/users/3
PUT by ID -- PUT request
 http://localhost:8080/api/v1.0/users/4
 with user json
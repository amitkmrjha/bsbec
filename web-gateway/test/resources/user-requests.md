**Sign-up**

curl --request POST \
  --url http://localhost:9000/signUp \
  --header 'Content-Type: application/json' \
  --data '{
	"firstName": "Manoj", "lastName": "Waikar", "email": "MW@intercax.com", "password": "123"
}'

**Sign-in**

curl --request POST \
  --url http://localhost:9000/signIn \
  --header 'Content-Type: application/json' \
  --data '{
	"email": "MW@intercax.com", "password": "123", "rememberMe": false
}'

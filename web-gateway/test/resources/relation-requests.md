**Create a relation**

curl -X POST \
  http://localhost:9000/relations \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 6119c998-d2f7-47f1-8f2d-c8cd05392444' \
  -H 'X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLWVQc1UwaFZ5MXIxV3NTQktTeXdYTytvOEQrZitrb2VTT1J2V2F0c2JUa0VLTGFvMW94eDVRRU41Q0kxSzNXSnZ6VFlnbkpiS25nWk12anJ4UjI1RDVwa2FwXC9vVHdxMnRFQ1FKZ0RWYiIsImlzcyI6IlN5bmRlaWEtQ2xvdWQtc2lsaG91ZXR0ZSIsInBlcm1pc3Npb24iOlsiYWRtaW4iLCJSRVBPU0lUT1JZX0NSRUFURSIsIlJFTEFUSU9OX0NSRUFURSIsIkFSVElGQUNUX0NSRUFURSJdLCJleHAiOjE1MzU2Njk2MTIsImlhdCI6MTUzNTYyNjQxMiwianRpIjoiMjJhYzRiZDJkN2YzNTJmN2IzNGRkOGNmYThmMmQyODViZjJlYjdmODBlM2NlYzJiNTUwZTU3ZGVmNGZlNjJmOGUxMGUxODExMzFhZjJlZTIyYWM4NzQ4ZGMxZGY1NWNkMjEyZDg0YjRiMjBmYzU4ZTA4NWI4MjUzM2ZkMjRkNTFhNjg1Y2U5MDRhMDg1MGM3MGY4ZjVkZjkzOTllNDE1ZTA5MjRlMjVlZmViMzRkMTFlMzg4OGFlNTllZWIzY2RmN2JkYmRiYmM4OTY0ZjE3MmNjMGRjNjBmZjY3MDJhZjVjYmMzZDFlMGVhZWZmZjU3MTI4MTIwMGJjOWIzOTg1NiJ9.YWJhxH5brqhepG182KUKZ8eSDnLmtjR6r8zgYrzYHTs' \
  -d '{
  	"name":"xcvxcvxcvxcvxcvxcv",
  	"type": {
  		"key":"TypeB1"
  	},
  	"container": {
  		"key":"ADC1"
  	},
  	"sourceArtifact": {
  		"key":"ART1",
  		"version":"12"
  	},
  	"targetArtifact": {
  		"key":"ART3",
  		"version":"2"
  	},
  	"attributes": {
          "weight": {
              "value": 2.2,
              "unit": "kg"
          },
          "isMandatory": {
              "value": false
          },
          "color": {
              "value": "red"
          },
          "age": {
              "value": 21,
              "unit": "years"
          },
          "possible values": {
              "value": [
                  {
                      "value": 1
                  },
                  {
                      "value": 2
                  }
              ]
          },
          "distance": {
              "value": 123456789,
              "unit": "m"
          }
      }
   }'
   
**Get a relation by key**

curl -X GET \
  http://localhost:9000/relations/CONT04-R21 \
  -H 'Cache-Control: no-cache' \
  -H 'Postman-Token: 51116637-bb82-4955-8027-35735faae9e7' \
  -H 'X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLTIrZVwvazdMYmFkajNZXC9cL2ZsNU1OeG9kR3BOWTZwOXBqNlNVdW9tMmtXSm9JcDduTktET1wvNmdid3FTOVVXMkhrXC9qUURQVDNkXC85VTNPdTNnSXQ3WUR4WDNPREYyaXhZUE5qWml3YzBwIiwiaXNzIjoiU3luZGVpYS1DbG91ZC1zaWxob3VldHRlIiwicGVybWlzc2lvbiI6WyJhZG1pbiIsIlJFUE9TSVRPUllfQ1JFQVRFIiwiUkVMQVRJT05fQ1JFQVRFIiwiQVJUSUZBQ1RfQ1JFQVRFIl0sImV4cCI6MTUzNDk2ODY3NCwiaWF0IjoxNTM0OTI1NDc0LCJqdGkiOiIzY2MyOWJhZGFmZWUyM2M3ZTFlYTkzYzFjMTJmMWFmZTBhODEzYzBjMjM3NmVmYzEyY2IzMmQ1OTlhODQwZTVlYmNlMGEzYzE3YzI3NTVkZjY0ZjczNGY1YzM2MTk1N2ZiNWU3ZDgxYzIzOTQ4ZDMxNWQ1ZTc3MmUzODY3YzA5ODczZmY5NjYyODE3Y2UxMGIxMTEwMjg2OTRiMzc2YTljMzllNzAzMTgxMDczMzRhMDYwN2IyZWZiOTg4MDYxZGRmN2QwNjBkNDQxMWMwNjBjODk2MWY0Y2FkY2UxZWQ1MzNhZWFiNjkxNjVjNDFhYjA3ZjJjZTQwNDk0MGRkOGY5In0.CFiE-0pRwlC8-C4UYgtTW71zBc1DPLsnsIUtwSuvHb0'
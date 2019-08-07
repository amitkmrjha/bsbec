**Create an artifact type**

curl --request POST \
  --url http://localhost:9000/types/artifact \
  --header 'Content-Type: application/json' \
  --header 'X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLXZsOGZnNHUrRHZcLys2UjRJZXVjVjVXV2NRSlF1UlMrS1ZhTnh1OVZvK09kTmtlQ05Xa2FjTnpiZUMyMVZ2THR6RDBicFlqNGUzU3gyOFpmTFgrdVVMUTE2SHU4bHExdFhWSU5rNXc9PSIsImlzcyI6IlN5bmRlaWEtQ2xvdWQtc2lsaG91ZXR0ZSIsInBlcm1pc3Npb24iOlsiYWRtaW4iLCJSRVBPU0lUT1JZX0NSRUFURSIsIlJFTEFUSU9OX0NSRUFURSIsIkFSVElGQUNUX0NSRUFURSJdLCJleHAiOjE1MzkwMzEyNjUsImlhdCI6MTUzODk4ODA2NSwianRpIjoiZDdkZDE0ZmMxMWVlZDc5YTcyZjA5MWZiYzc2OTk3MWI1NjU3ZGFkZTY3ZmUzZDA0MDk3NzdkNmUxNjhkNjgyZTc4MzUyZGZkZWExNWE2N2QzNWFhYmYzZTVhOTMyZjJmMzYxMGI5ZjNjNTlhMmY3NmNiOTZmMzNkNTU2NGNjZDBkOWJiMTFhZmM2YzlkZmViZGYwM2RlMjNhN2U4ODdiYmU2OWMxMDliODJjNzg1MzliMGVjYWIzNzhiZWRkMGQyYWQ5YWUyZDM3NzdkNDMxMTJlZmEzYWFhMjJjOWU3NTlmNDM4MGUzNmYyZDU2NGQ1NTIwZDYzNDhlZTY2MDQ4MCJ9.y833ZmRAlU6xMF8BeHLLhCHL5Yr45LfoW4QoEyaRHgA' \
  --data '{
	"name": "art-type 1",
	"attributeDefinitions": [
		{
			"id": "1",
			"type": "String",
			"metaType": "String",
			"name": "at-ad-2",
			"displayName": "art-type-attrib-def-1",
			"isCollection": false,
			"isModifiable": false,
			"isMandatory": true,
			"defaultValue": {
				"value": "Hello"
			},
			"allowedValues": [
				{
					"value": "Hi"
				},
				{
					"value": "Hey"
				}
			]
		}
	]
}'

**Get an artifact type by key**

curl --request GET \
  --url http://localhost:9000/types/artifact/ART-TYPE1 \
  --header 'X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLXZsOGZnNHUrRHZcLys2UjRJZXVjVjVXV2NRSlF1UlMrS1ZhTnh1OVZvK09kTmtlQ05Xa2FjTnpiZUMyMVZ2THR6RDBicFlqNGUzU3gyOFpmTFgrdVVMUTE2SHU4bHExdFhWSU5rNXc9PSIsImlzcyI6IlN5bmRlaWEtQ2xvdWQtc2lsaG91ZXR0ZSIsInBlcm1pc3Npb24iOlsiYWRtaW4iLCJSRVBPU0lUT1JZX0NSRUFURSIsIlJFTEFUSU9OX0NSRUFURSIsIkFSVElGQUNUX0NSRUFURSJdLCJleHAiOjE1MzkwMzEyNjUsImlhdCI6MTUzODk4ODA2NSwianRpIjoiZDdkZDE0ZmMxMWVlZDc5YTcyZjA5MWZiYzc2OTk3MWI1NjU3ZGFkZTY3ZmUzZDA0MDk3NzdkNmUxNjhkNjgyZTc4MzUyZGZkZWExNWE2N2QzNWFhYmYzZTVhOTMyZjJmMzYxMGI5ZjNjNTlhMmY3NmNiOTZmMzNkNTU2NGNjZDBkOWJiMTFhZmM2YzlkZmViZGYwM2RlMjNhN2U4ODdiYmU2OWMxMDliODJjNzg1MzliMGVjYWIzNzhiZWRkMGQyYWQ5YWUyZDM3NzdkNDMxMTJlZmEzYWFhMjJjOWU3NTlmNDM4MGUzNmYyZDU2NGQ1NTIwZDYzNDhlZTY2MDQ4MCJ9.y833ZmRAlU6xMF8BeHLLhCHL5Yr45LfoW4QoEyaRHgA'

**Update artifact type by key**

curl --request PUT \
  --url http://localhost:9000/types/artifact/ART-TYPE1 \
  --header 'Content-Type: application/json' \
  --header 'X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLVZXQ1E0YjQ2bjBrdytJXC9MNjVPQzh1VkpHOFdGS0hmY3BwSGxmXC9vS1wvbzlCQVBLazFCRkcwVWxhbStCT0NweHB0WFA2ckJNajJjbUVVQlZEY3NpNnh4dkZmanhHZ3AzYXVOU281Zz09IiwiaXNzIjoiU3luZGVpYS1DbG91ZC1zaWxob3VldHRlIiwicGVybWlzc2lvbiI6WyJhZG1pbiIsIlJFUE9TSVRPUllfQ1JFQVRFIiwiUkVMQVRJT05fQ1JFQVRFIiwiQVJUSUZBQ1RfQ1JFQVRFIl0sImV4cCI6MTUzNzgyMTM2MiwiaWF0IjoxNTM3Nzc4MTYyLCJqdGkiOiJjNGQ5ZTYyMDgwN2Y2ZGMyZjg2YjA2MzEyMTc5OWFjNzVjNWMwODNlMDFlYzY5Yzk5ODdkNDQyM2RjOWNjOGE3ODFhZjI4ZDk3ZjJkODhmMWExMzY0Y2U3MzRmNjlkM2NiMmM5MjlmYTExNGUzYTcyMjFjYTFiNjRmNWI1MjI3ZTVjOTViMDU2YTM2ZmEwMmRhNmMyYzY1ODVjNTZiNDVjODJiZTI5NDRlYzdjZTMyNWE4ZDU3YThhZjdkNjQ0NWFmOTg1MTAzN2RmNzkwNmQ4MDgxYmYyNTBjOWE2ODI1ODJhY2Q1NmFhNzNkM2Q4Nzg3ZDc4YTJiN2M1ZTA2OTA3In0.W9qaU2IAkUP6vtYbSWMP6Irja47rORr3A1H5YC9UFU8' \
  --data '{
	"name": "art-type 1 updated",
	"attributeDefinitions": [
		{
			"id": "1",
			"type": "String",
			"metaType": "String",
			"name": "at-ad-11",
			"displayName": "art-type-attrib-def-11",
			"isCollection": true,
			"isModifiable": true,
			"isMandatory": false,
			"defaultValue": {
				"value": "Hello1"
			},
			"allowedValues": [
				{
					"value": "Hi1"
				},
				{
					"value": "Hey1"
				}
			]
		}
	]
}'

**Delete an artifact type**

curl --request DELETE \
  --url http://localhost:9000/types/artifact/ART-TYPE1 \
  --header 'X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLXZsOGZnNHUrRHZcLys2UjRJZXVjVjVXV2NRSlF1UlMrS1ZhTnh1OVZvK09kTmtlQ05Xa2FjTnpiZUMyMVZ2THR6RDBicFlqNGUzU3gyOFpmTFgrdVVMUTE2SHU4bHExdFhWSU5rNXc9PSIsImlzcyI6IlN5bmRlaWEtQ2xvdWQtc2lsaG91ZXR0ZSIsInBlcm1pc3Npb24iOlsiYWRtaW4iLCJSRVBPU0lUT1JZX0NSRUFURSIsIlJFTEFUSU9OX0NSRUFURSIsIkFSVElGQUNUX0NSRUFURSJdLCJleHAiOjE1MzkwMzEyNjUsImlhdCI6MTUzODk4ODA2NSwianRpIjoiZDdkZDE0ZmMxMWVlZDc5YTcyZjA5MWZiYzc2OTk3MWI1NjU3ZGFkZTY3ZmUzZDA0MDk3NzdkNmUxNjhkNjgyZTc4MzUyZGZkZWExNWE2N2QzNWFhYmYzZTVhOTMyZjJmMzYxMGI5ZjNjNTlhMmY3NmNiOTZmMzNkNTU2NGNjZDBkOWJiMTFhZmM2YzlkZmViZGYwM2RlMjNhN2U4ODdiYmU2OWMxMDliODJjNzg1MzliMGVjYWIzNzhiZWRkMGQyYWQ5YWUyZDM3NzdkNDMxMTJlZmEzYWFhMjJjOWU3NTlmNDM4MGUzNmYyZDU2NGQ1NTIwZDYzNDhlZTY2MDQ4MCJ9.y833ZmRAlU6xMF8BeHLLhCHL5Yr45LfoW4QoEyaRHgA'

**Get artifact type attribute definition by id**

curl --request GET \
  --url http://localhost:9000/types/artifact/ART-TYPE1/attribdefs/1 \
  --header ': ' \
  --header 'X-Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxLUwxQ2N5TnExb0R1RW9ERGF6VVlTbkFpSko5NE9NcTlKbUl5ckNtSHZFaFJqbTVES0dOdFMyVENSZ2VqSk1WUmh3VEFOaGZKZFdTTXFMXC9UM3BxeEZVUVpaRG1kMWFmUTFzS09nZXc9PSIsImlzcyI6IlN5bmRlaWEtQ2xvdWQtc2lsaG91ZXR0ZSIsInBlcm1pc3Npb24iOlsiYWRtaW4iLCJSRVBPU0lUT1JZX0NSRUFURSIsIlJFTEFUSU9OX0NSRUFURSIsIkFSVElGQUNUX0NSRUFURSJdLCJleHAiOjE1Mzc5OTU3MzYsImlhdCI6MTUzNzk1MjUzNiwianRpIjoiYjI2MTM3YzJhNGQwNDlkODhkNjFkOTRhNTlkM2M3NzhjN2U1NGZhZTZiOGRmNzU5MWZkMGY2MzA2MzRhZjUyNDBhZTAyY2E4YzM5Zjk3N2JmZDUwOTk2ZDg2NmI5NjBiYTU5YTQ0OWExZjU1YWY4MWU2ZTMzODRhODIyNjNmZDBhMzAwMTJlYzY0ZGUyNDAwNDI4YTUxMzYxMjE3ZGI0M2Y4MzQ4Y2Y0MGEzY2QwZTIxODFhYmZlZDM5MGVkMjA1YTc4NDA3ZGE2ZDJhZTQyYjdiYTQ0N2YxNDgzNjZkMTJhNDIzNDIyNTEwOWYyNmFjYTgwMzk3ZmVmZDY3YmYxNyJ9.GDVsb5YJZcFpgXLhSpRtHj6gdgtZC-_9Fep0etL7kbc'

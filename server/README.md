## Config server application to store and serve configs

This application uses spring cloud config project to provide remote config server functionality.
A custom implementation of Environment repository is provided to customize the logic of serving config to client.

Has APIs to save configs to DB. 
http://localhost:9090/config-server/save
sample request payload
```
{
	"namespace" : "service1",
	"profile" : "dev",
	"configValues" : {
		"user":"foo",
		"email":"somebar.com"
	}
}
```

The stores config can be fetched using,
http://localhost:9090/config-server/fetch?nameSpace=service1&profile=dev&version=1
```
{
    "namespace": "service1",
    "profile": "dev",
    "version": 1,
    "configValues": {
        "user": "foo",
        "email": "somebar.com"
    }
}
```


The client integrating with the config server uses the folloeing api provided by the cloud config project
http://localhost:9090/config-server/config/{application}/{profile}/{label}
Ex: - http://localhost:9090/config-server/config/service1/dev/latest
Response :
```
{
    "name": "service1",
    "profiles": [
        "dev"
    ],
    "label": "latest",
    "version": null,
    "state": null,
    "propertySources": [
        {
            "name": "service1-dev",
            "source": {
                "user": "foo",
                "email": "somebar.com"
            }
        }
    ]
}
```
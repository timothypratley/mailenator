# Mailenator

Send email with great ease and comfort!



## Usage

To send a message from the command line:

`lein send '{"to": "timothypratley@gmail.com", "subject": "Example Email", "body": "<html><body><h1>Hello World!</h1></body></html>"}'`


To send a message from the command line using a template,
specify a template name and arguments:

`lein send '{"to": "timothypratley@gmail.com", "subject": "Example Email", "template": ["welcome", {"name": "Insect Overlords"}]}'`

Templates are HTML files in the resources directory.
Templates are applied according to [Mustache.](http://mustache.github.io/mustache.5.html)
In this example `{{name}}` will be replaced with `"Insect Overlords"`.

To send a message via the REST API:

Start the server.

To run the server in development mode (will reload code as it changes) from the command line:

`lein ring server-headless`

Or by running the server normally:

`lein listen`

Then use curl to send the message:

```
curl -H "Content-Type: application/json" \
     -H "Accept: text/javascript" \
     -X POST \
     -d '{"to": "timothypratley@gmail.com",
          "subject": "Example Email",
          "template": ["welcome", {"name": "Insect Overlords"}]}' \
     http://localhost:3000/send
```


## Queue Based

### Setup

OSX: `brew install rabbitmq` then `brew services start rabbitmq`

Other: See https://www.rabbitmq.com

### Start the consumer

`lein consume`

You can also optionally pass a JSON blobs which will be enqueued for consumption.

`lein consume '{"to": "timothypratley@gmail.com",
                        "subject": "Example Email",
                        "template": ["welcome", {"name": "Insect Overlords"}]}'`

### Enqueue a message

#### Via the RabbitMQ admin console

Open http://localhost:15672/ and login.
(On a fresh installation the user "guest" is created with password "guest".)
(https://www.rabbitmq.com/management.html)

Click on the Queues tab.
Click on Publish message.
Enter into the payload the JSON description of an email:

```json
{"to": "timothypratley@gmail.com",
 "subject": "Example Email",
 "template": ["welcome", {"name": "Insect Overlords"}]}
```

Click publish.


#### Via the REST API

Stop the consumer and start the REST server

`lein listen`

Use the "queue" endpoint instead of "send":

```
curl -H "Content-Type: application/json" \
     -H "Accept: text/javascript" \
     -X POST \
     -d '{"to": "timothypratley@gmail.com",
          "subject": "Example Email",
          "template": ["welcome", {"name": "Insect Overlords"}]}' \
     http://localhost:3000/queue
```

#### Cleanup

I recommend removing RabbitMQ when you are done with it:

```
brew services stop rabbitmq
brew remove rabbitmq
brew services cleanup
```



## Development

To run the integration tests:

`lein test integration`


To run unit tests:

`lein test` (or `lein test-refresh`)



## Deployment

`lein uberjar`

This project is currently for local development only.



## License

Copyright © 2017 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

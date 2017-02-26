# Mailenator

Send email with great ease and comfort!



## Usage

To send a message from the command line:

`lein send '{"to": "timothypratley@gmail.com", "subject": "Example Email", "body": "<html><body><h1>Hello World!</h1></body></html>"}'`


To send a message from the command line useing a template,
specify a template name and arguments:

`lein send '{"to": "timothypratley@gmail.com", "subject": "Example Email", "template": ["welcome", {"name": "Insect Overlords"}]}'`

Templates are HTML files in the resources directory.
Templates are applied according to [Mustache.](http://mustache.github.io/mustache.5.html)
In this example `{{name}}` will be replaced with `"Insect Overlords"`.

To send a message via the REST API:

Start the server (see Development below) then

`curl -H "Content-Type: application/json" \
    -X POST -d '{"to": "timothypratley@gmail.com", "subject": "Example Email", "template": ["welcome", {"name": "Insect Overlords"}]}'
    http://localhost:3000/send`



## Development

To run the integration tests:

`lein test integration`


To run unit tests:

`lein test` (or `lein test-refresh`)


To run the server in development mode from the command line:

`lein ring server headless`



## Deployment

`lein uberjar`



## License

Copyright © 2017 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

# Mailenator

Send email with great ease and comfort!



## Usage

To send a message from the command line:

`lein send '{"to": "timothypratley@gmail.com", "subject": "Example Email", "body": "<html><body><h1>Hello World!</h1></body></html>"}'`


To use a template, specify a template name and arguments:

`lein send '{"to": "timothypratley@gmail.com", "subject": "Example Email", "template": ["welcome", {"name": "Insect Overlords"}]}'`

Templates are HTML files in the resources directory.
Templates are [Mustache.](http://mustache.github.io/mustache.5.html)


## Development

To run the integration tests:

`lein test integration`


To run unit tests:

`lein test`
(or `lein test-refresh`)



## License

Copyright © 2017 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

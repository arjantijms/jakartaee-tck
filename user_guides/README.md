# TCK User Guides

This directory contains all the TCK User Guides.

All User Guides except for the "javaee" (platform and Web Profile)
User Guide are derived from a common template in the Template
directory.  After making changes to the template, the
apply_template.sh script can be used to apply the changes to
all the derived TCK User Guides.  This is a bit of a kludge
and really should be done at build time using Maven; that's a
project for the future.

Note that when using the template for a particular specification's
TCK User Guide, none of the *.adoc files should be modified.
See the README file in Template/src/main/jbake/content.

## Pre requisites

- Maven
- JDK8+

Deploying to Github will require password less authentication.

This is done by exporting your SSH public key into your Github account.

## Build the site locally

The site is generated under target/staging.

Open file:///PATH_TO_PROJECT_DIR/target/staging in a browser to view the site.

```
mvn generate-resources
```

Or you can invoke the JBake plugin directly.

```
mvn jbake:build
```

### Rebuild the site on changes

```
mvn jbake:watch
```

If you keep this command running, changes to the sources will be
detected and the site will be rendered incrementally.

This is convenient when writing content.

### Serve the site locally

```
mvn jbake:serve
```

If a webserver is required (e.g. absolute path are used), this command
will start a webserver (jetty) at http://localhost:8820.  It will also
watch for changes and rebuild incrementally.

## Deploy the site to Github Pages

```
mvn deploy
```

## Produce a zip file for download

To produce a zip file containing the generated html files, use:

```
mvn package
```

When making a release on GitHub, this zip file should be added to the release.

## Links

- [JBake maven plugin documentation](https://github.com/Blazebit/jbake-maven-plugin)
- [JBake documentation](http://jbake.org/docs/2.5.1)
- [Freemarker documentation](http://freemarker.org/docs)
- [AsciiDoc User Guide](http://asciidoc.org/userguide.html)
- [Asciidoctor quick reference](http://asciidoctor.org/docs/asciidoc-syntax-quick-reference)

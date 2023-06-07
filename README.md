Standalonetools is a set of helper tools for building standalone Java applications.


## Building

The library is build using maven through github actions and is published in maven central.
You probably never need to build it yourself. But if you want to, you can find the exact maven commands that were used to build the artifacts in the files in .github/workflows/

## Using

Add one (or all) of the following maven dependencies to your project.

```
<dependency>
	<groupId>be.vito.rma.standalonetools</groupId>
	<artifactId>standalonetools-common</artifactId>
	<version>3.0.0-SNAPSHOT</version>
</dependency>
```
```
<dependency>
	<groupId>be.vito.rma.standalonetools</groupId>
	<artifactId>standalonetools-spring</artifactId>
	<version>3.0.0-SNAPSHOT</version>
</dependency>
```

## Version history

see Changelog.txt
# Scribe

## Setup

To add scribe to your mod add the following to your build.grade:

```
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	[...] //Existing Dependencies

	//Scribe, Replace 1.0.0 with desired version
	modImplementation 'com.github.outercloudstudio:Scribe:1.0.0'
}
```

## License

This library is liscensed under the MIT liscense.

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

	//Scribe
	modImplementation 'com.github.outercloudstudio:Scribe:<Desired Scribe Commit ID>'
}
```

## License

This library is liscensed under the MIT liscense.

# Scribe

## Setup

To add scribe to your mod add the following to your build.grade:

```
repositories {
	maven {
		url = uri("https://maven.pkg.github.com/outercloudstudio/scribe")
		credentials {
			username = "outercloudstudio"
			password = "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"
		}
	}
}

dependencies {
	[...] //Existing Dependencies

	//Scribe
	modImplementation 'com.outercloud.scribe:scribe:1.0.0' // Replace 1.0.0 with your desired version!
}
```

## License

This library is liscensed under the MIT liscense.

# Scribe

## Setup

To add Scribe to your mod add the following to your build.grade:

```
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	[...] //Existing Dependencies

	//Scribe, Replace 1.1.0 with desired version
	modImplementation 'com.github.outercloudstudio:Scribe:1.1.0'
}
```

## How To Use
Check the [Wiki](https://github.com/outercloudstudio/Scribe/wiki) for documentation and guides.

## License

This library is licensed under the MIT license.

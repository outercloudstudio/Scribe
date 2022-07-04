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

	modImplementation "com.github.outercloudstudio:Scribe:${scribe_version}"
}
```

Then in your gradle.properties add:
```
    scribe_version= //The desired scribe version
```

## How To Use
Check the [Wiki](https://github.com/outercloudstudio/Scribe/wiki) for documentation and guides.

## License

This library is licensed under the MIT license.

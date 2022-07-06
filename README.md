# Scribe
## What Is Scribe?
Scribe is a library meant to reduce code and simplify common things done in modding. At the moment this includes registering certain features and using a config.

## Where To Download?
[Download On Curseforge](https://www.curseforge.com/minecraft/mc-mods/scribe)

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

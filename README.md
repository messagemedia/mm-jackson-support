# MessageMedia Utility Classes for Jackson (Java JSON lib)

This repo contains utility/helper classes for Jackson.

## Currently it contains the following modules:

### jackson-support-core
This includes only classes which work with the core Jackson and have no further dependencies.

#### ValueWithNull
This class allows you to distinguish between an explicit null in the JSON and an absent value.
```
public class MyBean {

    @JsonProperty("foobar")
    public ValueWithNull<String> foobar;
    ...
}
```

- JSON ````{}```` => foobar is null
- JSON ````{ "foobar" : null }```` => foobar.isExplicitNull() is true
- JSON ````{ "foobar" : "bla" }```` => foobar.get() returns bla


## Compatibility issues

- Currently there are no known issues with any version other than the specified one in the pom. But please test carefully if you are using another version.

## Installation

### Maven
TODO upload to maven central
